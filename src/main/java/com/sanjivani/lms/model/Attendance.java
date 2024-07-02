package com.sanjivani.lms.model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @NonNull
    private Long scheduledSessionId;
    @NonNull
    private Long participantId;
    @NonNull
    private Long levelId;
    @NonNull
    private Long programId;
    private Date startTime;
    @Setter(AccessLevel.NONE)
    private Date created;
}
