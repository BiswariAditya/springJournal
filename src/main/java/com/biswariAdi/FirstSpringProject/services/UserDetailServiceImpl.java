package com.biswariAdi.FirstSpringProject.services;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username); // Debug log

        Users user = userRepo.findByUserName(username);
        if (user == null) {
            System.out.println("User not found: " + username); // Debug log
            throw new UsernameNotFoundException("User not found: " + username);
        }

        System.out.println("User found: " + username); // Debug log
        System.out.println("User roles: " + user.getRoles()); // Debug log
        System.out.println("User password (encoded): " + user.getPassword()); // Debug log

        List<String> roles = user.getRoles();
        String[] processedRoles = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .toArray(String[]::new);

        System.out.println("Processed roles: " + String.join(", ", processedRoles)); // Debug log

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword()) // This should be BCrypt encoded
                .roles(processedRoles) // Spring Security will automatically add "ROLE_" prefix
                .build();
    }
}