package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.CourseEntity;
import com.sanjivani.lms.entity.LevelEntity;
import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.VolunteerEntity;
import com.sanjivani.lms.enums.LevelStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long> {

    @NonNull
    Page<LevelEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByProgram(@NonNull ProgramEntity programEntity, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByPreacher1(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByPreacher2(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByMentor(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByCoordinator(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByStatus(@NonNull LevelStatus levelStatus, @NonNull Pageable page);

    @NonNull
    Page<LevelEntity> findAllByAcceptingNewParticipants(@NonNull Boolean acceptingNewParticipants, @NonNull Pageable page);
}
