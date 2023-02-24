package com.rom4ik.dao;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select b FROM Book b", Book.class).getResultList();
    }

    @Transactional
    public Book getById(int id) {
        /*Book book = jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);

        if(book != null) {
            Person person = jdbcTemplate.query("SELECT * FROM book JOIN person on book.owner_id = person.id WHERE book.id=?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
            book.setOwner(person);
        }*/

        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void create(Book book) {
        /*jdbcTemplate.update("INSERT INTO book(name, author, publication_year, owner_id) VALUES(?, ?, ?, ?)",
                book.getName(),
                book.getAuthor(),
                book.getPublicationYear(),
                null);*/

        Session session = sessionFactory.getCurrentSession();
        session.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id) {
        /*jdbcTemplate.update("UPDATE book SET name=?, author=?, publication_year=? WHERE id=?",
                book.getName(),
                book.getAuthor(),
                book.getPublicationYear(),
                id);*/

        Session session = sessionFactory.getCurrentSession();
        Book dbBook = session.get(Book.class, id);
        dbBook.setName(updatedBook.getName());
        dbBook.setAuthor(updatedBook.getAuthor());
        dbBook.setPublicationYear(updatedBook.getPublicationYear());
    }

    @Transactional
    public void delete(int id) {
        //jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);

        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        session.delete(book);
    }

    @Transactional
    public void release(int id) {
        //jdbcTemplate.update("UPDATE book SET owner_id = null WHERE id=?", id);

        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);
        Person owner = book.getOwner();

        if(owner != null)
            owner.getBooks().remove(book);

        book.setOwner(null);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        //jdbcTemplate.update("UPDATE book SET owner_id = ? WHERE id=?", person.getId(), id);

        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);
        Person person = session.get(Person.class, selectedPerson.getId());

        book.setOwner(person);
        person.getBooks().add(book);
    }
}
