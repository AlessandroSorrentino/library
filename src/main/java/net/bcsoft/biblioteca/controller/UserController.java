package net.bcsoft.biblioteca.controller;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.model.User;
import net.bcsoft.biblioteca.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

private final UserService userService;

@PostMapping("/create")
public ResponseEntity<Void> registerUser(@Validated @RequestBody User user) {
    userService.registerUser(user);
    return new ResponseEntity<>(HttpStatus.CREATED);
}
@GetMapping("/get-all")
public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    if (users.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
}
}
