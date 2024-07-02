package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantActivityEntity;
import com.sanjivani.lms.entity.ParticipantEntity;
import com.sanjivani.lms.interfaces.JapaRoundsParticipantCountInterface;
import com.sanjivani.lms.model.JapaRoundsParticipantCount;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {

    @NonNull
    Page<ParticipantEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByEmail(@NonNull String email, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByFirstName(@NonNull String firstName, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByLastName(@NonNull String lastName, @NonNull Pageable page);

    @NonNull
    Page<ParticipantEntity> findAllByWaNumber(@NonNull String waNumber, @NonNull Pageable page);

    @NonNull
    Optional<ParticipantEntity> findByContactNumber(@NonNull String contactNumber);

    @NonNull
    Page<ParticipantEntity> findAllByJapaRounds(@NonNull Integer JapaRounds, @NonNull Pageable page);

    @Modifying
    @Query(value = "UPDATE PARTICIPANTS SET JAPA_ROUNDS = ?2 WHERE ID = ?1",
            nativeQuery = true)
    void updateJapaRounds(@NonNull Long id, @NonNull  Integer value);

    @Query(value = "SELECT CONCAT(japa_rounds, ' Rounds') AS japaRounds, count(*) AS participantCount FROM participants GROUP BY japa_rounds ORDER BY japa_rounds",
            nativeQuery = true)
    @NonNull
    List<JapaRoundsParticipantCountInterface> findGroupByCountOfJapaRounds();
}
