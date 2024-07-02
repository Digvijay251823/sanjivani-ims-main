package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Attendance;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {
    void save(Attendance attendance) throws IllegalArgumentException;

    Attendance getAttendanceByParticipantAndSession(@NonNull Long participantId, @NonNull Long scheduledSessionId);
    @NonNull
    Page<Attendance> getAttendanceBySession(@NonNull Long scheduledSessionId, @NonNull Pageable page);

    @NonNull
    Page<Attendance> getAttendance(@NonNull Pageable page);
}
