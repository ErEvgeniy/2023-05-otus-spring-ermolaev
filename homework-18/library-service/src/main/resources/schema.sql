CREATE TABLE IF NOT EXISTS AUTHORS (
    AUTHOR_ID   BIGINT AUTO_INCREMENT,
    FIRSTNAME   VARCHAR2(255) NOT NULL,
    PATRONYMIC  VARCHAR2(255),
    LASTNAME    VARCHAR2(255) NOT NULL,
    PRIMARY KEY (AUTHOR_ID)
);

CREATE TABLE IF NOT EXISTS GENRES (
    GENRE_ID    BIGINT AUTO_INCREMENT,
    NAME        VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS BOOKS (
    BOOK_ID     BIGINT AUTO_INCREMENT,
    NAME        VARCHAR2(255) NOT NULL,
    GENRE_ID    BIGINT NOT NULL,
    AUTHOR_ID   BIGINT NOT NULL,
    PRIMARY KEY (BOOK_ID),
    FOREIGN KEY (GENRE_ID) REFERENCES GENRES(GENRE_ID),
    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS(AUTHOR_ID)
);

CREATE TABLE IF NOT EXISTS COMMENTS (
    COMMENT_ID  BIGINT AUTO_INCREMENT,
    TEXT        VARCHAR2(255) NOT NULL,
    BOOK_ID     BIGINT,
    PRIMARY KEY (COMMENT_ID),
    FOREIGN KEY (BOOK_ID) REFERENCES BOOKS(BOOK_ID)
);

CREATE TABLE IF NOT EXISTS USERS (
    USER_ID  BIGINT AUTO_INCREMENT,
    USERNAME VARCHAR2(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR2(500) NOT NULL,
    ENABLED BOOLEAN NOT NULL,
    PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS ROLES (
    ROLE_ID  BIGINT AUTO_INCREMENT,
    NAME VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ROLE_ID)
);

CREATE TABLE IF NOT EXISTS USER_ROLES (
    USER_ID  BIGINT NOT NULL,
    ROLE_ID  BIGINT NOT NULL
);