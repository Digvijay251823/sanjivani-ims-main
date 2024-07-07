package com.sanjivani.lms.entity;
import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
