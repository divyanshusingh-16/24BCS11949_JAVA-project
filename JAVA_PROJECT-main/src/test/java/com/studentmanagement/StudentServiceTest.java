package com.studentmanagement;

import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "John Doe", "john.doe@example.com", "Computer Science", 21);
    }

    @Test
    @DisplayName("Create student should save and return student")
    void testCreateStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student created = studentService.createStudent(new Student("John Doe", "john.doe@example.com", "Computer Science", 21));

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Get all students should return list of students")
    void testGetAllStudents() {
        Student student2 = new Student(2L, "Jane Smith", "jane@example.com", "Data Science", 22);
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student, student2));

        List<Student> list = studentService.getAllStudents();

        assertThat(list).hasSize(2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get student by ID - Success")
    void testGetStudentByIdSuccess() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student found = studentService.getStudentById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Get student by ID - Throws ResourceNotFoundException")
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: '99'");
    }

    @Test
    @DisplayName("Update student - Success")
    void testUpdateStudentSuccess() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student updateInfo = new Student("John Updated", "john.up@example.com", "AI", 22);
        Student updated = studentService.updateStudent(1L, updateInfo);

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("John Updated");
        assertThat(updated.getCourse()).isEqualTo("AI");
    }

    @Test
    @DisplayName("Delete student - Success")
    void testDeleteStudentSuccess() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    @DisplayName("Delete student - Not Found")
    void testDeleteStudentNotFound() {
        when(studentRepository.findById(88L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(88L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(studentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get students by course")
    void testGetStudentsByCourse() {
        when(studentRepository.findByCourseIgnoreCase("Computer Science")).thenReturn(List.of(student));

        List<Student> result = studentService.getStudentsByCourse("Computer Science");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourse()).isEqualTo("Computer Science");
    }
}

