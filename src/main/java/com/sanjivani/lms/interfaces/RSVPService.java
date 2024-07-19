package com.sanjivani.lms.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sanjivani.lms.model.RSVP;

import lombok.NonNull;

public interface RSVPService {
    void save(RSVP rsvp) throws IllegalArgumentException;
    RSVP getRSVPByParticipantAndSession(@NonNull Long participantId, @NonNull Long scheduledSessionId);
    @NonNull
    Page<RSVP> getRSVP(@NonNull Pageable page);
    @NonNull
    Page<RSVP> getRSVPBySession(@NonNull Long scheduledSessionId, @NonNull Pageable page);
}
