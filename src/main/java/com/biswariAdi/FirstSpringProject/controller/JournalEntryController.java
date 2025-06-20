package com.biswariAdi.FirstSpringProject.controller;

import com.biswariAdi.FirstSpringProject.Entity.JournalEntry;
import com.biswariAdi.FirstSpringProject.Entity.Users;
import com.biswariAdi.FirstSpringProject.services.JournalEntryService;
import com.biswariAdi.FirstSpringProject.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // Get all journal entries for the authenticated user
    @GetMapping
    public ResponseEntity<?> getAllEntriesByUser() {
        try {
            String username = getAuthenticatedUsername();
            Users user = userService.getUserByUserName(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            List<JournalEntry> entries = user.getJournalEntries();
            if (entries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No journal entries found");
            }

            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving entries");
        }
    }

    // Create a new journal entry
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            String username = getAuthenticatedUsername();
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(entry);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating entry");
        }
    }

    // Get journal entry by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        try {
            String username = getAuthenticatedUsername();
            Users user = userService.getUserByUserName(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Optional<JournalEntry> entry = user.getJournalEntries().stream()
                    .filter(j -> j.getId().equals(id))
                    .findFirst();

            return new ResponseEntity<>( entry, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving entry");
        }
    }


    // Delete journal entry by ID
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        String username = getAuthenticatedUsername();
        boolean removed = journalEntryService.deleteEntryById(id, username);

        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
        }
    }

    // Update journal entry by ID
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry updatedEntry) {
        try {
            String username = getAuthenticatedUsername();
            Users user = userService.getUserByUserName(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean entryExistsForUser = user.getJournalEntries().stream()
                    .anyMatch(entry -> entry.getId().equals(id));

            if (!entryExistsForUser) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found for user");
            }

            Optional<JournalEntry> existingEntryOpt = journalEntryService.getEntryById(id);
            if (existingEntryOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
            }

            JournalEntry existingEntry = existingEntryOpt.get();

            if (updatedEntry.getTitle() != null) {
                existingEntry.setTitle(updatedEntry.getTitle());
            }
            if (updatedEntry.getContent() != null) {
                existingEntry.setContent(updatedEntry.getContent());
            }
            existingEntry.setDate(LocalDateTime.now());

            journalEntryService.saveEntry(existingEntry, username);
            return ResponseEntity.ok(existingEntry);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating entry");
        }
    }
}
