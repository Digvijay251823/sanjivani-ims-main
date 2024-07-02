package com.sanjivani.lms.repository;

import com.sanjivani.lms.entity.TopicEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    @NonNull
    Page<TopicEntity> findAll(@NonNull Pageable page);

    Optional<TopicEntity> findByName(String name);
}
