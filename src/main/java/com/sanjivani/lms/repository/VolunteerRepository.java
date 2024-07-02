package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantEntity;
import com.sanjivani.lms.entity.VolunteerEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long> {

    @NonNull
    Page<VolunteerEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<VolunteerEntity> findAllByEmail(@NonNull String email, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByFirstName(@NonNull String firstName, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByLastName(@NonNull String lastName, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByInitiatedName(@NonNull String initiatedName, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByWaNumber(@NonNull String waNumber, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByContactNumber(@NonNull String contactNumber, @NonNull Pageable page);
}
