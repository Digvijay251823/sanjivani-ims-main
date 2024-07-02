package com.sanjivani.lms.entity;

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
    private Boolean earlyJapaRoundsBefore8AM;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean earlyJapaRoundsAfter8AM;

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

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}