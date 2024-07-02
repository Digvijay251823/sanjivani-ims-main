package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.LevelStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name="scheduled_level_sessions",
        indexes = @Index(name = "StartTimeIndex", columnList = "start_time"))
public class ScheduledSessionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="session")
    private SessionEntity session;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="level")
    private LevelEntity level;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "program")
    private ProgramEntity program;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

}
