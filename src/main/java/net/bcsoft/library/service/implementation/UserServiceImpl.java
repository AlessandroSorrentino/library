package net.bcsoft.library.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.UserRepository;
import net.bcsoft.library.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        boolean existingUser = userRepository.existsByEmail(user.getEmail());
        if (existingUser) {
            log.error("User not saved: An user with the same email already exists");
            throw new BadRequestException("An user with the same email already exists");
        }
        userRepository.save(user);
        log.info("User saved successfully: {}", user);
    }

    @Override
    public List<User> readAllUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
        log.info("All users retrieved and sorted by id in ascending order successfully: {}", users);
        return users;
    }

    @Override
    public User readUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.error("User not found with id: {}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        User user = optionalUser.get();
        log.info("User retrieved successfully: {}", user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findById(user.getId());
        if (!existingUserOpt.isPresent()) {
            log.error("User not found with id: {}", user.getId());
            throw new NotFoundException("User not found with id: " + user.getId());
        }
        User existingUser = existingUserOpt.get();
        // Verifica se l'email dell'utente passato come parametro è diversa dall'email dell'utente recuperato
        // e se esiste già un utente con la stessa email nel database
        if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            log.error("User not updated: An user with this email already exists");
            throw new BadRequestException("An user with this email already exists");
        }
        existingUser.setId(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setBooks(user.getBooks());
        userRepository.save(user);
        log.info("User updated successfully: {}", user);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.error("User not found with id: {}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.delete(optionalUser.get());
        log.info("User deleted successfully with id: {}", id);
    }
}
