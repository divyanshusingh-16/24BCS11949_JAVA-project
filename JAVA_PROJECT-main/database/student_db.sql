-- ====================================================================
-- Database: student_db
-- Student Management System Database Schema & Initial Mock Data
-- ====================================================================

-- 1. Create database if not exists
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- 2. Drop table if exists
DROP TABLE IF EXISTS students;

-- 3. Create students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    course VARCHAR(255) NOT NULL,
    age INT NOT NULL
);

-- 4. Insert initial sample records for testing
INSERT INTO students (name, email, course, age) VALUES
('John Doe', 'john.doe@example.com', 'Computer Science', 21),
('Jane Smith', 'jane.smith@example.com', 'Information Technology', 22),
('Alex Turner', 'alex.turner@example.com', 'Computer Science', 20),
('Emily Davis', 'emily.davis@example.com', 'Electrical Engineering', 23),
('Michael Brown', 'michael.brown@example.com', 'Mechanical Engineering', 22);

-- 5. Verify records
SELECT * FROM students;

