package com.sanjivani.lms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledSession {
    @Setter(AccessLevel.NONE)
    private Long id;
    @NonNull
    private String name;
    private String courseName;
    @NonNull
    private Long sessionId;
    private String sessionName;
    @NonNull
    private Long levelId;
    @NonNull
    private Long programId;
    private String programName;
    private Date startTime;
    private String createdBy;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;

}
