package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantSadhanaEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantSadhanaRepository extends JpaRepository<ParticipantSadhanaEntity, Long> {

    @NonNull
    Page<ParticipantSadhanaEntity> findAll(@NonNull Pageable page);

    @Query(value = "SELECT * FROM PARTICIPANT_SADHANA WHERE " +
            "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName)) AND " +
            "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
            "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
            "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
            "(:sadhanaDate is null OR SADHANA_DATE = :sadhanaDate)",
            countQuery = "SELECT COUNT(*) FROM PARTICIPANT_SADHANA WHERE " +
                    "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName)) AND " +
                    "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
                    "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
                    "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
                    "(:sadhanaDate is null OR SADHANA_DATE = :sadhanaDate)",
            nativeQuery = true)
    @NonNull
    Page<ParticipantSadhanaEntity> findAllByAny(@Param("programName") String programName,
                                                @Param("participantFirstName") String participantFirstName,
                                                @Param("participantLastName") String participantLastName,
                                                @Param("participantContactNumber") String participantContactNumber,
                                                @Param("sadhanaDate") String sadhanaDate,
                                                @NonNull Pageable page);

}
