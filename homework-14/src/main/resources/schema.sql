CREATE TABLE IF NOT EXISTS authors (
    author_id   SERIAL,
    firstname   VARCHAR(255) NOT NULL,
    patronymic  VARCHAR(255),
    lastname    VARCHAR(255) NOT NULL,
    PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id    SERIAL,
    name        VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS books (
    book_id     SERIAL,
    name        VARCHAR(255) NOT NULL,
    genre_id    BIGINT NOT NULL,
    author_id   BIGINT NOT NULL,
    PRIMARY KEY (book_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
);
