package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.ScheduledSession;
import com.sanjivani.lms.model.Session;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionService {
    Page<Session> getSessions(Pageable pageable);
    void save(Session session) throws IllegalArgumentException;
    Page<Session> getSessionsByCourse(String courseCode, Pageable pageable);
    Session getSessionById(Long id);
    Session getSessionByCode(String code);
    void schedule(ScheduledSession session) throws IllegalArgumentException;
    ScheduledSession getScheduledSession(Long id);
    Page<ScheduledSession> getScheduledSessionsByLevel(@NonNull Long id, Pageable pageable);
}