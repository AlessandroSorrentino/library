package net.bcsoft.library.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.service.BookService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public void saveBook(Book book) {
        boolean existingBook = bookRepository.existsBySerialCode(book.getSerialCode());
        if (existingBook) {
            log.error("Book not saved: A book with the same serial code already exists");
            throw new BadRequestException("A book with the serial code already exists");
        }
        bookRepository.save(book);
        log.info("Book saved successfully: {}", book);
    }

    @Override
    public List<Book> readAllBooks() {
        List<Book> books = bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getId));
        log.info("All books retrieved and sorted by id in ascending order successfully: {}", books);
        return books;
    }

    @Override
    public Book readBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new NotFoundException("Book not found with id: " + id);
                });
    }

    @Override
    public List<Book> readBooksBySerialCode(String serialCode) {
        List<Book> books = bookRepository.findBySerialCode(serialCode);
        log.info("Books retrieved successfully with serial code {}: {}", serialCode, books);
        return books;
    }

    @Override
    public List<Book> readBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        log.info("Books retrieved successfully with author {}: {}", author, books);
        return books;
    }

    @Override
    public void updateBook(Book book) {
        Optional<Book> existingBook = bookRepository.findById(book.getId());
        existingBook.ifPresent(b -> {
            bookRepository.save(book);
            log.info("Book updated successfully: {}", book);
        });
        if (!existingBook.isPresent()) {
            log.error("Book not found with id: {}", book.getId());
            throw new NotFoundException("Book not found with id: " + book.getId());
        }
    }

    @Override
    public void deleteBook(Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);
        existingBook.ifPresent(book -> {
            bookRepository.delete(book);
            log.info("Book deleted successfully with id: {}", id);
        });
        if (!existingBook.isPresent()) {
            log.error("Book not found with id: {}", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
    }
}
