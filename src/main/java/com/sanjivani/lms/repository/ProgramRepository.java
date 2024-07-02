package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.VolunteerEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {

    @NonNull
    Page<ProgramEntity> findAll(@NonNull Pageable page);

    Optional<ProgramEntity> findByName(String name);

    @NonNull
    Page<ProgramEntity> findAllByIncharge(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<ProgramEntity> findAllByPreacher(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<ProgramEntity> findAllByMentor(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<ProgramEntity> findAllByCoordinator(@NonNull VolunteerEntity volunteerEntity, @NonNull Pageable page);

    @NonNull
    Page<ProgramEntity> findAllByLocation(@NonNull String location, @NonNull Pageable page);
}
