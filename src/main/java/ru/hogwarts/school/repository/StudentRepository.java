package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    //Collection<Student> findAllByContainsIgnoreCase(String name);

    Collection<Student> findByAgeBetween(long min, long max);

    ///  ШАГ 3 ЗАДАНИЯ, так потому что не Ultimate версия:
    @Query("SELECT s FROM Student s WHERE s.age BETWEEN :min AND :max")
    Collection<Student> findByAgeBetween(@Param("min") int min, @Param("max") int max);

    /// Получить всех студентов, но отобразить только список их имен
    @Query("SELECT s.name FROM Student s")
    Collection<String> getAllStudentsNames();

    /// Получить всех студентов, у которых в имени присутствует буква О (или любая другая).
    @Query("SELECT s.name FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :letter, '%'))")
    Collection<String> getAllStudentsLikeNames(@Param("letter") String letter);

    /// Получить всех студентов, у которых возраст меньше идентификатора.
    @Query("SELECT s.name, s.age FROM Student s WHERE s.age < :age")
    Collection<Object[]> getStudentNamesAndAgesLessThan(@Param("age") int age);

    /// Получить всех студентов упорядоченных по возрасту.
    @Query("SELECT s.name, s.age FROM Student s ORDER BY s.age")
    Collection<Object[]> getStudentSortingByAge();

    /// метод поиска по факультету
    Collection<Student> findByFacultyId(Long facultyId);
}
