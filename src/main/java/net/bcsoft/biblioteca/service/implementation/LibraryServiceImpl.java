package net.bcsoft.biblioteca.service.implementation;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.exception.BadRequestException;
import net.bcsoft.biblioteca.exception.NotFoundException;
import net.bcsoft.biblioteca.model.Book;
import net.bcsoft.biblioteca.model.Library;
import net.bcsoft.biblioteca.model.User;
import net.bcsoft.biblioteca.repository.BookRepository;
import net.bcsoft.biblioteca.repository.LibraryRepository;
import net.bcsoft.biblioteca.repository.UserRepository;
import net.bcsoft.biblioteca.service.LibraryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public void createLibrary(Library library) {
        if (libraryRepository.existsById(library.getId())) {
            throw new BadRequestException("Library with id " + library.getId() + " already exists");
        }
        libraryRepository.save(library);
    }

    @Override
    public void addBookToLibrary(Long libraryId, Book book) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new NotFoundException("Library with id " + libraryId + " not found"));

        Optional<Book> existingBook = bookRepository.findBySerialNumber(book.getSerialNumber());
        if(existingBook.isPresent()) {
            throw new BadRequestException("Book with serial number " + book.getSerialNumber() + " already present in the library");
        }
        book.setLibrary(library); // aggiungiamo il libro nella biblioteca
        library.setAmount(library.getAmount() + 1);
        bookRepository.save(book);
        libraryRepository.save(library);
    }

    @Override
    public Library findLibraryById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Library with id " + id + "not found"));
    }

    @Override
    public void updateBookAmount(Long libraryId, int amount) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new NotFoundException("Library with id " + libraryId + " not found"));
        if (amount < 0) {
            throw new BadRequestException("Amount cannot be negative");
        }
        library.setAmount(amount);
        libraryRepository.save(library);
    }


    @Override
    public void borrowBook(Long userId, Long bookId ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id " + bookId + "not found"));
        if (user.getBorrowedBooks() >= 3) {
            throw new BadRequestException("User already borrowed 3 books");
        }
        if (book.getLibrary().getAmount() < 1) {
            throw new BadRequestException("Book not available");
        }
        if (book.getUser() != null) {
            throw new BadRequestException("Book already borrowed by another user");
        }
        book.setUser(user);
        user.setBorrowedBooks(user.getBorrowedBooks() + 1);
        book.getLibrary().setAmount(book.getLibrary().getAmount() - 1);
        bookRepository.save(book);
        userRepository.save(user);
        libraryRepository.save(book.getLibrary());
    }
}