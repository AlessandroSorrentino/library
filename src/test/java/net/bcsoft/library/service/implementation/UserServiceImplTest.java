package net.bcsoft.library.service.implementation;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.BookRepository;
import net.bcsoft.library.repository.UserRepository;
import net.bcsoft.library.service.BookService;
import net.bcsoft.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //  per attivare l'integrazione tra JUnit e Mockito. In particolare, questa annotazione viene utilizzata per abilitare l'uso di annotazioni come @Mock
class UserServiceImplTest {

    public static final String USERNAME = "AleSorry";
    public static final String EMAIL = "alesorry@example.com";
    public static final String PASSWORD = "password123";
    public static final long USER_ID = 1L;
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testSaveUser() {
        User user = new User(USER_ID, USERNAME, EMAIL, PASSWORD, null);

        // Mock the book repository to return the list of loaned books
        when(userRepository.save(user)).thenReturn(user);

        // Test adding a new user
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals(user, savedUser);

        // Test adding a new user that doesn't exist
        assertThrows(BadRequestException.class, () -> userService.saveUser(null));

        // Create a new user with a duplicate email
        User duplicateUser = new User(2L, "DuplicateUser", "alesorry@example.com", "password123", null);
        when(userRepository.existsByEmailIgnoreCase(duplicateUser.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.saveUser(duplicateUser));
    }


    @Test
    void testReadAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(USER_ID, USERNAME, EMAIL , PASSWORD,null));
        userList.add(new User(2L, "AleDolly", "aledolly@example.com", "password456", null));

        // Configure the mock objects to return the expected results
        when(userRepository.findAll()).thenReturn(userList);

        // Test retrieving all users
        List<User> result = userService.readAllUsers();
        assertNotNull(result);
        assertEquals(userList, result);

        // Test retrieving all users when there are none
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> userService.readAllUsers());
    }

    @Test
    void testReadUserById() {
        User user = new User(USER_ID, USERNAME, EMAIL, PASSWORD, null);

        // Configure the mock objects to return the expected results
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        // Test retrieving a book by id
        User result = userService.readUserById(USER_ID);
        assertNotNull(result);
        assertEquals(user, result);

        // Test retrieving a book with a null id
        assertThrows(BadRequestException.class, () -> userService.readUserById(null));

        // Test retrieving a book with a non-existing id
        assertThrows(NotFoundException.class, () -> userService.readUserById(2L));
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User(USER_ID, USERNAME, EMAIL, PASSWORD, null);
        User updatedUser = new User(1L, "newusername", "newemail@example.com", "newpassword123", null);

        // Configure the mock objects to return the expected results
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Test updating a user
        userService.updateUser(updatedUser);
        assertEquals(updatedUser, existingUser);

        // Test updating a user that doesn't exist
        User nonExistingUser = new User(2L, "NonExisting", "nonexisting@example.com", "password123", null);
        when(userRepository.findById(nonExistingUser.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(nonExistingUser));

        // Test updating a book with an email that already exists
        User duplicateUser = new User(2L, "DuplicateUser", "alesorry@example.com", "password123", null);
        when(userRepository.findById(duplicateUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailIgnoreCase(duplicateUser.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.updateUser(duplicateUser));
    }


    @Test
    void testDeleteUser() {
        User existingUser = new User(USER_ID, USERNAME, EMAIL, PASSWORD, null);

        // Configure the mock objects to return the expected results
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        // Test deleting an user
        userService.deleteUser(existingUser.getId());
        assertThrows(BadRequestException.class, () -> userService.deleteUser(null));

        // Test deleting a user that doesn't exist
        User nonExistingUser = new User(2L, "NonExisting", "nonexisting@example.com", "password123", null);
        when(userRepository.findById(nonExistingUser.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteUser(nonExistingUser.getId()));
    }
}