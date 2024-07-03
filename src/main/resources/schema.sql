CREATE TABLE books (
    id IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,
    genre VARCHAR(255),
    isbn VARCHAR(20) NOT NULL,
    price DOUBLE NOT NULL,
    pages INT NOT NULL,
    description CLOB,
    rating DOUBLE,
    new_flag BOOLEAN,
    stock INT NOT NULL,
    image_path VARCHAR(255)
);