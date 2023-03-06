package net.bcsoft.library.repository;

import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest // ha lo scopo di semplificare il processo di testing, caricando solo le componenti necessarie per i test dell'accesso ai dati.
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void testSave() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        Book savedBook = bookRepository.save(book);

        Assertions.assertNotNull(savedBook.getId());
    }

    @Test
    void testFindById() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals(book.getSerialCode(), foundBook.get().getSerialCode());
    }

    @Test
    void testFindAll() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        bookRepository.save(book);

        List<Book> allBooks = bookRepository.findAll();

        Assertions.assertEquals(1, allBooks.size());
    }

    @Test
    void testUpdate() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        bookRepository.save(book);

        book.setSerialCode("new_serial_code");
        bookRepository.save(book);

        Optional<Book> updatedBook = bookRepository.findById(book.getId());

        Assertions.assertTrue(updatedBook.isPresent());
        Assertions.assertEquals("new_serial_code", updatedBook.get().getSerialCode());
    }

    @Test
    void testDelete() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        bookRepository.save(book);

        bookRepository.delete(book);

        Optional<Book> deletedBook = bookRepository.findById(book.getId());

        Assertions.assertFalse(deletedBook.isPresent());
    }

    @Test
    void testFindBySerialCodeIgnoreCase() {
        Book book = new Book(null, "title", "author", "serial_code", 5, new ArrayList<>());
        bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findBySerialCodeIgnoreCase("SERIAL_CODE");

        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals(book.getSerialCode(), foundBook.get().getSerialCode());
    }

    @Test
    void testFindByAuthorContainingIgnoreCase() {
        Book book1 = new Book(null, "title1", "author_1", "serial_code_1", 5, new ArrayList<>());
        Book book2 = new Book(null, "title2", "author_2", "serial_code_2", 5, new ArrayList<>());
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> foundBooks = bookRepository.findByAuthorContainingIgnoreCase("AUTHOR");

        Assertions.assertEquals(2, foundBooks.size());
    }

    @Test
    void testFindByUsersIsNotNull() {
        User user = new User(null, "username", "email@gmail.com", "password", new ArrayList<>());
        userRepository.save(user);
        Book book1 = new Book(null, "title1", "author_1", "serial_code_1", 5, new ArrayList<>(Collections.singletonList(user)));
        Book book2 = new Book(null, "title2", "author_2", "serial_code_2", 5, new ArrayList<>());
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> booksWithUsers = bookRepository.findByUsersIsNotNull();

        Assertions.assertEquals(1, booksWithUsers.size());
        Assertions.assertEquals(book1.getTitle(), booksWithUsers.get(0).getTitle());
    }

    @Test
    void testExistsBySerialCodeIgnoreCase() {
        Book book = new Book(null, "title", "author", "serial_code",5, new ArrayList<>());
        bookRepository.save(book);

        boolean exists = bookRepository.existsBySerialCodeIgnoreCase("sErIal_code");

        Assertions.assertTrue(exists);
    }
}










