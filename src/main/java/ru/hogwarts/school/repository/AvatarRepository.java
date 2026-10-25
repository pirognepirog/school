package ru.hogwarts.school.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    // метод для поиска Avatar по Id студента
    Optional<Avatar> findByStudentId(Long studentId);

    // метод для пагинации Avatar
    Page<Avatar> findAll(Pageable pageable);

}
