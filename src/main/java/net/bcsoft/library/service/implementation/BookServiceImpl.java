package net.bcsoft.library.service.implementation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.service.BookService;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) {
        if (book == null) {
            log.error("Book not saved: Book is null");
            throw new BadRequestException("Book cannot be null");
        }
        boolean existingBook = bookRepository.existsBySerialCodeIgnoreCase(book.getSerialCode());

        if (existingBook) {
            log.error("Book not saved: A book with the same serial code already exists");
            throw new BadRequestException("A book with the serial code already exists");
        }
        bookRepository.save(book);

        log.info("Book saved successfully: {}", book);
        return book;
    }

    @Override
    public List<Book> readAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            log.warn("No books found");
            throw new NotFoundException("No books found");
        }
        books.sort(Comparator.comparing(Book::getId));

        log.info("All books retrieved and sorted by id in ascending order successfully: {}", books);
        return books;
    }

    @Override
    public Book readBookById(Long id) {
        if (id == null) {
            log.error("Book not read: Book id is null");
            throw new BadRequestException("Book id cannot be null");
        }
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (!optionalBook.isPresent()) {
            log.error("Book not found with id: {}", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
        Book book = optionalBook.get();

        log.info("Book retrieved successfully with id {}: {}", id, book);
        return book;
    }

    @Override
    public Book readBookBySerialCode(String serialCode) {
        if (serialCode == null || serialCode.trim().isEmpty()) {
            log.error("Books not read: Serial code is null or empty");
            throw new BadRequestException("Serial code cannot be null or empty");
        }
        Optional<Book> optionalBook = bookRepository.findBySerialCodeIgnoreCase(serialCode);

        if (!optionalBook.isPresent()) {
            log.error("Book not found with serial code: {}", serialCode);
            throw new NotFoundException("Book not found with serial code: " + serialCode);
        }
        Book book = optionalBook.get();

        log.info("Book retrieved successfully with serialcode {}: {}", serialCode, book);
        return book;
    }

    @Override
    public List<Book> readBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            log.error("Books not read: Author is null or empty");
            throw new BadRequestException("Author cannot be null or empty");
        }
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);

        if (books == null || books.isEmpty()) {
            log.warn("No books found with author: {}", author);
            throw new NotFoundException("No books found with author: " + author);
        }

        log.info("Books retrieved successfully with author {}: {}", author, books);
        return books;
    }

    @Override
    public Book updateBook(Book book) {
        if (book == null || book.getId() == null) {
            log.error("Book not updated: Book or book id is null");
            throw new BadRequestException("Book and book id cannot be null");
        }
        Optional<Book> optionalBook = bookRepository.findById(book.getId());

        if (!optionalBook.isPresent()) {
            log.error("Book not found with id: {}", book.getId());
            throw new NotFoundException("Book not found with id: " + book.getId());
        }
        Book existingBook = optionalBook.get();

        if (!existingBook.getSerialCode().equals(book.getSerialCode()) && bookRepository.existsBySerialCodeIgnoreCase(book.getSerialCode())) {
            log.error("Book not updated: A book with this serial code already exists");
            throw new BadRequestException("A book with this serial code already exists");
        }
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setSerialCode(book.getSerialCode());
        existingBook.setQuantity(book.getQuantity());
        existingBook.setUsers(book.getUsers());
        bookRepository.save(existingBook);

        log.info("Book updated successfully: {}", existingBook);
        return existingBook;
    }

    @Override
    public void deleteBook(Long id) {
        if (id == null) {
            log.error("Book not deleted: Book id is null");
            throw new BadRequestException("Book id cannot be null");
        }
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (!optionalBook.isPresent()) {
            log.error("Book not found with id: {}", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);

        log.info("Book deleted successfully with id: {}", id);
    }
}