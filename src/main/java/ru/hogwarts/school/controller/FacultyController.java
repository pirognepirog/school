package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController // помечаем что это контроллер
@RequestMapping("/faculties") // установка базового URL
public class FacultyController {

    // инжектим класс  FacultyService
    // @Autowired - лишнее, так как есть конструктор
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /// Метод для отображения всех Faculty
    /// полный URL:
    //Get: http://localhost:8080/faculties/23
    @GetMapping("{id}") // добавление идентификатора
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        // переменная
        Faculty faculty = facultyService.findFaculty(id);
        // проверяем есть ли Faculty
        if (faculty == null) {
            // return 404
            return ResponseEntity.notFound().build();//  с помощью .build() собираем искомый объект
        }
        // возврашаем сам Faculty ResponseEntity - хранит всю нформацию об ответе (запрос, тело, код и т.п.)
        return ResponseEntity.ok(faculty);
    }

    /// Метод для отображения всех Faculty в коллекции
    /// полный URL:
    //Get: http://localhost:8080/faculties
    @GetMapping
    public  ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    /// Метод для фильтрации всех Faculty в коллекции по цвету
    @GetMapping("/filter")
    public ResponseEntity<Collection<Faculty>> getAllFaculties(@RequestParam(required = false) String color) {
        if (color != null) {
            return ResponseEntity.ok(facultyService.getFacultiesByColor(color));
        }
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    /// Метод для создания новых Faculty
    /// полный URL:
    // POST: http://localhost:8080/faculties
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    /// Метод для редактирования Faculty
    /// полный URL:
    //Put: http://localhost:8080/faculties
    @PutMapping("/{id}")
    public ResponseEntity<Faculty> editFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
                                                // @RequestBody - сможет сохранить значение
        if (!id.equals(faculty.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Faculty updated = facultyService.editFaculty(faculty);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    /// Метод для удаления Faculty
    /// полный URL:
    // DELETE: http://localhost:8080/faculties/23
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

}
