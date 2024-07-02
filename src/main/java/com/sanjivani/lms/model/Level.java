package com.sanjivani.lms.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Positive
    private Integer number;

    @NonNull
    private String name;

    private String displayName;

    private String description;

    private String programName;

    @NonNull
    private Long programId;

    private Long preacher1;
    private Long preacher2;
    private Long mentor;
    private Long coordinator;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String status = "ACTIVE";

    private String attendanceUrl;
    private String posterUrl;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean acceptingNewParticipants = false;
    private DayOfWeek sessionDay;
    private LocalTime sessionTime;
    private Date expectedStartDate;
    private Date actualStartDate;
    private Date expectedEndDate;
    private Date actualEndDate;
    private String createdBy;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;

}
