package com.example.BookStore.dao;

import com.example.BookStore.provider.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookDAO {

    @Autowired
    private JdbcTemplate template;

    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, publication_year, genre, isbn, price, pages, description, rating, new_flag, stock, image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        template.update(sql, book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear(),
                book.getGenre(), book.getIsbn(), book.getPrice(), book.getPages(), book.getDescription(),
                book.getRating(), book.isNewFlag(), book.getStock(), book.getImagePath());
    }

    public List<Book> getAllBooks() {
        String sql = "select * from BOOKS";
        return template.query(sql, new BookRowMapper());
    }

    public Optional<Book> getBook(long id) {
        String sql = "select * from BOOKS where ID = ?";
        List<Book> books = template.query(sql, new BookRowMapper(), id);
        if (books.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(books.get(0));
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublisher(rs.getString("publisher"));
            book.setYear(rs.getInt("publication_year"));
            book.setGenre(rs.getString("genre"));
            book.setIsbn(rs.getString("isbn"));
            book.setPrice(rs.getDouble("price"));
            book.setPages(rs.getInt("pages"));
            book.setDescription(rs.getString("description"));
            book.setRating(rs.getInt("rating"));
            book.setNewFlag(rs.getBoolean("new_flag"));
            book.setStock(rs.getInt("stock"));
            book.setImagePath(rs.getString("image_path"));
            return book;
        }
    }
}
