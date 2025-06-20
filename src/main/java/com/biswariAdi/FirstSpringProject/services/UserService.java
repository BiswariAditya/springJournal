package com.biswariAdi.FirstSpringProject.services;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder encoder= new BCryptPasswordEncoder();

    public void saveNewUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(List.of("ROLE_USER")); // Default role for new users
        userRepo.save(user);
    }

    public void saveUser(Users user) {
        userRepo.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public Users getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }
    public void deleteUser(Users user) {
        userRepo.delete(user);
    }


}
