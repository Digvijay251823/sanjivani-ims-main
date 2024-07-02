package com.sanjivani.lms.model;

import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRegistration {
    @Setter(AccessLevel.NONE)
    private Long id;

    //Program
    Long programId;
    String programName;
    private String programLocation;
    private ProgramType programType;
    private AudienceType audienceType;

    //Level
    @NonNull
    private Long levelId;
    private Integer levelNumber;
    private String levelName;
    private String levelDisplayName;
    private LevelStatus levelStatus;
    private DayOfWeek levelSessionDay;
    private LocalTime levelSessionTime;

    //Participant
    @NonNull
    private Long participantId;
    private String participantFirstName;
    private String participantLastName;
    private String participantWaNumber;
    private String participantContactNumber;
    private String participantEmail;
    private Integer participantAge;
    private Gender participantGender;
    private String participantAddress;
    private String participantCity;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}
