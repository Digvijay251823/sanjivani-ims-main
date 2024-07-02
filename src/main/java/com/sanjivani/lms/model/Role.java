package com.sanjivani.lms.model;

import com.sanjivani.lms.entity.RolePrivilegeEntity;
import com.sanjivani.lms.entity.UserRoleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private String name;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}
