package net.bcsoft.library.service.implementation;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //  per attivare l'integrazione tra JUnit e Mockito. In particolare, questa annotazione viene utilizzata per abilitare l'uso di annotazioni come @Mock
class LoanServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final Long BOOK_ID = 1L;

    private LoanServiceImpl loanService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanService = new LoanServiceImpl(userRepository, bookRepository);
    }


    @Test
    void testAddLoan() {
        User user = new User(USER_ID, "John", "Doe", "johndoe@example.com", new ArrayList<>());
        Book book = new Book(BOOK_ID, "The Great Gatsby", "Scott Fitzgerald", "ABC123", 5, new ArrayList<>());

        // Configure the mock objects to return the expected results
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(userRepository.save(user)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        // Test adding a new loan
        loanService.addLoan(USER_ID, BOOK_ID);
        assertEquals(1, user.getBooks().size());
        assertEquals(1, book.getUsers().size());
        assertEquals(5 - 1, book.getQuantity());

        // Test adding a loan where the book is not available
        book.setQuantity(0);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        assertThrows(BadRequestException.class, () -> loanService.addLoan(USER_ID, BOOK_ID));

        // Test adding a loan where the user already has 3 books
        book.setQuantity(5);
        user.getBooks().add(new Book(2L, "Another Book", "Another Author", "DEF456", 5, null));
        user.getBooks().add(new Book(3L, "Yet Another Book", "Yet Another Author", "GHI789", 5, null));
        user.getBooks().add(new Book(4L, "And Another Book", "And Another Author", "JKL101", 5, null));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> loanService.addLoan(USER_ID, BOOK_ID));


        // Test adding a loan for a non-existent user
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> loanService.addLoan(USER_ID, BOOK_ID));

        // Test adding a loan for a non-existent book
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> loanService.addLoan(USER_ID, BOOK_ID));
    }

    @Test
    void testRemoveLoan() {
        User user = new User(USER_ID, "John", "Doe", "johndoe@example.com", new ArrayList<>());
        Book book = new Book(BOOK_ID, "The Great Gatsby", "Scott Fitzgerald", "ABC123", 5, new ArrayList<>());
        user.getBooks().add(book);
        book.getUsers().add(user);

        // Configure the mock objects to return the expected results
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(userRepository.save(user)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        // Remove the loan
        loanService.removeLoan(USER_ID, BOOK_ID);

        // Verify that the book was removed from the user's list of books and the user was removed from the book's list of users
        assertEquals(0, user.getBooks().size());
        assertEquals(0, book.getUsers().size());
        assertEquals(6, book.getQuantity());

        // Test removing a loan from a non-existent user
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> loanService.removeLoan(1L, 1L));

        // Test removing a loan from a non-existent book
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> loanService.removeLoan(1L, 1L));

    }

    @Test
    void testReadAllLoans() {
        User user = new User(USER_ID, "John", "Doe", "johndoe@example.com", new ArrayList<>());
        Book book = new Book(BOOK_ID, "The Great Gatsby", "Scott Fitzgerald", "ABC123", 5, new ArrayList<>());
        book.getUsers().add(user);
        user.getBooks().add(book);

        // Create a list of loaned books containing the book we created above
        List<Book> loanedBooks = new ArrayList<>();
        loanedBooks.add(book);

        // Mock the book repository to return the list of loaned books
        when(bookRepository.findByUsersIsNotNull()).thenReturn(loanedBooks);

        // Call the readAllLoans() method and verify that it returns the expected list of loaned books
        List<Book> returnedLoanedBooks = loanService.readAllLoans();
        assertEquals(returnedLoanedBooks, loanedBooks);
    }


}
