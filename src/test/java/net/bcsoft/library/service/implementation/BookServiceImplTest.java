package net.bcsoft.library.service.implementation;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //  per attivare l'integrazione tra JUnit e Mockito. In particolare, questa annotazione viene utilizzata per abilitare l'uso di annotazioni come @Mock
class BookServiceImplTest {

    private static final String TITLE = "The Great Gatsby";
    private static final String AUTHOR = "Scott Fitzgerald";
    private static final String SERIALCODE = "ABC123";

    private static final Long BOOK_ID = 1L;

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void testSaveBook() {
        Book book = new Book(BOOK_ID, TITLE, AUTHOR, SERIALCODE, 5, null);

        // Configure the mock objects to return the expected results
        when(bookRepository.save(book)).thenReturn(book);

        // Test adding a new book
        Book savedBook = bookService.saveBook(book);
        assertNotNull(savedBook);
        assertEquals(book, savedBook);

        // Test adding a book that doesn't exist
        assertThrows(BadRequestException.class, () -> bookService.saveBook(null));

        // Create a new book with a duplicate serial code
        Book duplicateBook = new Book(2L, TITLE, AUTHOR, SERIALCODE, 5, null);
        when(bookRepository.existsBySerialCodeIgnoreCase(duplicateBook.getSerialCode())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookService.saveBook(duplicateBook));
    }

    @Test
    void testReadAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, TITLE, AUTHOR, SERIALCODE, 5, null));
        bookList.add(new Book(2L, "To Kill a Mockingbird", "Harper Lee", "DEF456", 6, null));

        // Configure the mock objects to return the expected results
        when(bookRepository.findAll()).thenReturn(bookList);

        // Test retrieving all books
        List<Book> result = bookService.readAllBooks();
        assertNotNull(result);
        assertEquals(bookList, result);

        // Test retrieving all books when there are none
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> bookService.readAllBooks());
    }

    @Test
    void testReadBookById() {
        Book book = new Book(BOOK_ID, TITLE, AUTHOR, SERIALCODE, 5, null);

        // Configure the mock objects to return the expected results
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // Test retrieving a book by ID
        Book result = bookService.readBookById(BOOK_ID);
        assertNotNull(result);
        assertEquals(book, result);

        // Test retrieving a book with a null ID
        assertThrows(BadRequestException.class, () -> bookService.readBookById(null));

        // Test retrieving a book with a non-existing ID
        assertThrows(NotFoundException.class, () -> bookService.readBookById(2L));
    }

    @Test
    void testReadBookBySerialCode() {
        Book book = new Book(BOOK_ID, TITLE, AUTHOR, SERIALCODE, 5, null);

        // Configure the mock objects to return the expected results
        when(bookRepository.findBySerialCodeIgnoreCase(SERIALCODE)).thenReturn(Optional.of(book));

        // Test retrieving a book by serial code
        Book result = bookService.readBookBySerialCode(SERIALCODE);
        assertNotNull(result);
        assertEquals(book, result);

        // Test retrieving a book with a null serial code
        assertThrows(BadRequestException.class, () -> bookService.readBookBySerialCode(null));
        Assertions.assertThrows(BadRequestException.class, () -> bookService.readBookBySerialCode(""));

        // Test retrieving a book with a non-existing serial code
        String nonExistingSerialCode = "XYZ987";
        when(bookRepository.findBySerialCodeIgnoreCase(nonExistingSerialCode)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.readBookBySerialCode(nonExistingSerialCode));

    }

    @Test
    void testReadBooksByAuthor() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, TITLE, AUTHOR, SERIALCODE, 5, null));
        bookList.add(new Book(2L, "To Kill a Mockingbird", "Harper Lee", "DEF456", 6, null));

        // Configure the mock objects to return the expected results
        when(bookRepository.findByAuthorContainingIgnoreCase(AUTHOR)).thenReturn(bookList);

        // Test retrieving a book by author
        List<Book> result = bookService.readBooksByAuthor(AUTHOR);
        assertNotNull(result);
        assertEquals(bookList, result);

        // Test retrieving a book with a null or empty serial code
        assertThrows(BadRequestException.class, () -> bookService.readBooksByAuthor(null));
        assertThrows(BadRequestException.class, () -> bookService.readBooksByAuthor(""));

        // Test retrieving a book with a non-existing author
        String nonExistingAuthor = "William Shakespeare";
        Mockito.when(bookRepository.findByAuthorContainingIgnoreCase(nonExistingAuthor)).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> bookService.readBooksByAuthor(nonExistingAuthor));

        verify(bookRepository, times(2)).findByAuthorContainingIgnoreCase(Mockito.anyString());
    }


    @Test
    void testUpdateBook() {
        Book existingBook = new Book(BOOK_ID, TITLE, AUTHOR, SERIALCODE, 5, null);
        Book updatedBook = new Book(1L, "Updated title", "Updated book", "CBA321", 4, null);

        // Configure the mock objects to return the expected results
        when(bookRepository.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Test updating a book
        bookService.updateBook(updatedBook);
        assertEquals(updatedBook, existingBook);

        // Test updating a book that doesn't exist
        Book nonExistingBook = new Book(2L, "fake title", "fake author", "ERT357", 7, null);
        when(bookRepository.findById(nonExistingBook.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.updateBook(nonExistingBook));

        // Test updating a book with a serial code that already exists
        Book duplicateBook = new Book(2L, "DuplicateTitle", "Duplicate Auhtor", SERIALCODE, 5, null);
        when(bookRepository.findById(duplicateBook.getId())).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsBySerialCodeIgnoreCase(duplicateBook.getSerialCode())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookService.updateBook(duplicateBook));
    }

    @Test
    void testDeleteBook() {
        Book existingBook = new Book(BOOK_ID, TITLE, AUTHOR, SERIALCODE, 5, null);

        // Configure the mock objects to return the expected results
        when(bookRepository.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));

        // Test deleting a book
        bookService.deleteBook(existingBook.getId());
        assertThrows(BadRequestException.class, () -> bookService.deleteBook(null));

        // Test deleting a book that doesn't exist
        Book nonExistingBook = new Book(2L, "fake title", "fake author", "ERT357", 7, null);
        when(bookRepository.findById(nonExistingBook.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.deleteBook(nonExistingBook.getId()));
    }
}