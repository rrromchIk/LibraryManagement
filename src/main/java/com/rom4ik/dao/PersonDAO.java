package com.rom4ik.dao;

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
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Person> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select b FROM Person b", Person.class).getResultList();
    }

    @Transactional
    public Person getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void create(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        Session session = sessionFactory.getCurrentSession();
        Person dbPerson = session.get(Person.class, id);
        dbPerson.setName(updatedPerson.getName());
        dbPerson.setSurname(updatedPerson.getSurname());
        dbPerson.setBirthdayYear(updatedPerson.getBirthdayYear());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        session.delete(person);
    }
}
