package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarServiceImpl implements AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar for student id = {}", studentId);

        if (avatarFile == null || avatarFile.isEmpty()) {
            logger.warn("Avatar file is null or empty for student id = {}", studentId);
            throw new IllegalArgumentException("Avatar file cannot be empty");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = {}", studentId);
                    return new RuntimeException("Студент не найден");
                });

        logger.debug("Uploading avatar for student: name = {}", student.getName());

        String extension = getExtensions(avatarFile.getOriginalFilename());
        Path filePath = Path.of(avatarsDir, student.getId() + "." + extension);

        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (InputStream is = avatarFile.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
                bis.transferTo(bos);
            }
            logger.debug("Avatar file saved to path = {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving avatar file for student id = {}: {}", studentId, e.getMessage());
            throw e;
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());

        try {
            avatar.setData(generateDataForDB(filePath));
        } catch (IOException e) {
            logger.error("Error generating preview data for student id = {}: {}", studentId, e.getMessage());
            throw e;
        }

        avatarRepository.save(avatar);
        logger.debug("Avatar for student id = {} successfully saved in database", studentId);
    }

    @Override
    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar by student id = {}", studentId);

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(null);
        if (avatar == null) {
            logger.warn("No avatar found for student id = {}", studentId);
            return new Avatar();
        }

        logger.debug("Found avatar for student id = {}", studentId);
        return avatar;
    }

    public Page<Avatar> getAllAvatars(Pageable pageable) {
        logger.info("Was invoked method for get all avatars");

        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        if (avatars.isEmpty()) {
            logger.warn("No avatars found in the database");
        } else {
            logger.debug("Found {} avatars on page {}", avatars.getNumberOfElements(), pageable.getPageNumber());
        }

        return avatars;
    }

    private byte[] generateDataForDB(Path filePath) throws IOException {
        logger.debug("Generating preview data for file = {}", filePath);

        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                logger.error("Failed to read image from file = {}", filePath);
                throw new IOException("Не удалось прочитать изображение");
            }

            int width = Math.min(image.getWidth(), 100);
            int height = image.getHeight() * width / image.getWidth();

            BufferedImage preview = new BufferedImage(width, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();

            String extension = getExtensions(filePath.getFileName().toString());
            ImageIO.write(preview, extension, baos);

            logger.debug("Preview generated successfully: width = {}, height = {}", width, height);
            return baos.toByteArray();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}