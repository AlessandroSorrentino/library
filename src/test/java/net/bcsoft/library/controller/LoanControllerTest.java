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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
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
        List<Book> loans = new ArrayList<>();
        loans.add(new Book());
        List<LoanDTO> loanDTOs = new ArrayList<>();
        loanDTOs.add(new LoanDTO());
        when(loanService.readAllLoans()).thenReturn(loans);
        when(bookMapper.toLoanDTO(any(Book.class))).thenReturn(new LoanDTO());
        ResponseEntity<List<LoanDTO>> responseEntity = loanController.getAllLoans();
        log.info(responseEntity.getStatusCode() + "\n" + responseEntity.getBody());
        assert responseEntity.getStatusCode().equals(HttpStatus.OK);
        assert responseEntity.getBody().size() == 1;
    }

    @Test
    void testDeleteLoan() {
        Long userId = 1L;
        Long bookId = 1L;
        doNothing().when(loanService).removeLoan(userId, bookId);
        ResponseEntity<Void> responseEntity = loanController.deleteLoan(userId, bookId);
        assert responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT);
    }
}
