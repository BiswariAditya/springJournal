package com.biswariAdi.FirstSpringProject.controller;

import com.biswariAdi.FirstSpringProject.Entity.JournalEntry;
import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.JournalEntryService;
import com.biswariAdi.FirstSpringProject.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//  controller--->service--->repository

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService UserService;


    // Get all entries
    @GetMapping("{username}")
    public ResponseEntity<?> getAllEntriesByUsers(@PathVariable String username) {
        Users user = UserService.getUserByUserName(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> all = user.getJournalEntries();
        if (all.isEmpty()) {
            return new ResponseEntity<>("No journal entries found", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
    }


    // Create a new entry
    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry, @PathVariable String username) {
        try {
            System.out.println("Received entry: " + entry);
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry, username);
            System.out.println("Saved entry for user: " + username);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Get entry by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.getEntryById(id);
        if (entry.isPresent()) {
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete entry by ID
    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id, @PathVariable String username) {
        journalEntryService.deleteEntryById(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update entry by ID (partial update)
    @PutMapping("/id/{username}/{id}")
    public ResponseEntity<?> updateJournalEntry(@RequestBody JournalEntry entry, @PathVariable ObjectId id, @PathVariable String username) {
        Optional<JournalEntry> existingEntry = journalEntryService.getEntryById(id);
        if (existingEntry.isPresent()) {
            JournalEntry updatedEntry = existingEntry.get();
            updatedEntry.setTitle(entry.getTitle());
            updatedEntry.setContent(entry.getContent());
            updatedEntry.setDate(LocalDateTime.now());
            journalEntryService.updateEntry(updatedEntry);
            return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }
    }
}
