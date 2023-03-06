package net.bcsoft.library.service.implementation;

import lombok.RequiredArgsConstructor;
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

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        if (user == null) {
            log.error("User not saved: User is null");
            throw new BadRequestException("User cannot be null");
        }
        boolean existingUser = userRepository.existsByEmailIgnoreCase(user.getEmail());

        if (existingUser) {
            log.error("User not saved: An user with the same email already exists");
            throw new BadRequestException("An user with the same email already exists");
        }
        userRepository.save(user);

        log.info("User saved successfully: {}", user);
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.warn("No users found");
            throw new NotFoundException("No users found");
        }
        users.sort(Comparator.comparing(User::getId));

        log.info("All users retrieved and sorted by id in ascending order successfully: {}", users);
        return users;
    }

    @Override
    public User readUserById(Long id) {
        if (id == null) {
            log.error("User not read: User id is null");
            throw new BadRequestException("User id cannot be null");
        }
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
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            log.error("User not updated: User or user id is null");
            throw new BadRequestException("User and user id cannot be null");
        }
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (!optionalUser.isPresent()) {
            log.error("User not found with id: {}", user.getId());
            throw new NotFoundException("User not found with id: " + user.getId());
        }
        User existingUser = optionalUser.get();

        if (!existingUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            log.error("User not updated: An user with this email already exists");
            throw new BadRequestException("An user with this email already exists");
        }
        existingUser.setId(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setBooks(user.getBooks());
        userRepository.save(existingUser);

        log.info("User updated successfully: {}", existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            log.error("User not deleted: User id is null");
            throw new BadRequestException("User id cannot be null");
        }
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            log.error("User not found with id: {}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);

        log.info("User deleted successfully with id: {}", id);
    }
}
