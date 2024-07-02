package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.ParticipantActivity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ParticipantActivityService {
    void save(ParticipantActivity participantActivity) throws Exception;
    @NonNull
    Page<ParticipantActivity> getParticipantActivity(@NonNull Pageable page);

    @NonNull
    Page<ParticipantActivity> getParticipantActivityByAny(String activityName, String courseCode, String levelName,
                                                          String participantContactNumber, String participantFirstName,
                                                          String participantLastName, String programName,
                                                          String scheduledSessionName, String activityDate, @NonNull Pageable page);
}
