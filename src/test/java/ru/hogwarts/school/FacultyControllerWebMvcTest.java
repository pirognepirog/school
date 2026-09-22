package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    /// GET /faculties/{id}
    @Test
    void getFacultyInfo() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    /// GET /faculties
    @Test
    void getAllFaculties() throws Exception {
        Faculty f1 = new Faculty();
        f1.setId(1L);
        f1.setName("Gryffindor");
        f1.setColor("Red");

        Faculty f2 = new Faculty();
        f2.setId(2L);
        f2.setName("Slytherin");
        f2.setColor("Green");

        when(facultyService.getAllFaculties()).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));
    }

    /// GET /faculties/filter?color=...
    @Test
    void getAllFacultiesFilteredByColor() throws Exception {
        Faculty f = new Faculty();
        f.setId(1L);
        f.setName("Gryffindor");
        f.setColor("Red");

        when(facultyService.getFacultiesByColor("Red")).thenReturn(List.of(f));

        mockMvc.perform(get("/faculties/filter").param("color", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    /// GET /faculties/search?nameOrColor=...
    @Test
    void findFaculties() throws Exception {
        Faculty f = new Faculty();
        f.setId(1L);
        f.setName("Gryffindor");
        f.setColor("Red");

        when(facultyService.findByNameOrColor("Gryff")).thenReturn(List.of(f));

        mockMvc.perform(get("/faculties/search").param("nameOrColor", "Gryff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    /// POST /faculties
    @Test
    void createFaculty() throws Exception {
        Faculty input = new Faculty();
        input.setName("Slytherin");
        input.setColor("Green");

        Faculty saved = new Faculty();
        saved.setId(10L);
        saved.setName("Slytherin");
        saved.setColor("Green");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    /// PUT /faculties/{id}
    @Test
    void editFaculty() throws Exception {
        Faculty input = new Faculty();
        input.setId(1L);
        input.setName("Gryffindor Updated");
        input.setColor("Red");

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(input);

        mockMvc.perform(put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"));
    }

    /// DELETE /faculties/{id}
    @Test
    void deleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isOk());
    }

    /// GET /faculties/by-student/{studentId}
    @Test
    void getFacultyByStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.getFacultyByStudentId(5L)).thenReturn(faculty);

        mockMvc.perform(get("/faculties/by-student/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }
}