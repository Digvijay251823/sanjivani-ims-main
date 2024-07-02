package com.sanjivani.lms.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.sanjivani.lms.enums.RSVPOption;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantActivity {
    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;

    //Program
    @NonNull
    private Long programId;

    private String programName;

    private String programLocation;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String programType = "TEMPLE";

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String audienceType = "ALL";

    //Level
    private Long levelId;

    private Integer levelNumber;

    private String levelName;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String levelStatus = "ACTIVE";

    //Participant
    @NonNull
    private Long participantId;

    private String participantFirstName;

    private String participantLastName;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String participantWaNumber;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String participantContactNumber;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Invalid email")
    private String participantEmail;

    //Activity
    @NonNull
    private Long activityId;

    private String activityName;

    private String activityDescription;

    //Session
    private Long scheduledSessionId;

    private String scheduledSessionName;

    private Long sessionId;

    private String sessionCode;

    private String sessionName;

    //Course
    private Long courseId;

    @Pattern(regexp = "^[A-Z0-9]{3,10}$",
            message = "code must be of 3 to 10 length with no special characters")
    private String courseCode;

    private String courseName;

    //RSVP
    private RSVPOption rsvp;

    @NonNull
    private String activityDate;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}
