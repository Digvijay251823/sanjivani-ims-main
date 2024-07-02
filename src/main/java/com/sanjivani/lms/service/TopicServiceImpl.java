package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.TopicEntity;
import com.sanjivani.lms.interfaces.TopicService;
import com.sanjivani.lms.model.Topic;
import com.sanjivani.lms.repository.TopicRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    private static final Logger LOG = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private TopicRepository topicRepository;
    @Override
    public Page<Topic> getTopics(Pageable pageable) {
        Page<TopicEntity> pagedResult = topicRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Topic> topicPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::topicFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, topicPage);
            return topicPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("name"))); // Regression Spring Boot 3.2.0

    }

    @Override
    public void save(@NonNull Topic topic) throws IllegalArgumentException {
        String name = topic.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        String description = topic.getDescription();
        String createdBy;
        if(null == topic.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = topic.getCreatedBy();

        topicRepository.save(TopicEntity.builder()
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .build());
    }

    @Override
    public Topic getTopicByName(@NonNull String name) {
        if(name.isEmpty())
            return null;
        Optional<TopicEntity> topicEntity = topicRepository.findByName(name);
        return topicEntity.map(this::topicFromEntity).orElse(null);
    }

    @Override
    public Topic getTopicById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<TopicEntity> topicEntity = topicRepository.findById(id);
        return topicEntity.map(this::topicFromEntity).orElse(null);
    }

    private Topic topicFromEntity(@NonNull TopicEntity topicEntity) {
        Long id = topicEntity.getId();
        if( id <= 0L)
            return null;

        String name = topicEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String description = topicEntity.getDescription();

        String createdBy;
        if(null == topicEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = topicEntity.getCreatedBy();

        Date created = topicEntity.getCreated();
        Date modified = topicEntity.getModified();

        return Topic.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
