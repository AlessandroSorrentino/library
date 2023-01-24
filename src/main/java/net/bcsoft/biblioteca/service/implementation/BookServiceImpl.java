package net.bcsoft.biblioteca.service.implementation;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.exception.NotFoundException;
import net.bcsoft.biblioteca.model.Book;
import net.bcsoft.biblioteca.repository.BookRepository;
import net.bcsoft.biblioteca.service.BookService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class BookServiceImpl implements BookService {

private final BookRepository bookRepository;

    @Override
    public Book findBookBySerialNumber(String serialNumber) {
        return bookRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    // Il metodo of() della classe Optional in Java crea un nuovo oggetto Optional che contiene un valore specifico.
    // Se il valore passato come argomento è null, verrà lanciata un'eccezione NullPointerException.
    @Override
    public List<Book> findBooksByAuthor(String author) {
        return Optional.of(bookRepository.findByAuthor(author))
                .orElseThrow(() -> new NotFoundException("Books by author not found"));
    }

    @Override
    public List<Book> getAllBooks() {
        return Optional.of(bookRepository.findAll())
                .orElseThrow(() -> new NotFoundException("Books not found"));
    }
}
