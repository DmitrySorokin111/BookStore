DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    publication_year INT NOT NULL,
    genre VARCHAR(255),
    isbn VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    pages INT NOT NULL,
    description TEXT,
    rating INT,
    new_flag BOOLEAN,
    stock INT NOT NULL,
    image_path VARCHAR(255),
    author_id BIGINT,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES authors(id)
);