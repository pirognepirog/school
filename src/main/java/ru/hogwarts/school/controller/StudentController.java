package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController // помечаем что это контроллер
@RequestMapping("/students") // установка базового URL
public class StudentController {

    // инжектим класс  StudentService
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




}
