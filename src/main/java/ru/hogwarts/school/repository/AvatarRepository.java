package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    // метод для поиска Avatar по Id студента
    Optional<Avatar> findByStudentId(Long studentId);

}
