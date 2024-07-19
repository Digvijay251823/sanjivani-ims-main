package com.sanjivani.lms.model;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

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
