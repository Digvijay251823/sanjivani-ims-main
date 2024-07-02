package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.ProgramType;
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
@Table(name="programs", indexes = @Index(name = "progNameIndex", columnList = "name", unique = true))
public class ProgramEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "incharge")
    private VolunteerEntity incharge;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "preacher")
    private VolunteerEntity preacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor")
    private VolunteerEntity mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinator")
    private VolunteerEntity coordinator;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sadhana_form")
    private SadhanaFormEntity sadhanaForm;

    @Column(columnDefinition = "smallint default 0", nullable = false)
    private ProgramType type;

    @Column(columnDefinition = "smallint default 0", nullable = false)
    private AudienceType audienceType;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

}
