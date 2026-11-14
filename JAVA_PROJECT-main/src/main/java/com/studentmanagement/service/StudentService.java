package com.studentmanagement.service;

import com.studentmanagement.entity.Student;

import java.util.List;

public interface StudentService {

    /**
     * Add a new student record
     * @param student student entity to create
     * @return created student with generated ID
     */
    Student createStudent(Student student);

    /**
     * Retrieve all student records
     * @return list of all students
     */
    List<Student> getAllStudents();

    /**
     * Retrieve a student by ID
     * @param id student identifier
     * @return student entity if found
     */
    Student getStudentById(Long id);

    /**
     * Update details of an existing student
     * @param id student identifier
     * @param studentDetails updated student details
     * @return updated student entity
     */
    Student updateStudent(Long id, Student studentDetails);

    /**
     * Delete a student by ID
     * @param id student identifier
     */
    void deleteStudent(Long id);

    /**
     * Retrieve students enrolled in a specific course
     * @param course course name
     * @return list of students enrolled in the course
     */
    List<Student> getStudentsByCourse(String course);
}

