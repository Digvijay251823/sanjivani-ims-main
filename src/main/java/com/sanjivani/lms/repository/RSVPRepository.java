package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.AttendanceKey;
import com.sanjivani.lms.entity.RSVPEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RSVPRepository extends JpaRepository<RSVPEntity, AttendanceKey> {

    @NonNull
    Page<RSVPEntity> findAll(@NonNull Pageable page);
    @NonNull
    Page<RSVPEntity> findByIdScheduledSessionId(@NonNull Long scheduledSessionId, @NonNull Pageable page);

}
