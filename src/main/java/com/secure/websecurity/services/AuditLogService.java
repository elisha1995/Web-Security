package com.secure.websecurity.services;

import com.secure.websecurity.models.AuditLog;
import com.secure.websecurity.models.Note;

import java.util.List;

public interface AuditLogService {
    void logNoteCreation(String username, Note note);

    void logNoteUpdate(String username, Note note);

    void logNoteDeletion(String username, Long noteId);

    List<AuditLog> getAllAuditLogs();

    List<AuditLog> getAuditLogsForNoteId(Long id);
}
