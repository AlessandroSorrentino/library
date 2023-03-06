package net.bcsoft.library.service.implementation;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "User", "testuser@example.com", "password123", new ArrayList<>());
        book = new Book(1L, "title1", "author1", "123", 2, new ArrayList<>());
    }

    @Test
    void testAddLoan_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.save(user)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        loanService.addLoan(1L, 1L);

        assertTrue(user.getBooks().contains(book));
        assertTrue(book.getUsers().contains(user));
        assertEquals(1, book.getQuantity());
    }

    @Test
    void testAddLoan_userNotFound_throwNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.addLoan(1L, 1L));
    }

    @Test
    void testAddLoan_bookNotFound_throwNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.addLoan(1L, 1L));
    }

    @Test
    void testAddLoan_bookNotAvailable_throwBadRequestException() {
        book.setQuantity(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> loanService.addLoan(1L, 1L));
    }

    @Test
    void testAddLoan_userHasBorrowedThreeBooks_throwBadRequestException() {
        List<Book> books = new ArrayList<>();

        books.add(new Book(2L, "title2", "author2", "456", 2, new ArrayList<>()));
        books.add(new Book(3L, "title3", "author3", "789", 4, new ArrayList<>()));
        books.add(new Book(4L, "title4", "author4", "012", 3, new ArrayList<>()));
        user.setBooks(books);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> loanService.addLoan(1L, 1L));
    }

    @Test
    void testRemoveLoan_success() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        user.setBooks(books);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.save(user)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        loanService.removeLoan(1L, 1L);

        assertFalse(user.getBooks().contains(book));
        assertFalse(book.getUsers().contains(user));
        assertEquals(3, book.getQuantity());
    }

    @Test
    void testRemoveLoan_userNotFound_throwNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.removeLoan(1L, 1L));
    }

    @Test
    void testRemoveLoan_bookNotFound_throwNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.removeLoan(1L, 1L));
    }

    @Test
    void testRemoveLoan_userDidNotBorrowBook_throwBadRequestException() {
        List<Book> books = new ArrayList<>();
        user.setBooks(books);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> loanService.removeLoan(1L, 1L));
    }


    @Test
    void testReadAllLoans() {
        book.getUsers().add(user);
        user.getBooks().add(book);

        List<Book> loanedBooks = new ArrayList<>();
        loanedBooks.add(book);

        when(bookRepository.findByUsersIsNotNull()).thenReturn(loanedBooks);

        List<Book> returnedLoanedBooks = loanService.readAllLoans();
        assertEquals(returnedLoanedBooks, loanedBooks);
        verify(bookRepository, times(1)).findByUsersIsNotNull();
    }
}