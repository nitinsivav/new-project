# Phase 3: Serve the Frontend Through the Java HTTP Server

## 1. Phase Title and Objective
**Phase Title:** Serve the Frontend Through the Java HTTP Server
**Objective:** Modify the existing framework-free full-stack project so that the native Java `HttpServer` natively serves the static frontend assets (HTML, CSS, JavaScript) from the `frontend/` directory, entirely replacing the need for the external VS Code Live Server extension. The application should natively load at `http://localhost:8080/` while preserving the existing `/api/register` backend functionality.

## 2. Starting State and Intended Behavior
**Starting State:** 
- The project consisted of a Java `HttpServer` bound to port `8080` with a single context mounted at `/api/register`.
- The frontend assets (`frontend/index.html`, `frontend/css/style.css`, `frontend/js/app.js`) resided on the filesystem but were only accessible via external local web servers (like VS Code Live Server).
**Intended Behavior:**
- HTTP GET requests to `http://localhost:8080/` should return the `index.html` file with a `text/html` content type.
- HTTP GET requests to other static paths (like `/css/style.css`) should resolve correctly against the `frontend/` folder and return the accurate MIME content type.
- Access to files outside the `frontend/` directory must be strictly forbidden (preventing directory traversal attacks).

## 3. Final Implementation Outcome
The Java HTTP Server was successfully expanded to serve static files. The `com.project.server.StaticFileHandler` was implemented and securely mounted to the `/` root context in `Main.java`. Security checks and MIME-type deduction were implemented via standard Java standard libraries without relying on heavy frameworks.

## 4. Chronological Implementation Journey
1. **Inspection Phase:** A safe, read-only inspection was performed to verify the exact paths of the `frontend/` directory and ensure that the Gradle `run` task executes from the project root (making `new File("frontend")` natively accessible).
2. **RED Step (TDD):** A deliberately failing `StaticFileHandler` skeleton was created, returning an `UnsupportedOperationException`. A JUnit 5 integration test (`StaticFileHandlerTest`) was authored to spin up a temporary server on port `8082`, issue a GET request to `/`, and expect a `200 OK` response with `text/html`. 
3. **Observation:** The test was executed via `./gradlew test` and failed exactly as expected, raising a `java.net.SocketException` because the handler threw an exception and dropped the connection.
4. **GREEN Step:** The `StaticFileHandler` was fully implemented with canonical path boundary checks, 404 handling, and file streaming. 
5. **Main.java Integration:** `server.createContext("/", new StaticFileHandler());` was appended to `Main.java`.
6. **Debugging:** The user observed a compilation error (`error: cannot find symbol class StaticFileHandler`) when executing `./gradlew run`. The root cause was a missing import in `Main.java`. The `import com.project.server.StaticFileHandler;` statement was added, resolving the error.

## 5. Files Created, Modified, or Untouched
- **Created:** `src/main/java/com/project/server/StaticFileHandler.java` (Static file serving logic)
- **Created:** `src/test/java/com/project/server/StaticFileHandlerTest.java` (Automated integration test for the static handler)
- **Modified:** `src/main/java/com/project/Main.java` (Added the root context registration and the required import)
- **Intentionally Untouched:** `frontend/` files (HTML/CSS/JS), `build.gradle`, `RegistrationHandler.java`, `UserRepository.java`, and the MySQL database schema remained structurally identical to maintain separation of concerns.

## 6. Handler Routing and Content Types
The `StaticFileHandler` extracts the path using `exchange.getRequestURI().getPath()`.
- If the path is exactly `/`, it defaults to `/index.html`.
- It appends the path to a virtual root `new File("frontend")`.
- It conditionally sets the `Content-Type` header based on basic extension matching:
  - `.html` -> `text/html`
  - `.css` -> `text/css`
  - `.js` -> `application/javascript`
  - *fallback* -> `application/octet-stream`

