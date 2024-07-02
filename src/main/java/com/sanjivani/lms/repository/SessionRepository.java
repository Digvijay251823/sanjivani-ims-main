package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.CourseEntity;
import com.sanjivani.lms.entity.SessionEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    @NonNull
    Page<SessionEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<SessionEntity> findAllByCourse(@NonNull CourseEntity courseEntity, @NonNull Pageable page);

    Optional<SessionEntity> findByCode(String code);
}
