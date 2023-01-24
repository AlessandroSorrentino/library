package net.bcsoft.biblioteca.repository;

import net.bcsoft.biblioteca.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    boolean existsById(Long id);
    Optional<Library> findById(Long id);

}
