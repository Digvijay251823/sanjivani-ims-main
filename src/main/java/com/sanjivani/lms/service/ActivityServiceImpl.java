package com.sanjivani.lms.service;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sanjivani.lms.entity.ActivityEntity;
import com.sanjivani.lms.interfaces.ActivityService;
import com.sanjivani.lms.model.Activity;
import com.sanjivani.lms.repository.ActivityRepository;

import lombok.NonNull;

@Service
public class ActivityServiceImpl implements ActivityService {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityRepository activityRepository;
    @Override
    public Page<Activity> getActivities(Pageable pageable) {
        Page<ActivityEntity> pagedResult = activityRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Activity> activityPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::activityFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, activityPage);
            return activityPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("name"))); // Regression Spring Boot 3.2.0

    }

    @Override
    public void save(@NonNull Activity activity) throws IllegalArgumentException {
        String name = activity.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        String description = activity.getDescription();
        String createdBy;
        if(activity.getCreatedBy() ==null ){
            createdBy = "SYSTEM";
        }else{
            createdBy = activity.getCreatedBy();}

        activityRepository.save(ActivityEntity.builder()
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .build());
    }

    @Override
    public Activity getActivityByName(@NonNull String name) {
        if(name.isEmpty())
            return null;
        Optional<ActivityEntity> activityEntity = activityRepository.findByName(name);
        return activityEntity.map(this::activityFromEntity).orElse(null);
    }

    @Override
    public Activity getActivityById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<ActivityEntity> activityEntity = activityRepository.findById(id);
        return activityEntity.map(this::activityFromEntity).orElse(null);
    }

    private Activity activityFromEntity(@NonNull ActivityEntity activityEntity) {
        Long id = activityEntity.getId();
        if( id <= 0L)
            return null;

        String name = activityEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String description = activityEntity.getDescription();

        String createdBy;
        if(null == activityEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = activityEntity.getCreatedBy();
        Date created = activityEntity.getCreated();
        Date modified = activityEntity.getModified();

        return Activity.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
