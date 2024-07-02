package com.sanjivani.lms.model;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantSadhana {
    @Setter(AccessLevel.NONE)
    private Long id;

    //Program
    @NonNull
    Long programId;

    String programName;

    //Participant
    @NonNull
    private Long participantId;

    private String participantFirstName;

    private String participantLastName;

    private String participantWaNumber;

    private String participantContactNumber;

    //Sadhana
    private Integer numberOfRounds;
    private Integer earlyJapaRoundsBefore8AM;
    private Integer earlyJapaRoundsAfter8AM;
    private LocalTime first8RoundsCompletedTime;
    private LocalTime next8RoundsCompletedTime;
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private Integer prabhupadaBookReading;
    private Integer nonPrabhupadaBookReading;
    private Integer prabhupadaClassHearing;
    private Integer guruClassHearing;
    private Integer otherClassHearing;
    private String speaker;
    private Boolean attendedArti;
    private Integer mobileInternetUsage;

    @NonNull
    private String sadhanaDate;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}
