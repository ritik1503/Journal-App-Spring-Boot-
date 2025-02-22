package com.axeno.journalApp.service;

import com.axeno.journalApp.entity.JournalEntity;
import com.axeno.journalApp.entity.UserEntity;
import com.axeno.journalApp.repository.JournalRepository;
import com.axeno.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean saveJournalEntry(JournalEntity journalEntry,String userName) throws Exception {
        UserEntity user = userRepository.findByUserName(userName).orElse(null);
        if (user == null) {
            return false;
        }
        journalEntry.setDate(LocalDateTime.now());
        journalRepository.save(journalEntry);
        user.getJournalEntries().add(journalEntry);
        userRepository.save(user);
        return true;
    }
    public List<JournalEntity> getJournalEntries() {
        return journalRepository.findAll();
    }
    public Optional<JournalEntity> getJournalEntry(ObjectId id) {
        return journalRepository.findById(id);
    }

    public boolean deleteJournalEntry(ObjectId id,String userName) {
        //check if user exists or not
        UserEntity user = userRepository.findByUserName(userName).orElse(null);
        if (user == null) {
            return false;
        }
        user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userRepository.save(user);
        journalRepository.deleteById(id);
        return true;
    }
    public boolean updateUserById(ObjectId objectId,String userName,JournalEntity journalEntry) {
        UserEntity user = userRepository.findByUserName(userName).orElse(null);
        JournalEntity currentJournalEntry = journalRepository.findById(objectId).orElse(null);
        if (user == null || currentJournalEntry == null) {
            return false;
        }
        currentJournalEntry.setDate(LocalDateTime.now());
        currentJournalEntry.setTitle(!journalEntry.getTitle().isEmpty()?journalEntry.getTitle():currentJournalEntry.getTitle());
        currentJournalEntry.setContent(!journalEntry.getContent().isEmpty()?journalEntry.getContent():currentJournalEntry.getContent());
        journalRepository.save(currentJournalEntry);
        return true;
    }
}
