package com.sanjivani.lms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sanjivani.lms.enums.RSVPOption;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
@Table(name="rsvp")
public class RSVPEntity implements Serializable {
    @EmbeddedId
    private AttendanceKey id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId("scheduledSessionId")
    @JoinColumn(name="scheduled_session")
    private ScheduledSessionEntity scheduledSession;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId("participantId")
    @JoinColumn(name = "participant")
    private ParticipantEntity participant;

    @Column(nullable = true)
    private Long membersComming;

    @Column(nullable = false)
    private RSVPOption rsvp;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
