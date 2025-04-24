DROP DATABASE IF EXISTS login_study;
DROP USER IF EXISTS 'login_study_dev'@'localhost';
CREATE DATABASE login_study CHAR SET utf8;
CREATE USER 'login_study_dev'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT,INSERT,UPDATE,DELETE ON login_study.* TO 'login_study_dev'@'localhost';
USE login_study;
CREATE TABLE users(
    id VARCHAR(255) PRIMARY KEY,
    pw VARCHAR(255) ,
    picture VARCHAR(255) ,
    oauth ENUM('GOOGLE','KAKAO', 'NAVER', 'GITHUB'),
    role ENUM('GUEST','USER','MANAGER','ADMIN'),

    name VARCHAR(255) NOT NULL
);
INSERT INTO users(id,pw,role,name)
VALUES ('user1','$2a$10$HDA7deEKi9SlqcFSJriojOBQMDCNYDdwd0U87qRXhc5f6aQ5oveoS','USER','사용자'),
       ('guest1','$2a$10$HDA7deEKi9SlqcFSJriojOBQMDCNYDdwd0U87qRXhc5f6aQ5oveoS','GUEST','게스트'),
       ('manager1','$2a$10$HDA7deEKi9SlqcFSJriojOBQMDCNYDdwd0U87qRXhc5f6aQ5oveoS','MANAGER','매니저'),
       ('admin1','$2a$10$HDA7deEKi9SlqcFSJriojOBQMDCNYDdwd0U87qRXhc5f6aQ5oveoS','ADMIN','관리자');