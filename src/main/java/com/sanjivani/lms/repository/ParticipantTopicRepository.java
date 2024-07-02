package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.ParticipantTopicEntity;
import com.sanjivani.lms.entity.ParticipantTopicKey;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParticipantTopicRepository extends JpaRepository<ParticipantTopicEntity, ParticipantTopicKey> {

    @NonNull
    List<ParticipantTopicEntity> findByIdParticipant(@NonNull Long id);

    @NonNull
    List<ParticipantTopicEntity> findByIdTopic(@NonNull Long id);

}
