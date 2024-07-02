package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.Privilege;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @NonNull
    Page<Privilege> findAll(@NonNull Pageable page);

    Privilege findByName(String name);

}
