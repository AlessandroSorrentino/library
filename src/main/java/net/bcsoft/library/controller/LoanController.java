package net.bcsoft.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/loans")
@Api(value = "Loan Management System")
public class LoanController {

    private final LoanService loanService;
    private final BookMapper bookMapper;

    @PostMapping("/{userId}/{bookId}")
    @ApiOperation(value = "Create loan by user id and book id")
    public ResponseEntity<Void> createLoan(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        loanService.addLoan(userId, bookId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get all loans")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<Book> loans = loanService.readAllLoans();
        List<LoanDTO> loanDTOs = loans.stream().map(bookMapper::toLoanDTO).collect(Collectors.toList());
        return new ResponseEntity<>(loanDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{bookId}")
    @ApiOperation(value = "Delete by user id and book id")
    public ResponseEntity<Void> deleteLoan(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        loanService.removeLoan(userId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
