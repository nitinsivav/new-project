# Phase 1: Framework-Free Web Application Boilerplate / Workspace Initialization

## Purpose
Initialize a new, entirely framework-free web application workspace. Establish the foundational directory structure, Gradle build configuration, testing dependencies, vanilla frontend scaffolding, and a minimal standard Java backend entry point.

## Starting State
- **Workspace:** Empty directory (`/home/nitinsivav/Documents/new-project`)
- **Java/JDK version detected:** 21.0.9 (Java 21 LTS)
- **Gradle version detected:** 8.14.4
- **Project Configuration:** None existed initially.
- **Gradle Wrapper:** Did not exist.

## Architecture and Technology Choices
The project is strictly educational and framework-free.
- **Frontend:** HTML, CSS, Vanilla JavaScript (no React, Angular, Vue, etc.)
- **Backend:** Java (using standard capabilities like `HttpServer` eventually; no Spring, Spring Boot, or Express)
- **Database:** MySQL
- **Database Communication:** JDBC (no Hibernate or JPA)
- **Build System:** Gradle (used only for build and dependency management)

## Final Outcome
- ✅ Basic project structure initialized.
- ✅ Gradle configuration and wrapper set up.
- ✅ Frontend scaffolding created and wired together.
- ✅ Java backend entry point established.
- ✅ Project documentation (`README.md`) established constraint rules.

## Implementation Journey
1. **Initialize Gradle Wrapper and base directories:** Attempted to run `gradle wrapper` in the empty directory, which failed because Gradle requires project configuration first. Created `settings.gradle` with `rootProject.name = "new-project"`, after which `gradle wrapper` succeeded. Created the standard Maven/Gradle directory structure along with custom `frontend` directories.
2. **Configure Gradle build:** Created `build.gradle` applying `java` and `application` plugins. Added `com.mysql:mysql-connector-j:8.3.0` for future database connectivity and JUnit 5 for planned TDD implementation.
3. **Set up minimal frontend:** Created `index.html`, `css/style.css`, and `js/app.js` to serve as a basic framework-free frontend scaffolding.
4. **Set up minimal Java backend:** Created `com.example.Main` with a simple standard `public static void main(String[] args)` to serve as the backend entry point.
5. **Add documentation:** Created a `README.md` to document the strict framework-free constraint for future work.

## Repository Changes
- `settings.gradle`: Created to define the project root name.
- `gradlew`, `gradlew.bat`, `gradle/wrapper/*`: Generated to ensure reproducible builds.
- `build.gradle`: Created to define the build configuration, plugins, dependencies, and test task.
- `src/main/java/com/example/Main.java`: Created as the Java application entry point.
- `frontend/index.html`: Created as the main frontend page.
- `frontend/css/style.css`: Created for basic styling.
- `frontend/js/app.js`: Created for basic DOM interaction.
- `README.md`: Created to document the project rules.

## Important Code and Configuration
**Gradle Dependencies (`build.gradle`):**
```gradle
dependencies {
    // Standard JDBC driver for MySQL
    implementation 'com.mysql:mysql-connector-j:8.3.0'

    // JUnit 5 for TDD implementation
    testImplementation platform('org.junit:junit-bom:5.10.2')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```

**Application Entry Point (`build.gradle`):**
```gradle
application {
    mainClass = 'com.example.Main'
}
```

## Commands and Operations
1. `java -version`: Verified JDK 21.0.9.
2. `gradle -v`: Verified Gradle 8.14.4.
3. `gradle wrapper`: Initially failed (no Gradle build found). Succeeded after creating `settings.gradle`.
4. `mkdir -p src/main/java src/main/resources src/test/java frontend/css frontend/js`: Created project directories.
5. `./gradlew build`: Initially failed due to a typo in the test configuration. Succeeded after correction.
6. `./gradlew run`: Initially failed due to a missing semicolon in `Main.java`. Succeeded after correction.

## Verification
- **Gradle Wrapper exists:** Yes.
- **Gradle build succeeds:** Yes (`./gradlew build` ran successfully).
- **Java application runs successfully:** Yes (`./gradlew run` outputted "Backend Setup Successfully").
- **Frontend files exist & wired correctly:** Yes (verified via manual browser testing context).
- **README exists:** Yes.
- **Dependencies configured correctly:** Yes.

## Failures and Fixes
- **Gradle Wrapper Error:** Running `gradle wrapper` initially failed because the directory lacked a `settings.gradle` file. **Fix:** Created `settings.gradle` first.
- **Gradle Build Error:** `test.named('test')` was placed incorrectly inside the `dependencies` block, causing a "Could not find method named()" error. **Fix:** Moved the block outside `dependencies` and corrected the syntax to `tasks.named('test')`.
- **Java Compilation Error:** Missing semicolon in `Main.java` line 5 (`System.out.println("Backend Setup Successfully")`). **Fix:** Added the semicolon.

## Current JDBC/MySQL State
- **MySQL Connector/J dependency available:** Yes, via Gradle.
- **Java ↔ JDBC ↔ MySQL Connection:** **NOT YET IMPLEMENTED.**
  - No JDBC connection implementation has been created.
  - No database credentials/configuration has been implemented.
  - No MySQL tables/schema have been created.
  - No SQL queries have been implemented.
  - Adding the JDBC driver dependency does NOT mean a database connection exists.

## Final Technical State
The project is a standard Java application utilizing Gradle for builds. It includes the MySQL JDBC driver but does not yet connect to a database. The frontend is a static HTML/CSS/JS scaffolding located in a dedicated `frontend` folder. The application is completely framework-free.

## Deferred / Out of Scope
- User Registration (planned for the next phase)
- Authentication (login/sessions/JWT)
- Database schema and table creation
- Actual JDBC connection logic and queries
- Any form of application business logic

## Commit Linkage
- **Git Repository:** Not yet initialized. (Running `git status` resulted in a fatal error indicating this is not a git repository).
- **Commit Status:** Phase 1 is not committed yet.

## Resume From Here
The project has successfully established a clean boilerplate without any heavy frameworks. 
- You have an empty, functioning standard Java entry point in `src/main/java/com/example/Main.java`.
- You have an isolated vanilla frontend in the `frontend/` directory.
- Gradle is fully configured and working (use `./gradlew build` and `./gradlew run`).
- The MySQL JDBC driver is available on the classpath.

**Next step:** Implement user registration using strict TDD, ensuring you manually wire the HTTP server, JDBC connection, and frontend behavior without relying on external frameworks.
