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
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/students";
    }

    private Student student;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        // Создаём факультет — он нужен для теста /by-faculty/{id}
        faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        faculty = facultyRepository.save(faculty);

        // Создаём студента
        student = new Student();
        student.setName("Harry Potter");
        student.setAge(11);
        student.setFaculty(faculty);
        student = studentRepository.save(student);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    /// GET /students/{id}
    @Test
    void getStudentInfo() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/" + student.getId(), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    /// GET /students
    @Test
    void getAllStudents() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /students/filter?age=...
    @Test
    void getAllStudentsFilteredByAge() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl() + "/filter?age=11",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// POST /students
    @Test
    void createStudent() {
        Student newStudent = new Student();
        newStudent.setName("Ron Weasley");
        newStudent.setAge(11);
        newStudent.setFaculty(faculty);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl(), newStudent, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ron Weasley");
    }

    /// PUT /students/{id}
    @Test
    void editStudent() {
        student.setName("Harry J. Potter");

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl() + "/" + student.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry J. Potter");
    }

    /// DELETE /students/{id}
    @Test
    void deleteStudent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + student.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    /// GET /students/age-between?min=&max=
    @Test
    void getStudentsByAgeBetween() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl() + "/age-between?min=10&max=12",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /students/names
    @Test
    void getAllStudentsNames() {
        ResponseEntity<Collection<String>> response = restTemplate.exchange(
                baseUrl() + "/names",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Harry Potter");
    }

    /// GET /students/like-names?letter=...
    @Test
    void getAllStudentsLikeNames() {
        ResponseEntity<Collection<String>> response = restTemplate.exchange(
                baseUrl() + "/like-names?letter=H",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Harry Potter");
    }

    /// GET /students/names-and-ages?age=...
    @Test
    void getStudentNamesAndAgesLessThan() {
        ResponseEntity<Collection<Object[]>> response = restTemplate.exchange(
                baseUrl() + "/names-and-ages?age=12",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /students/names-and-ages-order-by
    @Test
    void getStudentSortingByAge() {
        ResponseEntity<Collection<Object[]>> response = restTemplate.exchange(
                baseUrl() + "/names-and-ages-order-by",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    /// GET /students/by-faculty/{facultyId}
    @Test
    void getStudentsByFaculty() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl() + "/by-faculty/" + faculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}