package com.biswariAdi.FirstSpringProject.services;

import com.biswariAdi.FirstSpringProject.Entity.JournalEntry;
import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.repository.journalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private journalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    /* to achieve atomicity, we use @Transactional
    this ensures that if any part of the transaction fails, all changes are rolled back
    this can only be used in mongoDB if we have a single collection so to solve this issue we shift from local mongoDB to MongoDB Atlas */
    @Transactional
    public void saveEntry(JournalEntry entry, String username) {
        try {
            Users user = userService.getUserByUserName(username);
            if (user == null) {
                System.out.println("User not found: " + username);
                return;
            }
            JournalEntry saved = journalEntryRepo.save(entry);
            if (user.getJournalEntries() == null) {
                user.setJournalEntries(new ArrayList<>());
            }
            user.getJournalEntries().add(saved);

            userService.saveUser(user);
            System.out.println("Saved entry for user: " + username);
        } catch (Exception e) {
            System.out.println("Error while saving entry: " + e.getMessage());
            throw new RuntimeException("Failed to save entry", e);
        }

    }


    public List<JournalEntry> getAllEntries() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    public void deleteEntryById(ObjectId id, String username) {
        Users user = userService.getUserByUserName(username);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        userService.saveUser(user);
        journalEntryRepo.deleteById(id);
    }

    public void updateEntry(JournalEntry entry) {
        journalEntryRepo.save(entry);
    }

}
