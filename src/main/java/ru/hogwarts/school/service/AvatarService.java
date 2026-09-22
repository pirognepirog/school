package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;

public interface AvatarService {

    // метод загрузки аватара в систему
    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    // метод поиска аватара по id студента
    Avatar findAvatar(Long studentId);
}
