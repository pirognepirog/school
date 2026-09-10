package ru.hogwarts.school.service;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;


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
    ///  конструктор
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
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
        return facultyRepository.findAll().stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .collect(Collectors.toList());
    }
/* старый код для работы с HashMap
    ///  Получение Faculty из библиотеки
    public Faculty findFaculty(long id) {
        return faculties.get(id); // возврат значения из HashMap по id
    }

    /// Редактирование Faculty
    public Faculty editFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            throw new IllegalArgumentException("Faculty with id " + faculty.getId() + " not found");
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    /// Удаление Faculty из карты
    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    /// Возврат коллекции Student
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    /// Метод фильтрации Faculty по цвету через stream API
    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .collect(Collectors.toList());
    }
 */
}
