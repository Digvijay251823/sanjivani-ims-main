package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.ParticipantSadhana;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParticipantSadhanaService {
    void save(ParticipantSadhana participantSadhana) throws IllegalArgumentException;
    @NonNull
    Page<ParticipantSadhana> getParticipantSadhana(@NonNull Pageable page);

    ParticipantSadhana getParticipantSadhanaById(@NonNull Long id);

    @NonNull
    Page<ParticipantSadhana> getParticipantSadhanaByAny(String programName, String participantFirstName, String participantLastName,
                                                        String participantContactNumber, String sadhanaDate,
                                                        @NonNull Pageable page);
}
