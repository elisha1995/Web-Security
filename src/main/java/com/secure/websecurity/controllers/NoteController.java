package com.secure.websecurity.controllers;

import com.secure.websecurity.models.Note;
import com.secure.websecurity.services.NoteService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /*@PostMapping
    public Note createNote(@RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("USER DETAILS: " + username);
        if (!isValidNoteContent(content)) {
            throw new IllegalArgumentException("Invalid note content");
        }
        return noteService.createNoteForUser(username, sanitizeContent(content));
    }*/

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody String content,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (!isValidNoteContent(content)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid note content. Content contains disallowed characters or patterns.");
        }
        String username = userDetails.getUsername();
        Note savedNote = noteService.createNoteForUser(username, sanitizeContent(content));
        return ResponseEntity.ok(savedNote);
    }

    /*@GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("USER DETAILS: " + username);
        List<Note> notes = noteService.getNotesForUser(username);
        return notes.stream()
                .map(note -> {
                    note.setContent(StringEscapeUtils.escapeHtml4(note.getContent()));
                    return note;
                })
                .collect(Collectors.toList());
    }*/

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<Note> notes = noteService.getNotesForUser(username);
        return ResponseEntity.ok(notes.stream()
                .map(note -> {
                    note.setContent(StringEscapeUtils.escapeHtml4(note.getContent()));
                    return note;
                })
                .collect(Collectors.toList()));
    }

    /*@PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (!isValidNoteContent(content)) {
            throw new IllegalArgumentException("Invalid note content");
        }
        return noteService.updateNoteForUser(noteId, sanitizeContent(content), username);
    }*/

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Long noteId,
                                        @RequestBody String content,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (!isValidNoteContent(content)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid note content. Content contains disallowed characters or patterns.");
        }
        String username = userDetails.getUsername();
        Note updatedNote = noteService.updateNoteForUser(noteId, sanitizeContent(content), username);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        noteService.deleteNoteForUser(noteId, username);
    }

    private boolean isValidNoteContent(String content) {
        // Basic check to prevent script injections
        return content != null && !content.matches("(?i).*<script.*?>.*</script>.*");
    }

    private String sanitizeContent(String content) {
        // Use Apache Commons Text to escape HTML entities
        return StringEscapeUtils.escapeHtml4(content);
    }
}

