package ru.hogwarts.school.service;


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

/* старый код
    /// Создание нового Student
    public Student createStudent(Student student) {
        // указываем последний идентификатор
        student.setId(++lastId); // увеличиваем на 1
        students.put(lastId, student); // ложим Student в HashMap
        return student; // возвращаем Student из метода для того, чтобы можно было использовать в другом месте
    }

    ///  Получение Student из библиотеки
    public Student findStudent(long id) {
        return students.get(id); // возврат значения из HashMap по id
    }

    /// Редактирование Student
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with id " + student.getId() + " not found");
        }
        students.put(student.getId(), student);
        return student;
    }

    /// Удаление Student из карты
    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    /// Возврат коллекции Student
    public Collection<Student> getAllStudents() {
        return students.values();
    }

    /// Метод фильтрации студентов по возрасту через stream API
    public Collection<Student> getStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

 */
}
