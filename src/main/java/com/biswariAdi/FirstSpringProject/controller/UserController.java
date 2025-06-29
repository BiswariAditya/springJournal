package com.biswariAdi.FirstSpringProject.controller;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // Update the currently authenticated user's information
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users updatedUser) {
        try {
            String username = getAuthenticatedUsername();
            Users existingUser = userService.getUserByUserName(username);

            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Avoid changing username unless you have proper handling for it
            // existingUser.setUserName(updatedUser.getUserName());

            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            userService.saveNewUser(existingUser);
            return ResponseEntity.ok("User updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    // Delete the currently authenticated user
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        try {
            String username = getAuthenticatedUsername();
            Users user = userService.getUserByUserName(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            userService.deleteUser(user);
            return ResponseEntity.ok("User deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}
