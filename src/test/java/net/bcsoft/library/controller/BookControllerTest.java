package net.bcsoft.library.controller;

import net.bcsoft.library.dto.BookDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.BookService;
import net.bcsoft.library.service.implementation.BookServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String BOOK_SERIAL_CODE = "ABC123";
    private static final String BOOK_SERIAL_CODE2 = "DEF456";

    private static final String BOOK_AUTHOR = "Author 1";
    private static final String BOOK_AUTHOR2 = "Author 2";
    private static final String BOOK_TITLE = "Title 1";
    private static final String BOOK_TITLE2 = "Title 2";

    private static final Logger log = LogManager.getLogger(BookServiceImpl.class);
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController(bookService, bookMapper);
    }

    @Test
    void createBook() {
        Book book = new Book(1L, BOOK_TITLE, BOOK_AUTHOR,BOOK_SERIAL_CODE, 5, null);
        BookDTO bookDTO = new BookDTO(BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5);
        when(bookMapper.toEntity(bookDTO)).thenReturn(book);

        ResponseEntity<BookDTO> response = bookController.createBook(bookDTO);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
    }

    @Test
    void getAllBooks() {
        Book book1 = new Book(1L,BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        Book book2 = new Book(2L, BOOK_TITLE2, BOOK_AUTHOR2, BOOK_SERIAL_CODE2, 3, null);
        List<Book> books = Arrays.asList(book1, book2);

        BookDTO bookDTO1 = new BookDTO(BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5);
        BookDTO bookDTO2 = new BookDTO(BOOK_TITLE2, BOOK_AUTHOR, BOOK_SERIAL_CODE2, 3);
        List<BookDTO> bookDTOs = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.readAllBooks()).thenReturn(books);
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTOs, response.getBody());

    }

    @Test
    void getBookById() {
        Long id = 1L;
        Book book = new Book(id, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        BookDTO bookDTO = new BookDTO(BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5);
        when(bookService.readBookById(id)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(id);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
        verify(bookService, times(1)).readBookById(id);
    }


    @Test
    void getBookBySerialCode() {
        String serialCode = "SC1";
        Book book = new Book(1L, BOOK_TITLE, BOOK_AUTHOR, serialCode, 5, null);
        BookDTO bookDTO = new BookDTO(BOOK_TITLE, BOOK_AUTHOR, serialCode, 5);
        when(bookService.readBookBySerialCode(serialCode)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookBySerialCode(serialCode);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
    }

    @Test
    void getAllBooksByAuthor() {
        String author = "Alessandro Sorrentino";
        // given
        Book book1 = new Book(1L, BOOK_TITLE, author, BOOK_SERIAL_CODE, 5, null);
        Book book2 = new Book(2L, BOOK_TITLE2, author, BOOK_SERIAL_CODE2, 3, null);
        List<Book> books = Arrays.asList(book1, book2);

        BookDTO bookDTO1 = new BookDTO(BOOK_TITLE, author, BOOK_SERIAL_CODE, 5);
        BookDTO bookDTO2 = new BookDTO(BOOK_TITLE2, author, BOOK_SERIAL_CODE2, 3);
        List<BookDTO> bookDTOs = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.readBooksByAuthor(author)).thenReturn(books);
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        ResponseEntity<List<BookDTO>> response = bookController.getBooksByAuthor(author);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTOs, response.getBody());
    }

    @Test
    void updateBook() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO( "Old Title", "Old Author", "SC1", 10);
        Book updatedBook = new Book(bookId, BOOK_TITLE, BOOK_AUTHOR, BOOK_SERIAL_CODE, 5, null);
        when(bookMapper.toEntity(bookDTO)).thenReturn(updatedBook);

        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, bookDTO);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
    }

    @Test
    void deleteBook() {
        Long bookId = 1L;
        ResponseEntity<Void> response = bookController.deleteBook(bookId);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

