package com.biswariAdi.FirstSpringProject.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
//first using LOMBOK we can avoid boilerplate code like getters and setters, and then we can use @Getter and @Setter annotations
// now instead of writing getter and setter and no args constructor we can just use @data annotation
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    @JsonProperty("id") // Ensures id is treated as string in JSON
    private ObjectId id;
    private String title;
    private  String content;
    private LocalDateTime date;


}
