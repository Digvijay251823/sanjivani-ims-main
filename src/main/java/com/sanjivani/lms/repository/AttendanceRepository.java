package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.AttendanceEntity;
import com.sanjivani.lms.entity.AttendanceKey;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, AttendanceKey> {

    @NonNull
    Page<AttendanceEntity> findAll(@NonNull Pageable page);

    @NonNull
    Page<AttendanceEntity> findByIdScheduledSessionId(@NonNull Long scheduledSessionId, @NonNull Pageable page);

}
