package com.sanjivani.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="privilege", indexes = @Index(name = "UniquePrivilegeNameIndex", columnList = "name", unique = true))
public class Privilege implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "privilege")
    private Collection<RolePrivilegeEntity> rolePrivileges;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
