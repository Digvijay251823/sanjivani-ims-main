package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.SadhanaFormEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SadhanaFormRepository extends JpaRepository<SadhanaFormEntity, Long> {

    @NonNull
    Page<SadhanaFormEntity> findAll(@NonNull Pageable page);
}
