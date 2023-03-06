package net.bcsoft.library.repository;

import net.bcsoft.library.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest // ha lo scopo di semplificare il processo di testing, caricando solo le componenti necessarie per i test dell'accesso ai dati.
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSave() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());

        User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    void testFindById() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindAll() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        List<User> allUsers = userRepository.findAll();

        Assertions.assertEquals(1, allUsers.size());
    }

    @Test
    void testUpdate() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        user.setEmail("new_email@gmail.com");
        userRepository.save(user);

        Optional<User> updatedUser = userRepository.findById(user.getId());

        Assertions.assertTrue(updatedUser.isPresent());
        Assertions.assertEquals("new_email@gmail.com", updatedUser.get().getEmail());
    }

    @Test
    void testDelete() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findById(user.getId());

        Assertions.assertFalse(deletedUser.isPresent());
    }

    @Test
    void findByEmailIgnoreCase() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmailIgnoreCase("email@gmail.com");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void existsByEmailIgnoreCase() {
        User user = new User(null, "username", "email@gmail.com","password", new ArrayList<>());
        userRepository.save(user);

        boolean exists = userRepository.existsByEmailIgnoreCase("eMaIl@gmail.com");

        Assertions.assertTrue(exists);
    }
}
