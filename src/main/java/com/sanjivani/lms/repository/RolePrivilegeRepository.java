package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.RolePrivilegeEntity;
import com.sanjivani.lms.entity.RolePrivilegeKey;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilegeEntity, RolePrivilegeKey> {

    @NonNull
    Page<RolePrivilegeEntity> findAll(@NonNull Pageable page);

}
