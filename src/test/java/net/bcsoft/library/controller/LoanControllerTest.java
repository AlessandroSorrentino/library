package net.bcsoft.library.controller;

import net.bcsoft.library.dto.LoanDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.service.LoanService;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //  per attivare l'integrazione tra JUnit e Mockito. In particolare, questa annotazione viene utilizzata per abilitare l'uso di annotazioni come @Mock
class LoanControllerTest {

    private static final Logger log = LogManager.getLogger(BookServiceImpl.class);
    private LoanController loanController;

    @Mock
    private LoanService loanService;

    @Mock
    private BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        loanController = new LoanController(loanService, bookMapper);
    }

    @Test
    void createLoan() {
        Book book = new Book(1L, "bookTitle", "bookAuthor", "ABC123", 5, null);
        User user = new User(1L, "userName", "user@email.com", "userPassword", null);
        doNothing().when(loanService).addLoan(user.getId(), book.getId());

        ResponseEntity<Void> response = loanController.createLoan(user.getId(), book.getId());
        log.info(response.getStatusCode());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    void getAllLoans() {
        Book book1 = new Book(1L, "bookTitle1", "bookAuthor1", "ABC123", 5, new ArrayList<>());
        Book book2 = new Book(2L, "bookTitle2", "bookAuthor2", "DEF456", 2, new ArrayList<>());
        User user1 = new User(1L, "userName1", "userEmail1", "userPassword1", new ArrayList<>());
        User user2 = new User(2L, "userName2", "userEmail2", "userPassword2", new ArrayList<>());
        user1.getBooks().add(book1);
        user2.getBooks().add(book1);
        user2.getBooks().add(book2);
        book1.getUsers().add(user1);
        book1.getUsers().add(user2);
        book2.getUsers().add(user2);
        List<Book> loans = Arrays.asList(book1, book2);
        when(loanService.readAllLoans()).thenReturn(loans);
        List<LoanDTO> expectedLoanDTOs = loans.stream().map(bookMapper::toLoanDTO).collect(Collectors.toList());

        ResponseEntity<List<LoanDTO>> response = loanController.getAllLoans();
        log.info(response.getStatusCode() + "\n" + loans);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLoanDTOs, response.getBody());
    }

    @Test
    void testDeleteLoan() {
        Book book = new Book(1L, "bookTitle", "bookAuthor", "ABC123", 5, null);
        User user = new User(1L, "userName", "user@email.com", "userPassword", null);
        doNothing().when(loanService).removeLoan(user.getId(), book.getId());

        ResponseEntity<Void> response = loanController.deleteLoan(user.getId(), book.getId());
        log.info(response.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
