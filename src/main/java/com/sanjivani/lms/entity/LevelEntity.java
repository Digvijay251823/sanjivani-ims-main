package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.LevelStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
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
@Table(name="levels",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndProgram", columnNames = { "number", "program" }) },
        indexes = { @Index(name = "statusIndex", columnList = "status"),
                @Index(name = "anpIndex", columnList = "accepting_new_participants")})
public class LevelEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String name;

    private String displayName;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "program")
    private ProgramEntity program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "preacher1")
    private VolunteerEntity preacher1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "preacher2")
    private VolunteerEntity preacher2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor")
    private VolunteerEntity mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinator")
    private VolunteerEntity coordinator;

    @Column(columnDefinition = "smallint default 0", nullable = false)
    private LevelStatus status;

    private String attendanceUrl;

    private String posterUrl;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean acceptingNewParticipants;

    @Column(columnDefinition = "smallint")
    private DayOfWeek sessionDay;

    @Column(columnDefinition = "TIME")
    private LocalTime sessionTime;

    @Temporal(TemporalType.DATE)
    private Date expectedStartDate;

    @Temporal(TemporalType.DATE)
    private Date actualStartDate;

    @Temporal(TemporalType.DATE)
    private Date expectedEndDate;

    @Temporal(TemporalType.DATE)
    private Date actualEndDate;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

}
