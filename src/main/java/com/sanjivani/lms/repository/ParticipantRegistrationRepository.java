package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantRegistrationEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantRegistrationRepository extends JpaRepository<ParticipantRegistrationEntity, Long> {

    @NonNull
    Page<ParticipantRegistrationEntity> findAll(@NonNull Pageable page);

    @Query(value = "SELECT * FROM PARTICIPANT_REGISTRATION WHERE " +
            "(:levelName is null OR LOWER(LEVEL_NAME) = LOWER(:levelName)) AND " +
            "(:levelDisplayName is null OR LOWER(LEVEL_DISPLAY_NAME) = LOWER(:levelDisplayName)) AND " +
            "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
            "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
            "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
            "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName))",
            countQuery = "SELECT COUNT(*) FROM PARTICIPANT_ACTIVITY WHERE " +
                    "(:levelName is null OR LOWER(LEVEL_NAME) = LOWER(:levelName)) AND " +
                    "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
                    "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
                    "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
                    "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName))",
            nativeQuery = true)
    @NonNull
    Page<ParticipantRegistrationEntity> findAllByAny(@Param("levelName") String levelName,
                                                     @Param("levelDisplayName") String levelDisplayName,
                                                     @Param("participantContactNumber") String participantContactNumber,
                                                     @Param("participantFirstName") String participantFirstName,
                                                     @Param("participantLastName") String participantLastName,
                                                     @Param("programName") String programName,
                                                     @NonNull Pageable page);

}
