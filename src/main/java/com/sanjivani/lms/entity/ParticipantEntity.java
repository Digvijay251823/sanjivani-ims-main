package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.ParticipantStatus;
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
@Table(name="participants",
        indexes = {@Index(name = "UniqueParticipantContactIndex", columnList = "contact_number", unique = true),
                  @Index(name = "JapaRoundsIndex", columnList = "japa_rounds")})
public class ParticipantEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 10, nullable = false)
    private String waNumber;

    @Column(length = 10, nullable = false)
    private String contactNumber;

    private String email;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer japaRounds;

    private Integer age;
    private Gender gender;
    private String address;
    private String city;
    private String maritalStatus;
    private String education;
    private String occupation;
    private String reference;
    @Lob
    private String notes;
    private ParticipantStatus status;
    private Integer numberOfChildren;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

}
