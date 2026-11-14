package com.studentmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanagement.entity.Student;
import com.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/students - Add a new student successfully")
    void testCreateStudentSuccess() throws Exception {
        Student student = new Student("Alice Johnson", "alice.johnson@example.com", "Computer Science", 21);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Alice Johnson")))
                .andExpect(jsonPath("$.email", is("alice.johnson@example.com")))
                .andExpect(jsonPath("$.course", is("Computer Science")))
                .andExpect(jsonPath("$.age", is(21)));
    }

    @Test
    @DisplayName("POST /api/students - Validation error when required fields are missing")
    void testCreateStudentValidationError() throws Exception {
        Student invalidStudent = new Student("", "invalid-email", "", -5);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/students - Retrieve all students")
    void testGetAllStudents() throws Exception {
        studentRepository.save(new Student("Bob Smith", "bob.smith@example.com", "Information Technology", 22));
        studentRepository.save(new Student("Charlie Brown", "charlie.b@example.com", "Computer Science", 20));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Bob Smith")))
                .andExpect(jsonPath("$[1].name", is("Charlie Brown")));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Retrieve student by valid ID")
    void testGetStudentByIdSuccess() throws Exception {
        Student saved = studentRepository.save(
                new Student("David Miller", "david.m@example.com", "Electrical Engineering", 23)
        );

        mockMvc.perform(get("/api/students/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.name", is("David Miller")))
                .andExpect(jsonPath("$.email", is("david.m@example.com")))
                .andExpect(jsonPath("$.course", is("Electrical Engineering")))
                .andExpect(jsonPath("$.age", is(23)));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Not found when ID does not exist")
    void testGetStudentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: '9999'")));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Update student details successfully")
    void testUpdateStudentSuccess() throws Exception {
        Student saved = studentRepository.save(
                new Student("Emma Watson", "emma.w@example.com", "Civil Engineering", 20)
        );

        Student updatedDetails = new Student("Emma Watson", "emma.new@example.com", "Data Science", 21);

        mockMvc.perform(put("/api/students/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.email", is("emma.new@example.com")))
                .andExpect(jsonPath("$.course", is("Data Science")))
                .andExpect(jsonPath("$.age", is(21)));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Update student not found")
    void testUpdateStudentNotFound() throws Exception {
        Student updatedDetails = new Student("Ghost User", "ghost@example.com", "Physics", 25);

        mockMvc.perform(put("/api/students/{id}", 8888L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: '8888'")));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Delete student successfully")
    void testDeleteStudentSuccess() throws Exception {
        Student saved = studentRepository.save(
                new Student("Frank Castle", "frank@example.com", "Mechanical Engineering", 24)
        );

        mockMvc.perform(delete("/api/students/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("Student successfully deleted with id: " + saved.getId())));

        mockMvc.perform(get("/api/students/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Delete student not found")
    void testDeleteStudentNotFound() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 7777L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found with id: '7777'")));
    }

    @Test
    @DisplayName("GET /api/students/course/{course} - Retrieve students by course")
    void testGetStudentsByCourse() throws Exception {
        studentRepository.save(new Student("Grace Hopper", "grace@example.com", "Computer Science", 22));
        studentRepository.save(new Student("Alan Turing", "alan@example.com", "Computer Science", 23));
        studentRepository.save(new Student("Marie Curie", "marie@example.com", "Chemistry", 24));

        mockMvc.perform(get("/api/students/course/{course}", "Computer Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].course", is("Computer Science")))
                .andExpect(jsonPath("$[1].course", is("Computer Science")));
    }
}

