package com.biswariAdi.FirstSpringProject.service;

import com.biswariAdi.FirstSpringProject.services.JournalEntryService;
import com.biswariAdi.FirstSpringProject.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

     @Test
     public void testSaveNewUser() {
         assert userService != null : "UserService should not be null";
     }

     @Test
     public void testGetAllUsers() {
         // Implement test logic here
     }
}
