package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="participant_registration",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueRegistration", columnNames = { "level_id", "participant_id" }) },
        indexes = { @Index(name = "pgNameIndex", columnList = "program_name"),
                @Index(name = "lvlNameIndex", columnList = "level_name"),
                @Index(name = "lvlDisplayNameIndex", columnList = "level_display_name"),
                @Index(name = "contNumberIndex", columnList = "participant_contact_number"),
                @Index(name = "pFirstNameIndex", columnList = "participant_first_name"),
                @Index(name = "pLastNameIndex", columnList = "participant_last_name")})
public class ParticipantRegistrationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //Program
    @Column(nullable = false)
    Long programId;

    @Column(nullable = false)
    String programName;

    @Column(nullable = false)
    private String programLocation;

    @Column(columnDefinition = "smallint default 0", nullable = false)
    private ProgramType programType;

    @Column(columnDefinition = "smallint default 0", nullable = false)
    private AudienceType audienceType;

    //Level
    @Column(nullable = false)
    private Long levelId;

    @Column(nullable = false)
    private Integer levelNumber;

    @Column(nullable = false)
    private String levelName;

    private String levelDisplayName;

    @Column(nullable = false)
    private LevelStatus levelStatus;

    @Column(columnDefinition = "smallint")
    private DayOfWeek levelSessionDay;

    @Column(columnDefinition = "TIME")
    private LocalTime levelSessionTime;

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

    private String participantEmail;
    private Integer participantAge;
    private Gender participantGender;
    private String participantAddress;
    private String participantCity;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
