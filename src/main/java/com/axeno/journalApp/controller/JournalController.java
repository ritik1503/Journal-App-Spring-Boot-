package com.axeno.journalApp.controller;

import com.axeno.journalApp.entity.JournalEntity;
import com.axeno.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @GetMapping("/getJournal")
    public ResponseEntity<List<JournalEntity>> getJournal() {
        List<JournalEntity> result = journalService.getJournalEntries();
        if(result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/postJournal/{userName}")
    public ResponseEntity<JournalEntity> createJournal(@RequestBody JournalEntity entry, @PathVariable String userName) {
        try{
            boolean isJournalCreated = journalService.saveJournalEntry(entry,userName);
            if(isJournalCreated) {
                return new ResponseEntity<>(entry, HttpStatus.CREATED);
            }
            throw new IOException();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getById/{objectId}")
    public ResponseEntity<JournalEntity> getJournalById(@PathVariable ObjectId objectId){
        Optional<JournalEntity> resultantJournal =  journalService.getJournalEntry(objectId);
        return resultantJournal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteJournalForUser/{userName}/{myId}")
    public ResponseEntity deleteJournal(@PathVariable ObjectId myId, @PathVariable String userName) {
        boolean isDeleted = journalService.deleteJournalEntry(myId, userName);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/byID/{userName}/{myId}")
    public ResponseEntity<JournalEntity> updateJournal(@PathVariable ObjectId myId, @RequestBody JournalEntity entry,@PathVariable String userName) {
        boolean isJournalUpdated = journalService.updateUserById(myId, userName, entry);
        if(isJournalUpdated) {
            return new ResponseEntity<>(entry, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