## 7. Coexistence with `/api/register`
The root `/` handler coexists perfectly with `/api/register`. `Main.java` binds `/api/register` specifically to `RegistrationHandler`, and `/` as the catch-all to `StaticFileHandler`. The Java `HttpServer` natively prioritizes the most specific path match, guaranteeing that API requests never conflict with static asset requests.

## 8. Security Considerations (Path Traversal)
A strict canonical path boundary check was implemented. The handler calculates `requestedFile.getCanonicalPath()` and enforces that it `startsWith(frontendDir.getCanonicalPath())`. 
- **Permitted:** `http://localhost:8080/css/style.css` resolves cleanly.
- **Forbidden:** Requests attempting directory traversal like `http://localhost:8080/../../build.gradle` fall outside the canonical prefix and immediately yield an HTTP `403 Forbidden` response.

## 9. Relevant Project Paths, Ports, and Commands
- **Project Root:** `/home/nitinsivav/Documents/new-project`
- **Application Port:** `8080` (Real Application)
- **Test Port:** `8082` (Used exclusively by `StaticFileHandlerTest` to avoid `BindException` port collisions while the main app is running).
- **Commands:** `./gradlew test` (to run the automated test suite) and `./gradlew run` (to start the long-running application).

## 10. Tests and Builds Executed
- **Automated Tests:** `./gradlew test` was executed. The build reported `BUILD SUCCESSFUL`. The `testServeIndexHtml()` assertion natively confirmed that a real HTTP GET request dynamically fetched the HTML file bytes and headers correctly.
- **Application Launch:** The user executed `./gradlew run` and the console output confirmed `Backend server is running on http://localhost:8080` running at 75% execution state natively blocking the thread as intended.

## 11. Failures, Error Messages, and Fixes
- **Error:** `java.net.SocketException at StaticFileHandlerTest.java:34`
  - **Root Cause:** Expected behavior during the RED phase because the skeleton threw an `UnsupportedOperationException`. 
  - **Fix:** Implemented the actual file streaming logic (GREEN phase).
- **Error:** `error: cannot find symbol server.createContext("/", new StaticFileHandler());`
  - **Root Cause:** Missing import statement in `Main.java`.
  - **Fix:** Added `import com.project.server.StaticFileHandler;`.

## 12. Remaining Risks, Limitations, and Unverified Behavior
- **Verified (Code & Tests):** The Java backend successfully hosts the files on `8080` and passes automated integration tests mimicking browser behavior.
- **Not Verified (Human Runtime):** Although the `run` command succeeded, there is no physical evidence in the transcript (like a successful browser network log or a follow-up E2E database insert) confirming that the user successfully opened `http://localhost:8080/` in their browser and observed the CSS/JS rendering properly. 
- **Limitation:** The basic content-type deduction logic handles only HTML, CSS, and JS. Serving images (PNG/SVG) or fonts would currently return an `application/octet-stream`, which could cause browser rendering issues.

## 13. Git Status
- **Commit History:** The repository sits at `10ddc2b5` ("Implement User registration with frontend UI , Java backend and MySQL database").
- **Working Tree:** The phase 3 files (`StaticFileHandler`, `StaticFileHandlerTest`, and the modified `Main.java`) are currently uncommitted (untracked/modified).

## 14. Resume From Here
**Current State:**
The application is a fully integrated, self-contained monolith. The framework-free Java server on port `8080` simultaneously hosts the HTML/CSS/JS frontend on the root `/` path and provides a secure, JDBC-backed registration API on `/api/register`. 

**Next Logical Steps:**
1. Manually verify the frontend fully renders and functions via `http://localhost:8080/` in a web browser without the Live Server extension.
2. Commit the codebase to Git.
3. Advance to the next feature (e.g., User Authentication/Login) expanding upon the established framework-free architecture. 

**Instructions for Future Agents:**
- Do **not** modify the current `com.project` structure.
- Do **not** introduce heavy frameworks (Spring/React).
- All static assets must remain in the `frontend/` folder, natively served by `StaticFileHandler`.
- All new API endpoints must be mounted explicitly in `Main.java` and adhere to the established JDBC patterns.
