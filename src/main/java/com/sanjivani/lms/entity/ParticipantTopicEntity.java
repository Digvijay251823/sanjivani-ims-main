package com.sanjivani.lms.entity;

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
@Immutable
@Table(name="participant_topic")
public class ParticipantTopicEntity implements Serializable {

    @EmbeddedId
    private ParticipantTopicKey id;

    @ManyToOne(optional = false)
    @MapsId("participant")
    @JoinColumn(name="participant")
    private ParticipantEntity participant;

    @ManyToOne(optional = false)
    @MapsId("topic")
    @JoinColumn(name="topic")
    private TopicEntity topic;

    @CreationTimestamp
    private Date created;

}
