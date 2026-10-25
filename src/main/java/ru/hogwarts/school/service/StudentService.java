package ru.hogwarts.school.service;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {
/*  ДЛЯ РАБОТЫ С БД ЭТО НЕ НУЖНО, РАБОТА ДОЛЖНА ОСУЩЕСТВЛЯТЬСЯ ЧЕРЕЗ РЕПОЗИТОРИИ
    ПРОСЛОЙКА МЕЖДУ КЛАССОМ И БАЗОЙ ДАННЫХ
    private final HashMap<Long, Student> students = new HashMap<>();
    private long lastId = 0; // идентификатор по умолчанию
 */
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    /// ИНЖЕКТИМ РЕПОЗИТОРИЙ
    private final StudentRepository studentRepository;
    ///  конструктор
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    /// Создание нового Student
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name: {}, age: {}", student.getName(), student.getAge());
        return studentRepository.save(student);
    }

    ///  Получение Student из библиотеки
    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by id = {}", id);

        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("There is not student with id = {}", id);
        } else {
            logger.debug("Found student: {}", student.getName());
        }
        return student;
    }

    /// Редактирование Student
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");

        if (student == null) {
            logger.error("Cannot edit student: student is null");
            throw new IllegalArgumentException("Student cannot be null");
        }

        logger.debug("Saving student: id = {}, name = {}, age = {}",
                student.getId(), student.getName(), student.getAge());

        Student updated = studentRepository.save(student);
        logger.debug("Student updated successfully: id = {}", updated.getId());
        return updated;
    }

    /// Удаление Student из карты
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by id = {}", id);
        logger.warn("Deleting student with id = {}", id);
        studentRepository.deleteById(id);
    }

    /// Возврат коллекции Student
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for getAllStudents");

        Collection<Student> students = studentRepository.findAll();

        if(students.isEmpty()) {
            logger.warn("No students found in the database");
        } else {
            logger.debug("Found {} students", students.size());
        }
        return students;
    }
/* исправлен далее, так как правильно фильтровать через БД
    /// Метод фильтрации студентов по возрасту через stream API
    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for getStudentsByAge");

        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
 */
public Collection<Student> getStudentsByAge(int age) {
    logger.info("Was invoked method for get students by age = {}", age);

    Collection<Student> students = studentRepository.findByAge(age);

    if (students.isEmpty()) {
        logger.warn("No students found with age = {}", age);
    } else {
        logger.debug("Found {} students with age = {}", students.size(), age);
    }

    return students;
}


    /// ШАГ 3 по заданию
    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between min = {}, max = {}", min, max);

        Collection<Student> students = studentRepository.findByAgeBetween(min, max);

        if (students.isEmpty()) {
            logger.warn("No students found with age between min = {}, max = {}", min, max);
        } else {
            logger.debug("Found {} students with age between min = {}, max = {}", students.size(), min, max);
        }

        return students;
    }
    /// Получить всех студентов, но отобразить только список их имен
    public Collection<String> getAllStudentsNames() {
        logger.info("Was invoked method for get all student names");

        Collection<String> students = studentRepository.getAllStudentsNames();
        if (students.isEmpty()) {
            logger.warn("No students found");
        } else {
            logger.debug("Found {} student names", students.size());
        }
        return students;
    }

    /// Получить всех студентов, у которых в имени присутствует буква О (или любая другая).
    public Collection<String> getAllStudentsLikeNames(String letter) {
        logger.info("Was invoked method for get all students like names = {}",letter);

        Collection<String> students = studentRepository.getAllStudentsLikeNames(letter);
        if (students.isEmpty()) {
            logger.warn("No students found");
        } else {
            logger.debug("Found {} student names with letter = {}", students.size(), letter);
        }
        return students;
    }

    /// Получить всех студентов, у которых возраст меньше идентификатора.
    public Collection<Object[]> getStudentNamesAndAgesLessThan(int age) {
        logger.info("Was invoked method for get student names and ages less than age = {}", age);

        Collection<Object[]> students = studentRepository.getStudentNamesAndAgesLessThan(age);
        if (students.isEmpty()) {
            logger.warn("No students found with age < {}", age);
        } else {
            logger.debug("Found {} students with age < {}", students.size(), age);
        }
        return students;
    }

    /// Получить всех студентов упорядоченных по возрасту.
    public Collection<Object[]> getStudentSortingByAge() {
        logger.info("Was invoked method for get student sorting by age");
        Collection<Object[]> students = studentRepository.getStudentSortingByAge();
        if (students.isEmpty()) {
            logger.warn("No students found to sort by age");
        } else {
            logger.debug("Found {} students", students.size());
        }
        return students;
    }

    ///  получить студентов на факультете
    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get students by faculty id = {}", facultyId);
        Collection<Student> students = studentRepository.findByFacultyId(facultyId);
        if (students.isEmpty()) {
            logger.warn("No students found by faculty id = {}", facultyId);
        } else {
            logger.debug("Found {} students by faculty id = {}", students.size(), facultyId);
        }
        return students;
    }

    ///  получить количество студентов
    public long getCountStudent() {
        logger.info("Was invoked method for get count of students");
        long count = studentRepository.getCountStudent();
        if (count == 0) {
            logger.warn("No students found");
        } else {
            logger.debug("Found {} students", count);
        }
        return count;
    }

    /// Получить средний возраст студентов
    public double getAvgAgeStudent() {
        logger.info("Was invoked method for get average age of students");
        Double avg = studentRepository.getAvgAgeStudent();
        if (avg == null) {
            logger.warn("No students found, average age is not available");
            return 0.0;
        }
        if (avg == 0.0) {
            logger.warn("Average age is 0.0");
        } else {
            logger.debug("Average age of students = {}", avg);
        }
        return avg;
    }

    ///  получить только пять последних студентов
    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        List<Student> students = studentRepository.getLastFiveStudents(PageRequest.of(0, 5));
        if (students.isEmpty()) {
            logger.warn("No students found to get last five");
        } else {
            logger.debug("Last students found: {}", students.size());
        }
        return students;
    }


}
