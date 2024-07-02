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
@Table(name="role", indexes = @Index(name = "UniqueRoleNameIndex", columnList = "name", unique = true))
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private Collection<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "role")
    private Collection<RolePrivilegeEntity> rolePrivileges;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;
}
