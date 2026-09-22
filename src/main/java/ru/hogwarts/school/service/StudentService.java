package ru.hogwarts.school.service;


import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentService {
/*  ДЛЯ РАБОТЫ С БД ЭТО НЕ НУЖНО, РАБОТА ДОЛЖНА ОСУЩЕСТВЛЯТЬСЯ ЧЕРЕЗ РЕПОЗИТОРИИ
    ПРОСЛОЙКА МЕЖДУ КЛАССОМ И БАЗОЙ ДАННЫХ
    private final HashMap<Long, Student> students = new HashMap<>();
    private long lastId = 0; // идентификатор по умолчанию
 */

    /// ИНЖЕКТИМ РЕПОЗИТОРИЙ
    private final StudentRepository studentRepository;
    ///  конструктор
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    /// Создание нового Student
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    ///  Получение Student из библиотеки
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    /// Редактирование Student
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    /// Удаление Student из карты
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    /// Возврат коллекции Student
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /// Метод фильтрации студентов по возрасту через stream API
    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    /// ШАГ 3 по заданию
    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }
    /// Получить всех студентов, но отобразить только список их имен
    public Collection<String> getAllStudentsNames() {
        return studentRepository.getAllStudentsNames();
    }

    /// Получить всех студентов, у которых в имени присутствует буква О (или любая другая).
    public Collection<String> getAllStudentsLikeNames(String letter) {
        return studentRepository.getAllStudentsLikeNames(letter);
    }

    /// Получить всех студентов, у которых возраст меньше идентификатора.
    public Collection<Object[]> getStudentNamesAndAgesLessThan(int age) {
        return studentRepository.getStudentNamesAndAgesLessThan(age);
    }

    /// Получить всех студентов упорядоченных по возрасту.
    public Collection<Object[]> getStudentSortingByAge() {
        return studentRepository.getStudentSortingByAge();
    }

    ///  получить студентов на факультете
    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
}
