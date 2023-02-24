package net.bcsoft.library.controller;

import lombok.AllArgsConstructor;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private LoanService loanService;

    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> addLoan(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        loanService.addLoan(userId, bookId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> removeLoan(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        loanService.removeLoan(userId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllLoans() {
        List<Book> loans = loanService.readAllLoans();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }
}
