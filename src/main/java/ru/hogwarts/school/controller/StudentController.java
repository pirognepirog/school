package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@RestController // помечаем что это контроллер
@RequestMapping("/students") // установка базового URL
public class StudentController {

    // инжектим класс StudentService
    // @Autowired - лишнее, так как есть конструктор
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /// Метод для отображения всех Student
    /// полный URL:
    //Get: http://localhost:8080/students/23
    @GetMapping("{id}") // добавление идентификатора
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        // переменная
        Student student = studentService.findStudent(id);
        // проверяем есть ли Student
        if (student == null) {
            // return 404
            return ResponseEntity.notFound().build();//  с помощью .build() собираем искомый объект
        }
        // возврашаем сам Student ResponseEntity - хранит всю нформацию об ответе (запрос, тело, код и т.п.)
        return ResponseEntity.ok(student);
    }

    /// Метод для отображения всех Student в коллекции
    /// полный URL:
    //Get: http://localhost:8080/students
    @GetMapping
    public  ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /// Метод для фильтрации всех Student в коллекции по возрасту
    @GetMapping("/filter")
    public ResponseEntity<Collection<Student>> getAllStudents(@RequestParam(required = false) Integer age) {
        if (age != null) {
            return ResponseEntity.ok(studentService.getStudentsByAge(age));
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /// Метод для создания новых Student
    /// полный URL:
    // POST: http://localhost:8080/students
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    /// Метод для редактирования Student
    /// полный URL:
    //Put: http://localhost:8080/students
    @PutMapping("/{id}")
    public ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student) {
                                                 // @RequestBody - сможет сохранить значение
        if (!id.equals(student.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Student updated = studentService.editStudent(student);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    /// Метод для удаления Student
    /// полный URL:
    // DELETE: http://localhost:8080/students/23
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    ///  ШАГ 3 по заданию
    @GetMapping("/age-between")
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(
            @RequestParam int min,
            @RequestParam int max) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    /// Получить всех студентов, но отобразить только список их имен
    @GetMapping("/names")
    public ResponseEntity<Collection<String>> getAllStudentsNames() {
        return ResponseEntity.ok(studentService.getAllStudentsNames());
    }

    /// Получить всех студентов, но отобразить только список их имен
    @GetMapping("/like-names")
    public ResponseEntity<Collection<String>> getAllStudentsLikeNames(@RequestParam String letter) {

        return ResponseEntity.ok(studentService.getAllStudentsLikeNames(letter));
    }

    /// Получить всех студентов, у которых возраст меньше идентификатора.
    @GetMapping("/names-and-ages")
    public ResponseEntity<Collection<Object[]>> getStudentNamesAndAgesLessThan(@RequestParam int age) {

        return ResponseEntity.ok(studentService.getStudentNamesAndAgesLessThan(age));
    }

    /// Получить всех студентов упорядоченных по возрасту.
    @GetMapping("/names-and-ages-order-by")
    public ResponseEntity<Collection<Object[]>> getStudentSortingByAge() {
        return ResponseEntity.ok(studentService.getStudentSortingByAge());
    }

    /// Получить студентов факультета
    @GetMapping("/by-faculty/{facultyId}")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(studentService.getStudentsByFacultyId(facultyId));
    }

    /// Получить количество студентов.
    @GetMapping("/count")
    public ResponseEntity<Long> getCountStudent() {
        return ResponseEntity.ok(studentService.getCountStudent());
    }

    /// Получить средний возраст студентов.
    @GetMapping("/avg-age")
    public ResponseEntity<Double> getAvgAgeStudent() {
        return ResponseEntity.ok(studentService.getAvgAgeStudent());
    }

    /// получить только пять последних студентов
    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }
    // задание урока 4.5 параллельные стримы
    @GetMapping("/names/starting-with-a")
    public ResponseEntity<List<String>> getStudentsNamesStartingWithA() {
        return ResponseEntity.ok(studentService.getStudentsNamesStartingWithA());
    }

    @GetMapping("/sum")
    public ResponseEntity<Long> calculateSum() {
        // Оптимизация: используем IntStream вместо Stream.iterate,
        // это избавляет от автоупаковки объектов и работает в разы быстрее.
        long sum = IntStream.rangeClosed(1, 1_000_000).sum();

        return ResponseEntity.ok(sum);
    }

    // задание урока 4.6 потоки

    @GetMapping("/print-parallel")
    public ResponseEntity<Void> printParallel() {
        // Получаем всех студентов. Преобразуем в List для удобного доступа по индексу.
        List<Student> students = new ArrayList<>(studentService.getAllStudents());

        // Защита на случай, если студентов меньше 6
        if (students.size() < 6) {
            return ResponseEntity.badRequest().build();
        }

        // 1. Первые два имени в основном потоке
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // 2. Третий и четвертый в первом параллельном потоке
        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        // 3. Пятый и шестой во втором параллельном потоке
        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        // Запускаем потоки
        thread1.start();
        thread2.start();

        return ResponseEntity.ok().build();
    }

    private synchronized void printNameSynchronized(String name) {
        System.out.println(name);
        // Небольшая пауза для наглядности блокировки, а то не ясно, есть она или нет
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // эндпоинт
    @GetMapping("/print-synchronized")
    public ResponseEntity<Void> printSynchronized() {
        List<Student> students = new ArrayList<>(studentService.getAllStudents());

        if (students.size() < 6) {
            return ResponseEntity.badRequest().build();
        }

        // 1. Первые два имени в основном потоке
        printNameSynchronized(students.get(0).getName());
        printNameSynchronized(students.get(1).getName());

        // 2. Третий и четвертый в параллельном потоке
        Thread thread1 = new Thread(() -> {
            printNameSynchronized(students.get(2).getName());
            printNameSynchronized(students.get(3).getName());
        });

        // 3. Пятый и шестой в параллельном потоке
        Thread thread2 = new Thread(() -> {
            printNameSynchronized(students.get(4).getName());
            printNameSynchronized(students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        return ResponseEntity.ok().build();
    }

}
