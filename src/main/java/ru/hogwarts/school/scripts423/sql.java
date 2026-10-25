package ru.hogwarts.school.scripts423;

/*
-- Все студенты с названиями факультетов
SELECT s.name AS student_name,
       s.age  AS student_age,
       f.name AS faculty_name
FROM student s
INNER JOIN faculty f ON f.id = s.id_faculty;


-- Только студенты, у которых есть аватарки
SELECT s.name      AS student_name,
       s.age       AS student_age,
       a.file_path AS avatar_path,
       a.file_size AS avatar_size,
       a.media_type AS avatar_type
FROM student s
INNER JOIN avatar a ON a.student_id = s.id;
 */