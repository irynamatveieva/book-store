package com.example.bookstore.repository.impl;

import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can`t save book:" + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can`t get book by id:" + id);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Book> query = session.getCriteriaBuilder()
                    .createQuery(Book.class);
            query.from(Book.class);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can`t get all books", e);
        }
    }
}
