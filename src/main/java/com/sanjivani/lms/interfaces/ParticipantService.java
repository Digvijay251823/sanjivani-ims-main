package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.JapaRoundsParticipantCount;
import com.sanjivani.lms.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParticipantService {
    Page<Participant> getParticipants(Pageable pageable);
    void save(Participant participant) throws IllegalArgumentException;
    Participant getParticipantById(Long id);

    Page<Participant> getParticipantsByFirstName(String firstName, Pageable pageable) throws IllegalArgumentException;
    Page<Participant> getParticipantsByLastName(String lastName, Pageable pageable) throws IllegalArgumentException;
    Page<Participant> getParticipantsByWaNumber(String waNumber, Pageable pageable) throws IllegalArgumentException;
    Participant getParticipantByContactNumber(String contactNumber);
    Page<Participant> getParticipantsByEmail(String emailId, Pageable pageable) throws IllegalArgumentException;

    Page<Participant> getParticipantsByJapaRounds(Integer rounds, Pageable pageable) throws IllegalArgumentException;

    List<JapaRoundsParticipantCount> getGroupByCountOfJapaRounds();

    void updateJapaRounds(Long id, Integer japaRounds) throws IllegalArgumentException;

}
