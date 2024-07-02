package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.Role;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @NonNull
    Page<Role> findAll(@NonNull Pageable page);

    Role findByName(String name);

}
