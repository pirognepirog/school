package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    /// ИНЖЕКТИМ РЕПОЗИТОРИЙ
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    ///  конструктор
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    /// Создание нового Faculty
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");

        if (faculty == null) {
            logger.error("Cannot create faculty: faculty is null");
            return null;
        }

        logger.debug("Creating faculty: name = {}, color = {}", faculty.getName(), faculty.getColor());
        Faculty created = facultyRepository.save(faculty);
        logger.debug("Faculty created successfully: id = {}", created.getId());
        return created;
    }

    /// Получение Faculty по id
    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty by id = {}", id);

        Faculty faculty = facultyRepository.findById(id).orElse(null);

        if (faculty == null) {
            logger.error("There is not faculty with id = {}", id);
        } else {
            logger.debug("Found faculty: name = {}, color = {}", faculty.getName(), faculty.getColor());
        }

        return faculty;
    }

    /// Редактирование Faculty
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");

        if (faculty == null) {
            logger.error("Cannot edit faculty: faculty is null");
            return null;
        }

        logger.debug("Editing faculty: id = {}, name = {}, color = {}",
                faculty.getId(), faculty.getName(), faculty.getColor());
        Faculty updated = facultyRepository.save(faculty);
        logger.debug("Faculty updated successfully: id = {}", updated.getId());
        return updated;
    }

    /// Удаление Faculty по id
    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty by id = {}", id);
        logger.warn("Deleting faculty with id = {}", id);
        facultyRepository.deleteById(id);
        logger.debug("Faculty with id = {} deleted", id);
    }

    /// Возврат коллекции Faculty
    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");

        Collection<Faculty> faculties = facultyRepository.findAll();

        if (faculties.isEmpty()) {
            logger.warn("No faculties found in the database");
        } else {
            logger.debug("Found {} faculties", faculties.size());
        }

        return faculties;
    }

    /// Метод фильтрации Faculty по цвету
    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculties by color = {}", color);

        Collection<Faculty> faculties = facultyRepository.getFacultiesByColor(color);

        if (faculties.isEmpty()) {
            logger.warn("No faculties found with color = {}", color);
        } else {
            logger.debug("Found {} faculties with color = {}", faculties.size(), color);
        }

        return faculties;
    }

    /// Метод фильтрации Faculty по имени или цвету без учета регистра
    public Collection<Faculty> findByNameOrColor(String value) {
        logger.info("Was invoked method for find faculties by name or color = {}", value);

        Collection<Faculty> faculties = facultyRepository
                .findByNameContainsIgnoreCaseOrColorContainsIgnoreCase(value, value);

        if (faculties.isEmpty()) {
            logger.warn("No faculties found with name or color = {}", value);
        } else {
            logger.debug("Found {} faculties with name or color = {}", faculties.size(), value);
        }

        return faculties;
    }

    /// Получить факультет студента
    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for get faculty by student id = {}", studentId);

        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            logger.error("There is not student with id = {}", studentId);
            return null;
        }

        Faculty faculty = student.getFaculty();

        if (faculty == null) {
            logger.warn("Student with id = {} has no faculty", studentId);
        } else {
            logger.debug("Found faculty for student id = {}: name = {}", studentId, faculty.getName());
        }

        return faculty;
    }

 }