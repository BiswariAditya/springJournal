package com.biswariAdi.FirstSpringProject.controller;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<Collection<Users>> getAllUsers() {
        Collection<Users> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allUsers);
    }


    // Get user by username
    @GetMapping("/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable("username") String username) {
        Optional<Users> user = userService.getAllUsers()
                .stream()
                .filter(u -> u.getUserName().equals(username))
                .findFirst();

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update an existing user by username
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody Users updatedUser) {
        Users existingUser = userService.getUserByUserName(username);
        if (existingUser != null) {
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setPassword(updatedUser.getPassword());
            userService.updateuser(existingUser);
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
