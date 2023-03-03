package net.bcsoft.library.service.implementation;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private static final String BOOK_SERIAL_CODE = "ABC123";
    private static final String BOOK_SERIAL_CODE2 = "DEF456";

    private static final String BOOK_AUTHOR = "Author 1";
    private static final String BOOK_AUTHOR2 = "Author 2";
    private static final String BOOK_TITLE = "Title 1";
    private static final String BOOK_TITLE2 = "Title 2";

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void saveBook() {
        Book book = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertNotNull(savedBook);
        assertEquals(book, savedBook);
    }

    @Test
    void saveBook_NullBook() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> bookService.saveBook(null));
        assertEquals("Book cannot be null",   exception.getMessage());
    }

    @Test
    void saveBook_ExistingSerialCode() {
        // given
        Book existingBook = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);

        // when
        when(bookRepository.existsBySerialCodeIgnoreCase(existingBook.getSerialCode())).thenReturn(true);

        // then
        assertThrows(BadRequestException.class, () -> bookService.saveBook(existingBook));
    }

    @Test
    void readAllBooks() {
        Book book1 = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        Book book2 = new Book(2L, BOOK_TITLE2, BOOK_AUTHOR2, BOOK_SERIAL_CODE2, 5, null);

        List<Book> booksListRepository = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(booksListRepository);

        List<Book> booksListService = bookService.readAllBooks();

        assertThat(booksListService).isNotEmpty()
                .hasSize(booksListRepository.size())
                .containsExactlyElementsOf(booksListRepository);
    }

    @Test
    void readAllBooks_NotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.readAllBooks());
        assertEquals("No books found",   exception.getMessage());
    }

    @Test
    void readBookById() {
        Long id = 1L;
        Book book = new Book(id, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        given(bookRepository.findById(id)).willReturn(Optional.of(book));

        Book foundBook = bookService.readBookById(id);
        assertNotNull(foundBook);
        assertEquals(book, foundBook);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.readBookById(2L));
        assertEquals("Book not found with id: " + 2L, exception.getMessage());
    }

    @Test
    void readBookBySerialCode() {
        Book book = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        when(bookRepository.findBySerialCodeIgnoreCase(BOOK_SERIAL_CODE)).thenReturn(Optional.of(book));

        Book foundBook = bookService.readBookBySerialCode(BOOK_SERIAL_CODE);
        assertEquals(book, foundBook);

        assertThrows(BadRequestException.class, () -> bookService.readBookBySerialCode(null));
        assertThrows(BadRequestException.class, () -> bookService.readBookBySerialCode(""));
        assertThrows(NotFoundException.class, () -> bookService.readBookBySerialCode("BOOK_SERIAL_CODE2"));
    }

    @Test
    void readBooksByAuthor() {
        Book book1 = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        Book book2 = new Book(1L, BOOK_TITLE2, BOOK_AUTHOR, BOOK_SERIAL_CODE2, 5, null);
        when(bookRepository.findByAuthorContainingIgnoreCase(BOOK_AUTHOR)).thenReturn(Arrays.asList(book1, book2));

        List<Book> foundBooks = bookService.readBooksByAuthor(BOOK_AUTHOR);
        assertEquals(2, foundBooks.size());
        assertTrue(foundBooks.contains(book1));
        assertTrue(foundBooks.contains(book2));

        assertThrows(BadRequestException.class, () -> bookService.readBooksByAuthor(null));
        assertThrows(BadRequestException.class, () -> bookService.readBooksByAuthor(""));
        assertThrows(NotFoundException.class, () -> bookService.readBooksByAuthor("Author C"));
    }
}