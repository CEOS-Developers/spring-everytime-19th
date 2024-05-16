-- schema.sql
CREATE DATABASE IF NOT EXISTS `everytime` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE everytime;

CREATE TABLE IF NOT EXISTS users
(
    user_id bigint NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    is_deleted TINYINT(1) NOT NULL default 0,
    school_id bigint NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS school
(
    school_id bigint NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (school_id)
);

CREATE TABLE IF NOT EXISTS board
(
    board_id bigint NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    school_id bigint NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (board_id)
);

CREATE TABLE IF NOT EXISTS post
(
    post_id bigint NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_anonymous TINYINT(1) NOT NULL default 0,
    like_number int NOT NULL default 0,
    user_id bigint NOT NULL,
    board_id bigint NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (post_id)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id bigint NOT NULL AUTO_INCREMENT,
    refresh_token VARCHAR(255) NOT NULL,
    username varchar(255) NOT NULL,
    expired_at varchar(255) NOT NULL,
    PRIMARY KEY (id)
);
