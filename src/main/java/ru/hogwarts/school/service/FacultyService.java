package ru.hogwarts.school.service;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacultyService {

/*   ДЛЯ РАБОТЫ С БД ЭТО НЕ НУЖНО, РАБОТА ДОЛЖНА ОСУЩЕСТВЛЯТЬСЯ ЧЕРЕЗ РЕПОЗИТОРИИ
    ПРОСЛОЙКА МЕЖДУ КЛАССОМ И БАЗОЙ ДАННЫХ
    private final HashMap<Long, Faculty> faculties   = new HashMap<>();
    private long lastId = 0; // идентификатор по умолчанию
*/
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
        return facultyRepository.save(faculty);
    }

    ///  Получение Faculty из библиотеки
    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    /// Редактирование Faculty
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    /// Удаление Faculty из карты
    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    /// Возврат коллекции Student
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    /// Метод фильтрации Faculty по цвету через stream API
    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.getFacultiesByColor(color);
    }

    /// Метод фильтрации Faculty по имени или цвету без учета регистра
    public Collection<Faculty> findByNameOrColor(String value) {
        return facultyRepository.findByNameContainsIgnoreCaseOrColorContainsIgnoreCase(value, value);
    }

    /// Получить факультет студента
    public Faculty getFacultyByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return null;
        }
        return student.getFaculty();
    }
 }