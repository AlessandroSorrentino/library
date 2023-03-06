package net.bcsoft.library.controller;

import net.bcsoft.library.dto.BookDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookController bookController;

    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookDTO = new BookDTO("title", "author", "123", 5);
        book = new Book(1L, "title", "author", "123", 5, new ArrayList<>());
    }

    @Test
    void createBook() {
        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        when(bookService.saveBook(book)).thenReturn(book);

        ResponseEntity<BookDTO> response = bookController.createBook(bookDTO);

        verify(bookMapper, times(1)).toEntity(bookDTO);
        verify(bookService, times(1)).saveBook(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(bookDTO);
    }

    @Test
    void getAllBooks() {
        List<Book> books = Collections.singletonList(book);
        when(bookService.readAllBooks()).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        verify(bookService, times(1)).readAllBooks();
        verify(bookMapper, times(1)).toDTO(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(bookDTO);
    }

    @Test
    void getBookById() {
        Long bookId = 1L;
        when(bookService.readBookById(bookId)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        verify(bookService, times(1)).readBookById(bookId);
        verify(bookMapper, times(1)).toDTO(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bookDTO);
    }

    @Test
    void getBookBySerialCode() {
        String serialCode = "123456789";
        when(bookService.readBookBySerialCode(serialCode)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookBySerialCode(serialCode);

        verify(bookService, times(1)).readBookBySerialCode(serialCode);
        verify(bookMapper, times(1)).toDTO(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bookDTO);
    }

    @Test
    void getBookByAuthor() {
        String author = "author";
        List<Book> books = Collections.singletonList(book);
        when(bookService.readBooksByAuthor(author)).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<List<BookDTO>> response = bookController.getBooksByAuthor(author);

        verify(bookService, times(1)).readBooksByAuthor(author);
        verify(bookMapper, times(1)).toDTO(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(bookDTO);
    }

    @Test
    void updateBook() {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO("new title", "new author", "123", 5);
        Book updatedBook = new Book(bookId, "new title", "new author", "123", 5, new ArrayList<>());

        when(bookMapper.toEntity(updatedBookDTO)).thenReturn(updatedBook);
        when(bookService.updateBook(updatedBook)).thenReturn(updatedBook);

        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, updatedBookDTO);

        verify(bookMapper, times(1)).toEntity(updatedBookDTO);
        verify(bookService, times(1)).updateBook(updatedBook);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedBookDTO);
    }

    @Test
    void deleteBook() {
        Long bookId = 1L;

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        verify(bookService, times(1)).deleteBook(bookId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}