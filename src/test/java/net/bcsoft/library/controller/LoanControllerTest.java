package net.bcsoft.library.controller;

import net.bcsoft.library.dto.LoanDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private LoanController loanController;

    private Book book1;
    private Book book2;
    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        loanController = new LoanController(loanService, bookMapper);
        book1 = new Book(1L, "bookTitle1", "bookAuthor1", "ABC123", 5, new ArrayList<>());
        book2 = new Book(2L, "bookTitle2", "bookAuthor2", "DEF456", 2, new ArrayList<>());
        user1 = new User(1L, "userName1", "userEmail1", "userPassword1", new ArrayList<>());
        user2 = new User(2L, "userName2", "userEmail2", "userPassword2", new ArrayList<>());
    }

    @Test
    void createLoan() {
        doNothing().when(loanService).addLoan(user1.getId(), book1.getId());

        ResponseEntity<Void> response = loanController.createLoan(user1.getId(), book1.getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(loanService, times(1)).addLoan(user1.getId(), book1.getId());
    }


    @Test
    void getAllLoans() {
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

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLoanDTOs, response.getBody());
        verify(loanService, times(1)).readAllLoans();
    }

    @Test
    void testDeleteLoan() {
        doNothing().when(loanService).removeLoan(user1.getId(), book1.getId());

        ResponseEntity<Void> response = loanController.deleteLoan(user1.getId(), book1.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(loanService, times(1)).removeLoan(user1.getId(), book1.getId());
    }
}
