package com.biswariAdi.FirstSpringProject.repository;

import com.biswariAdi.FirstSpringProject.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface journalEntryRepo extends MongoRepository<JournalEntry, ObjectId> {
}
