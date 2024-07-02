package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.UserRoleEntity;
import com.sanjivani.lms.entity.UserRoleKey;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleKey> {

    @NonNull
    Page<UserRoleEntity> findAll(@NonNull Pageable page);

}
