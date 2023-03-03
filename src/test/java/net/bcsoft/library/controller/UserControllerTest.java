package net.bcsoft.library.controller;

import net.bcsoft.library.dto.UserDTO;
import net.bcsoft.library.mapper.UserMapper;
import net.bcsoft.library.model.User;
import net.bcsoft.library.service.UserService;
import net.bcsoft.library.service.implementation.BookServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String USER_NAME = "alesorry";
    private static final String USER_NAME2 = "vincysorry";

    private static final String USER_PASSWORD = "testpassword";
    private static final String USER_EMAIL = "alesorry@test.com";
    private static final String USER_EMAIL2 = "vincysorry@test.com";

    private static final Logger log = LogManager.getLogger(BookServiceImpl.class);

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, userMapper);
    }

    @Test
    void createUser() {
        User user = new User(1L, USER_NAME, USER_EMAIL, USER_PASSWORD, null);
        UserDTO userDTO = new UserDTO(USER_NAME, USER_EMAIL);
        when(userMapper.toEntity(userDTO)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getAllUsers() {
        User user1 = new User(1L, USER_NAME, USER_EMAIL, USER_PASSWORD, null);
        User user2 = new User(1L, USER_NAME2, USER_EMAIL2, USER_PASSWORD, null);
        List<User> users = Arrays.asList(user1, user2);

        UserDTO userDTO1 = new UserDTO(USER_NAME, USER_EMAIL);
        UserDTO userDTO2 = new UserDTO(USER_NAME2, USER_EMAIL2);
        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);

        when(userService.readAllUsers()).thenReturn(users);
        when(userMapper.toDTO(user1)).thenReturn(userDTO1);
        when(userMapper.toDTO(user2)).thenReturn(userDTO2);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());
    }

    @Test
    void getUserById() {
        Long id = 1L;
        User user = new User(id, USER_NAME, USER_EMAIL, USER_PASSWORD, null);
        UserDTO userDTO = new UserDTO(USER_NAME, USER_EMAIL);
        when(userService.readUserById(id)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(id);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }
    @Test
    void updateUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("oldUsername", "oldEmail@test.com");
        User updatedUser = new User(userId, USER_NAME, USER_EMAIL, USER_PASSWORD, null);
        when(userMapper.toEntity(userDTO)).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, userDTO);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void deleteBook() {
        Long userId = 1L;
        ResponseEntity<Void> response = userController.deleteUser(userId);
        log.info(response.getStatusCode() + "\n" + response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }
}
