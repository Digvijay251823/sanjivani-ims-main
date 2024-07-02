package com.sanjivani.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="participant_sadhana",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueSadhanaEntry", columnNames = { "program_id", "participant_id", "sadhana_date" }) },
        indexes = {@Index(name = "sadhanaDateIndex", columnList = "sadhana_date"),
                @Index(name = "sadhanaProgramNameIndex", columnList = "program_name"),
                @Index(name = "sadhanaContactNumberIndex", columnList = "participant_contact_number"),
                @Index(name = "sadhanaParticipantFirstNameIndex", columnList = "participant_first_name"),
                @Index(name = "sadhanaParticipantLastNameIndex", columnList = "participant_last_name")})
public class ParticipantSadhanaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //Program
    @Column(nullable = false)
    Long programId;

    @Column(nullable = false)
    String programName;

    //Participant
    @Column(nullable = false)
    private Long participantId;

    @Column(nullable = false)
    private String participantFirstName;

    @Column(nullable = false)
    private String participantLastName;

    @Column(length = 10, nullable = false)
    private String participantWaNumber;

    @Column(length = 10, nullable = false)
    private String participantContactNumber;

    //Sadhana
    private Integer numberOfRounds;
    private Integer earlyJapaRoundsBefore8AM;
    private Integer earlyJapaRoundsAfter8AM;
    @Column(columnDefinition = "TIME")
    private LocalTime first8RoundsCompletedTime;
    @Column(columnDefinition = "TIME")
    private LocalTime next8RoundsCompletedTime;
    @Column(columnDefinition = "TIME")
    private LocalTime wakeUpTime;
    @Column(columnDefinition = "TIME")
    private LocalTime sleepTime;
    private Integer prabhupadaBookReading;
    private Integer nonPrabhupadaBookReading;
    private Integer prabhupadaClassHearing;
    private Integer guruClassHearing;
    private Integer otherClassHearing;
    private String speaker;
    private Boolean attendedArti;
    private Integer mobileInternetUsage;

    @Column(nullable = false)
    private String sadhanaDate;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}