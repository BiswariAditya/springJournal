package com.biswariAdi.FirstSpringProject.services;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public void saveUser(Users user) {
        userRepo.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public void updateuser(Users user) {
        userRepo.save(user);
    }

    public Users getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }


}
