package net.bcsoft.library.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.repository.UserRepository;
import net.bcsoft.library.service.LoanService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class LoanServiceImpl implements LoanService {


    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public void addLoan(Long userId, Long bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (!userOptional.isPresent()) {
            log.error("User not found with id: {}", userId);
            throw new NotFoundException("User not found");
        } else if (!bookOptional.isPresent()) {
            log.error("Book not found with id: {}", bookId);
            throw new NotFoundException("Book not found");
        } else {
            User user = userOptional.get();
            Book book = bookOptional.get();
            if (book.getQuantity() == 0) {
                log.error("Book is not available with id: {}", bookId);
                throw new BadRequestException("Book is not available");
            } else if (user.getBooks().size() >= 3) {
                log.error("User has borrowed 3 books already with id: {}", userId);
                throw new BadRequestException("User has borrowed 3 books already");
            } else {
                user.getBooks().add(book);
                book.getUsers().add(user);
                book.setQuantity(book.getQuantity() - 1);
                userRepository.save(user);
                bookRepository.save(book);
                log.info("Loan added successfully with userId: {} and bookId: {}", userId, bookId);
            }
        }
    }

    @Override
    public void removeLoan(Long userId, Long bookId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (!userOptional.isPresent()) {
            log.error("User not found with id: {}", userId);
            throw new NotFoundException("User not found");
        } else if (!bookOptional.isPresent()) {
            log.error("Book not found with id: {}", bookId);
            throw new NotFoundException("Book not found");
        } else {
            User user = userOptional.get();
            Book book = bookOptional.get();
            if (!user.getBooks().contains(book)) {
                log.error("User doesn't have the book with id: {}", bookId);
                throw new BadRequestException("User doesn't have the book");
            } else {
                user.getBooks().remove(book);
                book.getUsers().remove(user);
                book.setQuantity(book.getQuantity() + 1);
                userRepository.save(user);
                bookRepository.save(book);
                log.info("Loan removed successfully with userId: {} and bookId: {}", userId, bookId);
            }
        }
    }

    @Override
    public List<Book> readAllLoans() {
        List<Book> loans = bookRepository.findByUsersIsNotNull();
        log.info("Loans retrieved successfully: " + loans.size());
        return loans;
    }

}

