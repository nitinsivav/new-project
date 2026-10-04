# Phase 2: User Registration Handoff

## 1. Objective and Phase Purpose
This phase successfully implemented a full-stack, framework-free user registration system. The flow securely receives user input (name, phone, email, password) from a vanilla HTML/JS frontend, hashes the password using BCrypt on a raw Java `HttpServer` backend, and persists it into a MySQL database via standard JDBC. 

## 2. Final Repository State & Architecture
The project successfully established a structured layered architecture under `com.project`, moving away from the initial flat `com.example` package.

### Architecture Highlights
- **Backend:** Plain Java `HttpServer` on port `8080`. No Spring, Hibernate, or other heavy application frameworks.
- **Database:** MySQL accessed via raw JDBC using `mysql-connector-j`.
- **Frontend:** Vanilla HTML/CSS/JS (no React/Vue). A premium glassmorphism dark mode aesthetic was implemented per design requirements.
- **Security:** Passwords are mathematically hashed using BCrypt. The app runs against a dedicated MySQL user account, not root.

### Dependencies (in `build.gradle`)
- `mysql:mysql-connector-j:9.0.0`
- `org.mindrot:jbcrypt:0.4`
- `com.google.code.gson:gson:2.10.1`
- `org.junit.jupiter:junit-jupiter-api` & `junit-jupiter-engine`

### Final Files Created/Modified
- `src/main/resources/application.properties`: Contains the connection string, DB user `java_app`, and credentials.
- `src/main/resources/schema.sql`: Contains the database, table, and user creation scripts.
- `src/main/java/com/project/Main.java`: Starts the HTTP server and registers the handler.
- `src/main/java/com/project/model/User.java`: Simple POJO representing the users table.
- `src/main/java/com/project/config/DatabaseConfig.java`: Centralized JDBC connection factory.
- `src/main/java/com/project/repository/UserRepository.java`: JDBC insert logic for the database.
- `src/main/java/com/project/server/RegistrationHandler.java`: HTTP endpoint handling JSON parsing, hashing, CORS, and persistence.
- `src/test/java/com/project/repository/UserRepositoryTest.java`: Integration test asserting repository insertion.
- `src/test/java/com/project/server/RegistrationHandlerTest.java`: E2E integration test against a real HTTP server.
- `frontend/index.html`, `frontend/css/style.css`, `frontend/js/app.js`: Registration frontend files.
- `.gitignore`: Updated with `/build/`, `/.gradle/`, `*.class`, etc.

## 3. Chronological Implementation Journey & Debugging

1. **Database Schema & Security:**
   - **Failure:** Initially running `mysql -u root -p < schema.sql` failed with `Access denied`.
   - **Root Cause & Fix:** Linux MySQL uses `auth_socket` by default for root, meaning standard password authentication fails. Executing it via OS root (`sudo mysql -u root`) succeeded. 
   - **Decision:** To prevent the app from running as root, a dedicated `java_app` database user was created with restricted privileges to only the `framework_free_db` schema.
2. **Architecture Restructuring:**
   - Transitioned from `com.example` to `com.project` (`model`, `repository`, `server`, `config`) to prevent technical debt.
3. **Repository TDD & Constraints:**
   - **Failure:** The `email` column was correctly set to `UNIQUE`. However, running `UserRepositoryTest` twice with a hardcoded email caused a `SQLIntegrityConstraintViolationException`.
   - **Fix:** Appended `System.currentTimeMillis()` to the test emails to ensure they are unique per run.
4. **Model Debugging:**
   - **Failure:** `RegistrationHandler` failed to compile because `user.setPassword(hashedPassword)` was missing.
   - **Fix:** Added the missing `setPassword()` mutator to the `User` POJO.
5. **Endpoint TDD & CORS:**
   - **Failure:** `RegistrationHandlerTest` also hit duplicate email constraints due to hardcoded test data. Fixed similarly to the repository test.
   - Added `Access-Control-Allow-Origin: *` to `RegistrationHandler` to prevent cross-origin resource sharing (CORS) blocks during frontend development.

## 4. HTTP Endpoint Behavior (`POST /api/register`)
- Accepts standard HTTP POST with JSON body.
- Uses `Gson` to deserialize the body to a `User` object.
- Automatically hashes the `password` using `BCrypt.hashpw()`.
- Defers database insertion to `UserRepository.save()`.
- Returns `201 Created` with a JSON success message on success.
- Returns `405 Method Not Allowed` for non-POST/OPTIONS requests.
- Returns `500 Internal Server Error` on SQL exceptions (like duplicate emails).
- Returns `204 No Content` for CORS `OPTIONS` pre-flight requests.

## 5. Security & Operational Considerations
- **Testing against Real DB:** Tests currently execute against the primary `framework_free_db` database instead of a dedicated, ephemeral test database. This causes test records to pollute the local development database over time.
- **CORS:** CORS is currently set to `*` to facilitate local file-system development. This would need to be restricted in a production environment.
- **Passwords:** Are safely hashed (BCrypt) before entering the DB, never logged plaintext.

## 6. Git Status & Final Repository Tree
**The phase is not committed yet.**
Currently, `git status` shows all files under `src/main/java/com/project/`, `src/test/`, `frontend/`, `schema.sql`, and `build.gradle` as modified or untracked. The last commit was the initial initialization.

### Final Tree
```text
/home/nitinsivav/Documents/new-project
├── build.gradle (modified: added gson, jbcrypt, changed mainClass)
├── settings.gradle
├── .gitignore (modified: added build/, .gradle/, *.class)
├── README.md
├── context/
│   ├── phase-01-framework-free-boilerplate.md
│   └── phase-02-user-registration.md (created)
├── frontend/
│   ├── index.html (modified: registration form added)
│   ├── css/
│   │   └── style.css (modified: glassmorphism styling)
│   └── js/
│       └── app.js (modified: fetch API logic)
└── src/
    ├── main/
    │   ├── java/com/project/ (moved from com/example/)
    │   │   ├── Main.java (created)
    │   │   ├── config/DatabaseConfig.java (created)
    │   │   ├── model/User.java (created)
    │   │   ├── repository/UserRepository.java (created)
    │   │   └── server/RegistrationHandler.java (created)
    │   └── resources/
    │       ├── application.properties (modified: java_app credentials)
    │       └── schema.sql (modified: users table, java_app user)
    └── test/java/com/project/
        ├── repository/UserRepositoryTest.java (created)
        └── server/RegistrationHandlerTest.java (created)
```

## 7. Deferred / Out-of-Scope Work
- Login and session management (JWT or cookies).
- Password reset and email verification mechanisms.
- Extensive backend data validation (e.g., regex checking the email format before DB insertion).
- Setting up a completely isolated, repeatable test database for unit/integration testing.

## 8. Resume From Here

### Current State
User registration is fully implemented end-to-end, tested, and manually verified using MySQL Workbench and the live browser frontend. The Java `HttpServer` runs natively on port `8080`.

### Next Logical Phase
The next logical phase is **User Login and Session Management**, allowing registered users to actually authenticate and receive an identity token/session.

### Important Instructions for the Next Agent
- **DO NOT** introduce heavy frameworks like Spring, Hibernate, or React. This project must remain framework-free (Java `HttpServer`, raw JDBC, Vanilla JS).
- **PRESERVE** the `com.project` layered packaging architecture (`model`, `repository`, `server`, `config`, `service`).
- **PRESERVE** the glassmorphism, high-aesthetic styling rules established in `style.css`.
- Proceed with TDD (RED-GREEN) for all new endpoints and repository logic.
