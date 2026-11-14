# Student Management System - REST API

A production-grade RESTful API developed with **Spring Boot 3**, **Spring Data JPA**, and **MySQL** that provides comprehensive student record management capabilities.

---

## 📋 Table of Contents
1. [Project Overview](#-project-overview)
2. [Tech Stack](#-tech-stack)
3. [System Architecture & Project Structure](#-system-architecture--project-structure)
4. [Student Entity Specifications](#-student-entity-specifications)
5. [REST API Endpoints](#-rest-api-endpoints)
6. [Database Configuration & Setup](#-database-configuration--setup)
7. [Running the Application](#-running-the-application)
8. [Automated Testing](#-automated-testing)
9. [Postman API Testing & Screenshots Guide](#-postman-api-testing--screenshots-guide)
10. [GitHub Repository Submission Guide](#-github-repository-submission-guide)

---

## 🚀 Project Overview

The **Student Management System** is built to facilitate CRUD operations on student records with robust input validation, centralized exception handling, and persistent relational data storage using MySQL.

### Core Features
- ✅ **Full CRUD Operations**: Create, Read, Update, and Delete student records.
- ✅ **Course-Based Filtering**: Retrieve all students enrolled in a specific course (`/api/students/course/{course}`).
- ✅ **Spring Data JPA**: Clean repository abstractions without manual boilerplate SQL.
- ✅ **Global Exception Handling**: Returns standardized JSON error structures and accurate HTTP status codes (`404 Not Found`, `400 Bad Request`, `500 Internal Server Error`).
- ✅ **Input Validation**: Enforces `@NotBlank`, `@Email`, and `@Min` constraints on student fields.
- ✅ **Postman Ready**: Pre-configured Postman Collection with automated test scripts included.

---

## 🛠 Tech Stack

| Technology | Version / Specification |
| :--- | :--- |
| **Java** | OpenJDK 17 / 21 / 25 |
| **Framework** | Spring Boot 3.3.5 |
| **Persistence** | Spring Data JPA / Hibernate 6 |
| **Database** | MySQL 8.x (with H2 profile available for quick local testing) |
| **Validation** | Jakarta Bean Validation (Hibernate Validator) |
| **Build Tool** | Apache Maven 3.9+ (includes Maven Wrapper `mvnw`) |
| **Testing** | JUnit 5, Mockito, Spring Boot MockMvc |
| **API Client** | Postman |

---

## 🏗 System Architecture & Project Structure

The project strictly adheres to the standard layered architecture pattern:

```
c:\Users\aades\Desktop\24BCS12012_JAVA
├── pom.xml                                      # Maven dependencies and build plugins
├── mvnw / mvnw.cmd                              # Maven wrapper scripts
├── .gitignore                                   # Standard Java/Maven ignore rules
├── README.md                                    # Complete project documentation
├── database/
│   └── student_db.sql                           # MySQL schema & sample data script
├── postman/
│   └── Student_Management_System.postman_collection.json # Exportable Postman collection
└── src/
    ├── main/
    │   ├── java/com/studentmanagement/
    │   │   ├── StudentManagementApplication.java# Spring Boot bootstrap class
    │   │   ├── controller/
    │   │   │   └── StudentController.java       # REST Controller exposing /api/students
    │   │   ├── entity/
    │   │   │   └── Student.java                 # JPA Entity with validation annotations
    │   │   ├── exception/
    │   │   │   ├── ErrorDetails.java            # Standardized error response payload
    │   │   │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice centralized handler
    │   │   │   └── ResourceNotFoundException.java # Custom 404 RuntimeException
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java       # Spring Data JPA repository
    │   │   └── service/
    │   │       ├── StudentService.java          # Service business interface
    │   │       └── impl/
    │   │           └── StudentServiceImpl.java  # Service implementation with @Transactional
    │   └── resources/
    │       ├── application.properties           # MySQL production database configuration
    │       └── application-h2.properties        # Fallback H2 in-memory profile
    └── test/
        ├── java/com/studentmanagement/
        │   ├── StudentControllerTest.java       # MockMvc integration tests (10 test cases)
        │   └── StudentServiceTest.java          # Mockito unit tests (8 test cases)
        └── resources/
            └── application.properties           # Test profile using in-memory H2
```

---

## 📊 Student Entity Specifications

The `Student` entity maps to the `students` table in the database:

| Attribute | Data Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | Primary Key, Auto-increment | Unique student identifier |
| `name` | `String` | `@NotBlank`, Not Null | Student's full name |
| `email` | `String` | `@NotBlank`, `@Email`, Unique, Not Null | Student's unique email address |
| `course` | `String` | `@NotBlank`, Not Null | Enrolled academic program / course |
| `age` | `Integer` | `@NotNull`, `@Min(1)` | Student age (must be positive) |

---

## 🌐 REST API Endpoints

| HTTP Method | Endpoint | Description | Request Body | Success Status | Failure Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/students` | Add a new student | JSON Student object | `201 Created` | `400 Bad Request` |
| `GET` | `/api/students` | Retrieve all students | None | `200 OK` | `500 Server Error` |
| `GET` | `/api/students/{id}` | Retrieve student by ID | None | `200 OK` | `404 Not Found` |
| `PUT` | `/api/students/{id}` | Update student details | JSON Student object | `200 OK` | `404 Not Found` / `400 Bad Request` |
| `DELETE` | `/api/students/{id}` | Delete a student | None | `200 OK` | `404 Not Found` |
| `GET` | `/api/students/course/{course}` | Retrieve students by course | None | `200 OK` | `500 Server Error` |

---

## 🗄 Database Configuration & Setup

### 1. MySQL Setup
Ensure your MySQL server is running (e.g., via MySQL Workbench, MySQL Server Service, or XAMPP).

Open MySQL Command Line Client or Workbench and execute the script located at `database/student_db.sql`:

```sql
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    course VARCHAR(255) NOT NULL,
    age INT NOT NULL
);
```

### 2. Configure Credentials in `application.properties`
Open `src/main/resources/application.properties` and verify your MySQL username and password:

```properties
# MySQL Connection Settings
spring.datasource.url=jdbc:mysql://localhost:3306/student_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate & JPA Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

> **Note**: `createDatabaseIfNotExist=true` will automatically create the database `student_db` when the application starts, and `ddl-auto=update` will generate the `students` table automatically.

---

## 🚀 Running the Application

### Option A: Standard Run with MySQL (Default)
In your terminal, navigate to the project directory and run:

```bash
mvn spring-boot:run
```
Or using the Maven Wrapper:
```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on **`http://localhost:8080`**.

### Option B: Quick Run with In-Memory H2 Database
If you do not have MySQL running right now and want to immediately test all REST endpoints:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
In this mode, an in-memory database is used, and the H2 Web Console is accessible at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:student_db`, User: `sa`, Password: empty).

---

## 🧪 Automated Testing

The project includes **18 automated unit and integration tests** verifying all controller endpoints, service methods, validation constraints, and error scenarios.

Execute all tests with:
```bash
mvn test
```

Expected output:
```
[INFO] Running com.studentmanagement.StudentControllerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.studentmanagement.StudentServiceTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📮 Postman API Testing & Screenshots Guide

A complete Postman collection is included in the project:
📁 `postman/Student_Management_System.postman_collection.json`

### Step 1: Import the Collection into Postman
1. Open **Postman**.
2. Click the **Import** button in the top-left corner.
3. Drag and drop or browse to select `postman/Student_Management_System.postman_collection.json`.
4. Click **Import**. You will see the collection named **"Student Management System API"**.

---

### Step 2: Test Endpoints & Take Screenshots

Here are the detailed steps and payloads to run in Postman for each screenshot required by your assignment:

#### 📸 Endpoint 1: Add a New Student
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/students`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```
- **Expected Status**: `201 Created`
- **Expected Response**:
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```
- *Capture Screenshot*: Ensure the request URL, request body, `201 Created` status badge, and response body are visible.

---

#### 📸 Endpoint 2: Retrieve All Students
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students`
- **Expected Status**: `200 OK`
- **Expected Response**:
  ```json
  [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "course": "Computer Science",
      "age": 21
    }
  ]
  ```
- *Capture Screenshot*: Show the list containing student records with `200 OK`.

---

#### 📸 Endpoint 3: Retrieve Student by ID
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students/1`
- **Expected Status**: `200 OK`
- **Expected Response**:
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "age": 21
  }
  ```
- *Capture Screenshot*: Show the single student object returned for ID `1`.

---

#### 📸 Endpoint 4: Update Student Details
- **Method**: `PUT`
- **URL**: `http://localhost:8080/api/students/1`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
  ```json
  {
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "course": "Computer Science",
    "age": 22
  }
  ```
- **Expected Status**: `200 OK`
- **Expected Response**:
  ```json
  {
    "id": 1,
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "course": "Computer Science",
    "age": 22
  }
  ```
- *Capture Screenshot*: Show updated details (name, email, age) with `200 OK`.

---

#### 📸 Endpoint 5: Retrieve Students by Course
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students/course/Computer Science`
- **Expected Status**: `200 OK`
- **Expected Response**:
  ```json
  [
    {
      "id": 1,
      "name": "John Doe Updated",
      "email": "john.updated@example.com",
      "course": "Computer Science",
      "age": 22
    }
  ]
  ```
- *Capture Screenshot*: Show the filtered array of students belonging to "Computer Science".

---

#### 📸 Endpoint 6: Delete a Student
- **Method**: `DELETE`
- **URL**: `http://localhost:8080/api/students/1`
- **Expected Status**: `200 OK`
- **Expected Response**:
  ```json
  {
    "message": "Student successfully deleted with id: 1",
    "status": 200
  }
  ```
- *Capture Screenshot*: Show the successful deletion message with `200 OK`.

---

#### 📸 Bonus / Exception Handling: Student Not Found (404)
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students/999`
- **Expected Status**: `404 Not Found`
- **Expected Response**:
  ```json
  {
    "timestamp": "2026-11-14T10:45:00.123456",
    "status": 404,
    "error": "Not Found",
    "message": "Student not found with id: '999'",
    "path": "/api/students/999"
  }
  ```
- *Capture Screenshot*: Demonstrates the custom exception handling for student not found.

---

#### 📸 Bonus / Exception Handling: Validation Error (400)
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/students`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "name": "",
    "email": "invalid-email",
    "course": "",
    "age": 0
  }
  ```
- **Expected Status**: `400 Bad Request`
- **Expected Response**: Shows field-by-field validation error messages.

---

## 🐙 GitHub Repository Submission Guide

To publish your project to GitHub and obtain the repository link:

### 1. Initialize Git and Commit
```bash
git init
git add .
git commit -m "feat: complete Student Management System REST API with Spring Boot, JPA, MySQL, and Postman collection"
```

### 2. Create a Repository on GitHub
1. Go to [GitHub](https://github.com/new).
2. Set the repository name (e.g., `Student-Management-System` or `24BCS12012_JAVA`).
3. Set visibility to **Public**.
4. Leave "Initialize this repository with README" **unchecked** (we already have a complete README).
5. Click **Create repository**.

### 3. Push Local Code to GitHub
Copy and paste the following commands (replace `<username>` and `<repo-name>` with yours):

```bash
git branch -M main
git remote add origin https://github.com/<username>/<repo-name>.git
git push -u origin main
```

Your GitHub repository will now contain the complete, well-documented source code ready for submission!

