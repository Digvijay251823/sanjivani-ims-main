package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.RSVPOption;
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

    @Column(nullable = false)
    private RSVPOption rsvp;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
