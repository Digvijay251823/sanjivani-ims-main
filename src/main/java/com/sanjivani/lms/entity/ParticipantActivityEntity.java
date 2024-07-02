package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="participant_activity",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueRegistryEntry", columnNames = { "program_id", "participant_id", "activity_id", "scheduled_session_id" }) },
        indexes = {@Index(name = "activityDateIndex", columnList = "activity_date"),
                @Index(name = "programNameIndex", columnList = "program_name"),
                @Index(name = "courseCodeIndex", columnList = "course_code"),
                @Index(name = "levelNameIndex", columnList = "level_name"),
                @Index(name = "scheduledSessionNameIndex", columnList = "scheduled_session_name"),
                @Index(name = "activityNameIndex", columnList = "activity_name"),
                @Index(name = "contactNumberIndex", columnList = "participant_contact_number"),
                @Index(name = "participantFirstNameIndex", columnList = "participant_first_name"),
                @Index(name = "participantLastNameIndex", columnList = "participant_last_name")})
public class ParticipantActivityEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String description;

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
    private Long levelId;

    private Integer levelNumber;

    private String levelName;

    private LevelStatus levelStatus;

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
    //Activity
    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
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

    @Column(length = 10)
    private String courseCode;

    private String courseName;

    //RSVP
    private RSVPOption rsvp;

    @Column(nullable = false)
    private String activityDate;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
