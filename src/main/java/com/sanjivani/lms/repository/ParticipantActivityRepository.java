package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantActivityEntity;
import com.sanjivani.lms.enums.ProgramType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface ParticipantActivityRepository extends JpaRepository<ParticipantActivityEntity, Long> {

    @NonNull
    Page<ParticipantActivityEntity> findAll(@NonNull Pageable page);

    @Query(value = "SELECT * FROM PARTICIPANT_ACTIVITY WHERE " +
            "(:activityName is null OR LOWER(ACTIVITY_NAME) = LOWER(:activityName)) AND " +
            "(:courseCode is null OR LOWER(COURSE_CODE) = LOWER(:courseCode)) AND " +
            "(:levelName is null OR LOWER(LEVEL_NAME) = LOWER(:levelName)) AND " +
            "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
            "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
            "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
            "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName)) AND " +
            "(:scheduledSessionName is null OR LOWER(SCHEDULED_SESSION_NAME) = LOWER(:scheduledSessionName)) AND " +
            "(:activityDate is null OR ACTIVITY_DATE = :activityDate)",
            countQuery = "SELECT COUNT(*) FROM PARTICIPANT_ACTIVITY WHERE " +
                    "(:activityName is null OR LOWER(ACTIVITY_NAME) = LOWER(:activityName)) AND " +
                    "(:courseCode is null OR LOWER(COURSE_CODE) = LOWER(:courseCode)) AND " +
                    "(:levelName is null OR LOWER(LEVEL_NAME) = LOWER(:levelName)) AND " +
                    "(:participantContactNumber is null OR LOWER(PARTICIPANT_CONTACT_NUMBER) = LOWER(:participantContactNumber)) AND " +
                    "(:participantFirstName is null OR LOWER(PARTICIPANT_FIRST_NAME) = LOWER(:participantFirstName)) AND " +
                    "(:participantLastName is null OR LOWER(PARTICIPANT_LAST_NAME) = LOWER(:participantLastName)) AND " +
                    "(:programName is null OR LOWER(PROGRAM_NAME) = LOWER(:programName)) AND " +
                    "(:scheduledSessionName is null OR LOWER(SCHEDULED_SESSION_NAME) = LOWER(:scheduledSessionName)) AND " +
                    "(:activityDate is null OR ACTIVITY_DATE = :activityDate)",
            nativeQuery = true)
    @NonNull
    Page<ParticipantActivityEntity> findAllByAny(@Param("activityName") String activityName, @Param("courseCode") String courseCode,
                                                 @Param("levelName") String levelName, @Param("participantContactNumber") String participantContactNumber,
                                                 @Param("participantFirstName") String participantFirstName, @Param("participantLastName") String participantLastName,
                                                 @Param("programName") String programName, @Param("scheduledSessionName") String scheduledSessionName,
                                                 @Param("activityDate") String activityDate, @NonNull Pageable page);

}
