package com.sanjivani.lms.entity;

import com.sanjivani.lms.enums.Gender;
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
@Table(name="volunteers", indexes = { @Index(name = "UniqueVolunteerContactIndex", columnList = "contact_number", unique = true)
        , @Index(name = "UniqueVolunteerEmailIndex", columnList = "email", unique = true) })
public class VolunteerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String initiatedName;

    @Column(length = 10)
    private String waNumber;

    @Column(length = 10)
    private String contactNumber;

    @Column(nullable = false)
    private String email;

    private Integer age;

    private Gender gender;

    private String address;

    private String serviceInterests;

    private String currentServices;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
