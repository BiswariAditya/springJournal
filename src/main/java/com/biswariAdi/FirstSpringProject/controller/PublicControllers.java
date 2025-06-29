package com.biswariAdi.FirstSpringProject.controller;
import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.UserDetailServiceImpl;
import com.biswariAdi.FirstSpringProject.services.UserService;
import com.biswariAdi.FirstSpringProject.utils.JWTutils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Tag(name = "Public APIs")
public class PublicControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTutils jwtUtils;

    @GetMapping("/healthCheck")
    public String healthCheck(){
        return "OK";
    }

    // Create a new user
    @PostMapping("/signup")
    public ResponseEntity<Users> signup(@RequestBody Users user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(user.getUserName());
            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
