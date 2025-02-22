package com.axeno.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="user_entries")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity {
    @Id
    private ObjectId id;

    @Indexed(unique=true)
    @NonNull
    private String userName;

    @NonNull
    private String passWord;

    @DBRef
    private List<JournalEntity> journalEntries = new ArrayList<>();
}
