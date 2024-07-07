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
import jakarta.persistence.OneToOne;
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
@Table(name="sadhana_form")
public class SadhanaFormEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "sadhanaForm", fetch = FetchType.EAGER, optional = false)
    private ProgramEntity program;

    //Sadhana
    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean numberOfRounds;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean first8RoundsCompletedTime;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean next8RoundsCompletedTime;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean wakeUpTime;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean sleepTime;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean prabhupadaBookReading;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean nonPrabhupadaBookReading;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean prabhupadaClassHearing;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean guruClassHearing;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean otherClassHearing;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean speaker;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean attendedArti;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean mobileInternetUsage;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean topic;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean visibleSadhana;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}