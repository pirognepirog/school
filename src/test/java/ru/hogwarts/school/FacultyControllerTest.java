package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/faculties";
    }

    private Faculty faculty;
    private Student student;

    @BeforeEach
    void setUp() {
        // Создаём факультет
        faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        faculty = facultyRepository.save(faculty);

        // Создаём студента, привязанного к факультету — нужен для /by-student/{id}
        student = new Student();
        student.setName("Harry Potter");
        student.setAge(11);
        student.setFaculty(faculty);
        student = studentRepository.save(student);
    }

    @AfterEach
    void tearDown() {
        // Сначала удаляем студентов (из-за внешнего ключа на faculty)
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    /// GET /faculties/{id}
    @Test
    void getFacultyInfo() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl() + "/" + faculty.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }

    /// GET /faculties
    @Test
    void getAllFaculties() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /faculties/filter?color=...
    @Test
    void getAllFacultiesFilteredByColor() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl() + "/filter?color=Red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /faculties/search?nameOrColor=...
    @Test
    void findFaculties() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl() + "/search?nameOrColor=Gryff",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// POST /faculties
    @Test
    void createFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Slytherin");
        newFaculty.setColor("Green");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl(), newFaculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    /// PUT /faculties/{id}
    @Test
    void editFaculty() {
        faculty.setName("Gryffindor Updated");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl() + "/" + faculty.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor Updated");
    }

    /// DELETE /faculties/{id}
    @Test
    void deleteFaculty() {
        // Сначала отвязываем студента — иначе FK не даст удалить факультет
        student.setFaculty(null);
        studentRepository.save(student);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRepository.findById(faculty.getId())).isEmpty();
    }

    /// GET /faculties/by-student/{studentId}
    @Test
    void getFacultyByStudent() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl() + "/by-student/" + student.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }
}