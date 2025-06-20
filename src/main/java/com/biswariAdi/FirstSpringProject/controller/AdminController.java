package com.biswariAdi.FirstSpringProject.controller;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // Return an error response if something goes wrong
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody Users adminUser) {
        try {
            userService.saveAdmin(adminUser);
            return ResponseEntity.ok("Admin created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating admin: " + e.getMessage());
        }
    }
}
