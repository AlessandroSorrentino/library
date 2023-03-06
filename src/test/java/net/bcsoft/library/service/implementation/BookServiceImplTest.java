package net.bcsoft.library.service.implementation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.repository.BookRepository;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "Title", "Author", "123456", 10, null);

    }

    @Test
    void testSaveBook_success() {
        when(bookRepository.existsBySerialCodeIgnoreCase(book.getSerialCode())).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertEquals(book, savedBook);
        verify(bookRepository, times(1)).existsBySerialCodeIgnoreCase(book.getSerialCode());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBook_nullBook_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.saveBook(null));

        verify(bookRepository, never()).existsBySerialCodeIgnoreCase(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testSaveBook_existingBook_throwBadRequestException() {
        when(bookRepository.existsBySerialCodeIgnoreCase(book.getSerialCode())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> bookService.saveBook(book));

        verify(bookRepository, times(1)).existsBySerialCodeIgnoreCase(book.getSerialCode());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testReadAllBooks_success() {
        Book book2 = new Book();
        book2.setId(2L);

        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> retrievedBooks = bookService.readAllBooks();

        assertEquals(2, retrievedBooks.size());
        assertEquals(book, retrievedBooks.get(0));
        assertEquals(book2, retrievedBooks.get(1));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testReadAllBooks_emptyList_throwNotFoundException() {
        List<Book> books = new ArrayList<>();

        when(bookRepository.findAll()).thenReturn(books);

        assertThrows(NotFoundException.class, () -> bookService.readAllBooks());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testReadBookById_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.readBookById(1L);
        assertThat(result).isEqualTo(book);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testReadBookById_bookIdIsNull_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.readBookById(null));
        verify(bookRepository, times(0)).findById(any());
    }

    @Test
    void testReadBookById_bookNotFound_throwNotFoundException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.readBookById(2L));
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    void testReadBookBySerialCode_success() {
        when(bookRepository.findBySerialCodeIgnoreCase("123456")).thenReturn(Optional.of(book));

        Book result = bookService.readBookBySerialCode("123456");
        assertThat(result).isEqualTo(book);
        verify(bookRepository, times(1)).findBySerialCodeIgnoreCase("123456");
    }

    @Test
    void testReadBookBySerialCode_serialCodeIsNull_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.readBookBySerialCode(null));
        verify(bookRepository, times(0)).findBySerialCodeIgnoreCase(anyString());
    }

    @Test
    void testReadBookBySerialCode_bookNotFound__throwNotFoundException() {
        when(bookRepository.findBySerialCodeIgnoreCase("invalid-serial-code")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.readBookBySerialCode("invalid-serial-code"));
        verify(bookRepository, times(1)).findBySerialCodeIgnoreCase("invalid-serial-code");
    }

    @Test
    void testReadBooksByAuthor_success() {
        String author = "Test Author";
        Book book2 = new Book(2L, "456", "Test Book 2", author, 1, new ArrayList<>());
        List<Book> books = Arrays.asList(book, book2);

        when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(books);

        List<Book> retrievedBooks = bookService.readBooksByAuthor(author);
        assertNotNull(retrievedBooks);
        assertEquals(2, retrievedBooks.size());
        assertTrue(books.contains(book));
        assertTrue(books.contains(book2));
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase(author);
    }

    @Test
    void testReadBooksByAuthor_authorIsNull_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.readBooksByAuthor(null));

        verify(bookRepository, times(0)).findByAuthorContainingIgnoreCase(anyString());
    }

    @Test
    void testReadBooksByAuthor_booksNotFound_throwNotFoundException() {
        when(bookRepository.findByAuthorContainingIgnoreCase("Invalid Author")).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> bookService.readBooksByAuthor("Invalid Author"));

        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("Invalid Author");
    }

    @Test
    void testUpdateBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book updatedBook = bookService.updateBook(book);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    void testUpdateBook_serialAlreadyExists_throwBadRequestException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.existsBySerialCodeIgnoreCase("654321")).thenReturn(true);

        Book updatedBook = new Book(1L, "newTitle", "newAuthor", "654321", 5, null);

        assertThrows(BadRequestException.class, () -> bookService.updateBook(updatedBook));

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(updatedBook);
    }
    @Test
    void testUpdateBook_nullBook_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.updateBook(null));

        verify(bookRepository, times(0)).findById(any());
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    void testUpdateBook_bookNotFound_throwNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(book));

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    void testDeleteBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_bookNotFound_throwNotFoundException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.deleteBook(2L));

        verify(bookRepository, times(1)).findById(2L);
        verify(bookRepository, times(0)).deleteById(1L);
    }

    @Test
    void testDeleteBook_nullId_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> bookService.deleteBook(null));

        verify(bookRepository, times(0)).existsById(any());
        verify(bookRepository, times(0)).deleteById(any());
    }
}