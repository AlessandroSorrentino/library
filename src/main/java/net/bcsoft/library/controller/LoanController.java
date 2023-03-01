package net.bcsoft.library.controller;

import lombok.RequiredArgsConstructor;
import net.bcsoft.library.dto.LoanDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookMapper bookMapper;

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
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<Book> loans = loanService.readAllLoans();
        List<LoanDTO> loanDTOs = loans.stream().map(bookMapper::toLoanDTO).collect(Collectors.toList());
        return new ResponseEntity<>(loanDTOs, HttpStatus.OK);
    }
}
