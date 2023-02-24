package net.bcsoft.library.service;

import net.bcsoft.library.model.Book;

import java.util.List;

public interface LoanService {

    void addLoan(Long userId, Long bookId);
    void removeLoan(Long userId, Long bookId);
    List<Book> readAllLoans();
}
