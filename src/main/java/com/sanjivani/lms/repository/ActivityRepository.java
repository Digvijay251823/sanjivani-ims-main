package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ActivityEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    @NonNull
    Page<ActivityEntity> findAll(@NonNull Pageable page);

    Optional<ActivityEntity> findByName(String name);
}
