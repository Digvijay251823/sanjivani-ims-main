package com.sanjivani.lms.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sanjivani.lms.model.ParticipantRegistration;

import lombok.NonNull;

public interface ParticipantRegistrationService {
    void save(ParticipantRegistration participantRegistration) throws Exception;
    @NonNull
    Page<ParticipantRegistration> getParticipantRegistration(@NonNull Pageable page);

    @NonNull
    Page<ParticipantRegistration> getParticipantRegistrationByAny(String levelName, String displayLevelName,
                                                          String participantContactNumber, String participantFirstName,
                                                          String participantLastName, String programName,
                                                          @NonNull Pageable page);
}
