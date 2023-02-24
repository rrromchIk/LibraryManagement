package com.rom4ik.dao;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author rom4ik
 */
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Book> getAll() {
        //return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("FROM Book").getResultList();
    }

    public Book getById(int id) {
        Book book = jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);

        if(book != null) {
            Person person = jdbcTemplate.query("SELECT * FROM book JOIN person on book.owner_id = person.id WHERE book.id=?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
            book.setOwner(person);
        }

        return book;
    }

    public void create(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, publication_year, owner_id) VALUES(?, ?, ?, ?)",
                book.getName(),
                book.getAuthor(),
                book.getPublicationYear(),
                null);
    }

    public void update(Book book, int id) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, publication_year=? WHERE id=?",
                book.getName(),
                book.getAuthor(),
                book.getPublicationYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET owner_id = null WHERE id=?", id);
    }

    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE book SET owner_id = ? WHERE id=?", person.getId(), id);
    }
}
