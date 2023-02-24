package com.rom4ik.dao;

import com.rom4ik.model.Book;
import com.rom4ik.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rom4ik
 */
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAll() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person getById(int id) {
        Person person = jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
        if(person != null) {
            List<Book> books = jdbcTemplate.query("SELECT * FROM book WHERE owner_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
            person.setBooks(books);
        }
        return person;
    }

    public void create(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, surname, birthday_year) VALUES(?, ?, ?)",
                person.getName(),
                person.getSurname(),
                person.getBirthdayYear());
    }

    public void update(Person person, int id) {
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, birthday_year=? WHERE id=?",
                person.getName(),
                person.getSurname(),
                person.getBirthdayYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }
}
