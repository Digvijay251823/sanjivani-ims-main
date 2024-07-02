package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.LevelEntity;
import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.ScheduledSessionEntity;
import com.sanjivani.lms.entity.SessionEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduledSessionRepository extends JpaRepository<ScheduledSessionEntity, Long> {

    @NonNull
    Page<ScheduledSessionEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<ScheduledSessionEntity> findAllByLevel(LevelEntity level, @NonNull Pageable page);

    @NonNull
    Page<ScheduledSessionEntity> findAllByProgram(ProgramEntity program, @NonNull Pageable page);

    @NonNull
    Page<ScheduledSessionEntity> findAllBySession(SessionEntity session, @NonNull Pageable page);

}
