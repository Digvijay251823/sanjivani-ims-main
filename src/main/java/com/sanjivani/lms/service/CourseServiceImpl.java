package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.CourseEntity;
import com.sanjivani.lms.interfaces.CourseService;
import com.sanjivani.lms.model.Course;
import com.sanjivani.lms.repository.CourseRepository;
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
public class CourseServiceImpl implements CourseService {
    private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;
    @Override
    public Page<Course> getCourses(Pageable pageable) {
        Page<CourseEntity> pagedResult = courseRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Course> coursePage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::courseFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, coursePage);
            return coursePage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("code"))); // Regression Spring Boot 3.2.0

    }

    @Override
    public void save(@NonNull Course course) throws IllegalArgumentException {
        String code = course.getCode();
        if(code.isEmpty())
            throw new IllegalArgumentException("Code cannot be null or empty");
        String name = course.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        String description = course.getDescription();
        String createdBy;
        if(null == course.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = course.getCreatedBy();

        courseRepository.save(CourseEntity.builder()
                .code(code)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .build());
    }

    @Override
    public Course getCourseByCode(@NonNull String code) {
        if(code.isEmpty())
            return null;
        Optional<CourseEntity> courseEntity = courseRepository.findByCode(code);
        return courseEntity.map(this::courseFromEntity).orElse(null);
    }

    private Course courseFromEntity(@NonNull CourseEntity courseEntity) {
        Long id = courseEntity.getId();
        if( id <= 0L)
            return null;

        String code = courseEntity.getCode();
        if(null == code || code.isEmpty())
            return null;

        String name = courseEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String description = courseEntity.getDescription();

        String createdBy;
        if(null == courseEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = courseEntity.getCreatedBy();

        Date created = courseEntity.getCreated();
        Date modified = courseEntity.getModified();

        return Course.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
