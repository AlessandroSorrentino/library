package net.bcsoft.library.repository;

import net.bcsoft.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBySerialCodeIgnoreCase(String serialCode);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByUsersIsNotNull();
    boolean existsBySerialCode(String serialCode);
}