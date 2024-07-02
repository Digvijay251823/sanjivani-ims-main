package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.CourseEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @NonNull
    Page<CourseEntity> findAll(@NonNull Pageable page);

    Optional<CourseEntity> findByCode(String code);
}
