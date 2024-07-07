package com.sanjivani.lms.entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String topic;
    private String visibleSadhana;

    @Column(nullable = false)
    private String sadhanaDate;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}