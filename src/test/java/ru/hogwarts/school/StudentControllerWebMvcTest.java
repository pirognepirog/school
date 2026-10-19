package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    /// GET /students/{id}
    @Test
    void getStudentInfo() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(11);

        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(11));
    }

    /// GET /students
    @Test
    void getAllStudents() throws Exception {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("Harry");
        s1.setAge(11);

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("Ron");
        s2.setAge(11);

        when(studentService.getAllStudents()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[1].name").value("Ron"));
    }

    /// GET /students/filter?age=...
    @Test
    void getAllStudentsFilteredByAge() throws Exception {
        Student s = new Student();
        s.setId(1L);
        s.setName("Harry");
        s.setAge(11);

        when(studentService.getStudentsByAge(11)).thenReturn(List.of(s));

        mockMvc.perform(get("/students/filter").param("age", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].age").value(11));
    }

    /// POST /students
    @Test
    void createStudent() throws Exception {
        Student input = new Student();
        input.setName("Ron");
        input.setAge(11);

        Student saved = new Student();
        saved.setId(10L);
        saved.setName("Ron");
        saved.setAge(11);

        when(studentService.createStudent(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Ron"));
    }

    /// PUT /students/{id}
    @Test
    void editStudent() throws Exception {
        Student input = new Student();
        input.setId(1L);
        input.setName("Harry Updated");
        input.setAge(12);

        when(studentService.editStudent(any(Student.class))).thenReturn(input);

        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Updated"));
    }

    /// DELETE /students/{id}
    @Test
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk());
    }

    /// GET /students/age-between?min=&max=
    @Test
    void getStudentsByAgeBetween() throws Exception {
        Student s = new Student();
        s.setId(1L);
        s.setName("Harry");
        s.setAge(11);

        when(studentService.getStudentsByAgeBetween(10, 12)).thenReturn(List.of(s));

        mockMvc.perform(get("/students/age-between")
                        .param("min", "10")
                        .param("max", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].age").value(11));
    }

    /// GET /students/names
    @Test
    void getAllStudentsNames() throws Exception {
        when(studentService.getAllStudentsNames())
                .thenReturn(List.of("Harry", "Ron"));

        mockMvc.perform(get("/students/names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Harry"))
                .andExpect(jsonPath("$[1]").value("Ron"));
    }

    /// GET /students/like-names?letter=...
    @Test
    void getAllStudentsLikeNames() throws Exception {
        when(studentService.getAllStudentsLikeNames("H"))
                .thenReturn(List.of("Harry"));

        mockMvc.perform(get("/students/like-names").param("letter", "H"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value("Harry"));
    }

    /// GET /students/names-and-ages?age=...
    @Test
    void getStudentNamesAndAgesLessThan() throws Exception {
        when(studentService.getStudentNamesAndAgesLessThan(12))
                .thenReturn(List.<Object[]>of(new Object[]{"Harry", 11}));

        mockMvc.perform(get("/students/names-and-ages").param("age", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    /// GET /students/names-and-ages-order-by
    @Test
    void getStudentSortingByAge() throws Exception {
        when(studentService.getStudentSortingByAge())
                .thenReturn(List.<Object[]>of(new Object[]{"Harry", 11}));

        mockMvc.perform(get("/students/names-and-ages-order-by"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    /// GET /students/by-faculty/{facultyId}
    @Test
    void getStudentsByFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(5L);
        faculty.setName("Gryffindor");

        Student s = new Student();
        s.setId(1L);
        s.setName("Harry");
        s.setAge(11);
        s.setFaculty(faculty);

        when(studentService.getStudentsByFacultyId(5L)).thenReturn(List.of(s));

        mockMvc.perform(get("/students/by-faculty/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry"));
    }
}