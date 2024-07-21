package com.sanjivani.lms.model;
import java.time.LocalTime;
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
    private LocalTime first8RoundsCompletedTime;
    private LocalTime next8RoundsCompletedTime;
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private Integer prabhupadaBookReading;
    private String nonPrabhupadaBookReading;
    private Integer prabhupadaClassHearing;
    private Integer guruClassHearing;
    private Integer otherClassHearing;
    private String speaker;
    private Boolean attendedArti;
    private Integer mobileInternetUsage;
    private String topic;
    private String visibleSadhana;


    @NonNull
    private String sadhanaDate;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}
