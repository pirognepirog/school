-- liquibase formatted sql

-- changeset sDoroshkov:1
CREATE TABLE users (
    id    SERIAL PRIMARY KEY,
    email TEXT
);

-- changeset sDoroshkov:2
CREATE INDEX student_name_index ON student (name);

-- changeset sDoroshkov:3
CREATE INDEX faculty_name_color_index ON faculty (name, color);