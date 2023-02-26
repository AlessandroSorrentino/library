package net.bcsoft.library.service.implementation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.service.BookService;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

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
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
    }

    @Override
    public List<Book> readBooksBySerialCode(String serialCode) {
        List<Book> books = bookRepository.findBySerialCodeIgnoreCase(serialCode);
        log.info("Books retrieved successfully with serial code {}: {}", serialCode, books);
        return books;
    }

    @Override
    public List<Book> readBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        log.info("Books retrieved successfully with author {}: {}", author, books);
        return books;
    }

    @Override
    public void updateBook(Book book) {
        Optional<Book> existingBookOpt = bookRepository.findById(book.getId());
        if (!existingBookOpt.isPresent()) {
            log.error("Book not found with id: {}", book.getId());
            throw new NotFoundException("Book not found with id: " + book.getId());
        }
        Book existingBook = existingBookOpt.get();
        if (!existingBook.getSerialCode().equals(book.getSerialCode()) && bookRepository.existsBySerialCode(book.getSerialCode())) {
            log.error("Book not updated: A book with the same serial code already exists");
            throw new BadRequestException("A book with the serial code already exists");
        }
        existingBook.setId(book.getId());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setSerialCode(book.getSerialCode());
        existingBook.setQuantity(book.getQuantity());
        existingBook.setUsers(book.getUsers());
        bookRepository.save(existingBook);
        log.info("Book updated successfully: {}", existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            log.error("Book not found with id: {}", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
        bookRepository.delete(optionalBook.get());
        log.info("Book deleted successfully with id: {}", id);
    }
}
