# Student Management System

A beginner-friendly full-stack Student Management System using:

- Java 17
- Java MVC architecture
- JDBC
- MySQL
- Node.js + Express
- HTML, CSS, JavaScript
- Maven
- Apache Tomcat 10+

## Architecture

Browser
  -> Node.js frontend server (port 3000)
  -> Java MVC REST API (port 8080)
  -> DAO layer
  -> JDBC
  -> MySQL

## Features

- Add student
- View all students
- Search students
- Update student
- Delete student
- MySQL persistence
- Responsive frontend
- Java MVC separation: Model / Controller / DAO / Service / DB
- REST-style API

## 1. Create the database

Open MySQL Workbench or XAMPP phpMyAdmin and run:

```sql
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Optional sample data:

```sql
INSERT INTO students (name, email, course, age, phone)
VALUES
('Anjali', 'anjali@example.com', 'Computer Science', 20, '9876543210'),
('Rahul', 'rahul@example.com', 'Artificial Intelligence', 21, '9876543211');
```

## 2. Configure Java database connection

Open:

`backend/src/main/resources/db.properties`

Change username/password to match your MySQL setup.

Example for XAMPP default MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC
db.username=root
db.password=
```

## 3. Build Java backend

Requirements:
- JDK 17+
- Maven
- Apache Tomcat 10+

From `backend`:

```bash
mvn clean package
```

This creates:

`backend/target/student-management-api.war`

Copy the WAR into Tomcat's `webapps` folder and start Tomcat.

The API will be available at:

`http://localhost:8080/student-management-api/api/students`

## 4. Start Node frontend

Requirements:
- Node.js 18+

From `frontend`:

```bash
npm install
npm start
```

Open:

`http://localhost:3000`

The frontend calls the Java API at:

`http://localhost:8080/student-management-api/api/students`

If your Tomcat port/context path is different, edit:

`frontend/public/js/app.js`

and change `API_URL`.

## API endpoints

GET    /api/students
GET    /api/students/{id}
POST   /api/students
PUT    /api/students/{id}
DELETE /api/students/{id}

Example POST body:

```json
{
  "name": "Thanuja",
  "email": "thanuja@example.com",
  "course": "Computer Science",
  "age": 20,
  "phone": "9876543210"
}
```

## GitHub

Create a repository, then:

```bash
git init
git add .
git commit -m "Initial Student Management System"
git branch -M main
git remote add origin YOUR_GITHUB_REPOSITORY_URL
git push -u origin main
```

Do not commit real database passwords. Use environment variables or a local `db.properties` file for production projects.
