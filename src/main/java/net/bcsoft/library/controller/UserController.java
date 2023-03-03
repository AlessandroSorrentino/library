package net.bcsoft.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.bcsoft.library.dto.UserDTO;
import net.bcsoft.library.mapper.UserMapper;
import net.bcsoft.library.model.User;
import net.bcsoft.library.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(value = "User Management System")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ApiOperation(value = "Create user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        userService.saveUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.readAllUsers();
        List<UserDTO> userDTOs = users.stream().map(userMapper::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        User user = userService.readUserById(id);
        return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user by id")
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setId(id);
        userService.updateUser(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
