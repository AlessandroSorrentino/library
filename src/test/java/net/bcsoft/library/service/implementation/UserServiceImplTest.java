package net.bcsoft.library.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import net.bcsoft.library.model.User;
import net.bcsoft.library.repository.UserRepository;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "User", "testuser@example.com", "password123", null);

    }

    @Test
    void testSaveUser_success() {
        when(userRepository.existsByEmailIgnoreCase(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertEquals(user, savedUser);
        verify(userRepository, times(1)).existsByEmailIgnoreCase(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_nullUser_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> userService.saveUser(null));

        verify(userRepository, never()).existsByEmailIgnoreCase(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSaveUser_existingUser_throwBadRequestException() {
        when(userRepository.existsByEmailIgnoreCase(user.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.saveUser(user));

        verify(userRepository, times(1)).existsByEmailIgnoreCase(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testReadAllUsers_success() {
        User user2 = new User();
        user2.setId(2L);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.readAllUsers();

        assertEquals(2, retrievedUsers.size());
        assertEquals(user, retrievedUsers.get(0));
        assertEquals(user2, retrievedUsers.get(1));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testReadAllUsers_emptyList_throwNotFoundException() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NotFoundException.class, () -> userService.readAllUsers());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testReadUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.readUserById(1L);
        assertThat(result).isEqualTo(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testReadUserById_nullId_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> userService.readUserById(null));
        verify(userRepository, times(0)).findById(anyLong());
    }

    @Test
    void testReadUserById_userNotFound_throwNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.readUserById(2L));
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testUpdateUser_emailAlreadyExists_throwBadRequestException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailIgnoreCase("updateduser@example.com")).thenReturn(true);

        User updatedUser = new User(1L, "User", "updateduser@example.com", "newpassword", null);

        assertThrows(BadRequestException.class, () -> userService.updateUser(updatedUser));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(updatedUser);
    }

    @Test
    void testUpdateUser_nullUser_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> userService.updateUser(null));
        verify(userRepository, times(0)).findById(any());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateUser_nullUserId_throwBadRequestException() {
        User userNull = new User();
        assertThrows(BadRequestException.class, () -> userService.updateUser(userNull));
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_userNotFound_throwNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User updatedUser = new User(2L, "User", "updateduser@example.com", "newpassword", null);

        assertThrows(NotFoundException.class, () -> userService.updateUser(updatedUser));

        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, never()).save(updatedUser);
    }

    @Test
    void testDeleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_userNotFound_throwNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(2L));

        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, times(0)).deleteById(1L);
    }

    @Test
    void testDeleteUser_nullId_throwBadRequestException() {
        assertThrows(BadRequestException.class, () -> userService.deleteUser(null));

        verify(userRepository, times(0)).existsById(2L);
        verify(userRepository, times(0)).deleteById(2L);

    }
}
