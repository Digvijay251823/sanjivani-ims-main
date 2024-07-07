package com.sanjivani.lms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
