package net.bcsoft.library.controller;

import net.bcsoft.library.dto.UserDTO;
import net.bcsoft.library.mapper.UserMapper;
import net.bcsoft.library.model.User;
import net.bcsoft.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO("alesorry", "alesorry@email.com");
        user = new User(1L, "alesorry", "alesorry@email.com", "password", new ArrayList<>());
    }

    @Test
    void createUser() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        verify(userMapper, times(1)).toEntity(userDTO);
        verify(userService, times(1)).saveUser(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(userDTO);
    }

    @Test
    void getAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userService.readAllUsers()).thenReturn(users);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        verify(userService, times(1)).readAllUsers();
        verify(userMapper, times(1)).toDTO(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).contains(userDTO);
    }

    @Test
    void getUserById() {
        Long userId = 1L;
        when(userService.readUserById(userId)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        verify(userService, times(1)).readUserById(userId);
        verify(userMapper, times(1)).toDTO(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDTO);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO("new username", "newemail@email.com");
        User updatedUser = new User(userId, "newUsername", "newPassword", "newFirstName", new ArrayList<>());

        when(userMapper.toEntity(updatedUserDTO)).thenReturn(updatedUser);
        when(userService.updateUser(updatedUser)).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, updatedUserDTO);

        verify(userMapper, times(1)).toEntity(updatedUserDTO);
        verify(userService, times(1)).updateUser(updatedUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedUserDTO);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        verify(userService, times(1)).deleteUser(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}