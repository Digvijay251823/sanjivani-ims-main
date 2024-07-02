package com.sanjivani.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="role_privilege")
public class RolePrivilegeEntity {
    @EmbeddedId
    private RolePrivilegeKey id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    @MapsId("privilegeId")
    @JoinColumn(name = "privilege_id")
    Privilege privilege;
}
