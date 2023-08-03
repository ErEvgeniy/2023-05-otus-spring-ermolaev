CREATE TABLE IF NOT EXISTS AUTHOR (
    AUTHOR_ID   BIGINT AUTO_INCREMENT,
    FIRSTNAME   VARCHAR2(255),
    PATRONYMIC  VARCHAR2(255),
    LASTNAME    VARCHAR2(255),
    PRIMARY KEY (AUTHOR_ID)
);

CREATE TABLE IF NOT EXISTS GENRE (
    GENRE_ID    BIGINT AUTO_INCREMENT,
    NAME        VARCHAR2(255),
    PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS BOOK (
    BOOK_ID     BIGINT AUTO_INCREMENT,
    NAME        VARCHAR2(255),
    GENRE_ID    BIGINT NOT NULL,
    AUTHOR_ID   BIGINT NOT NULL,
    PRIMARY KEY (BOOK_ID),
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE(GENRE_ID),
    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR(AUTHOR_ID)
);