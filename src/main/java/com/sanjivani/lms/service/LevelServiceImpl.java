package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.interfaces.LevelService;
import com.sanjivani.lms.model.*;
import com.sanjivani.lms.repository.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {
    private static final Logger LOG = LoggerFactory.getLogger(LevelServiceImpl.class);

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ScheduledSessionRepository scheduledSessionRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Override
    public Page<Level> getLevels(Pageable pageable) {
        Page<LevelEntity> pagedResult = levelRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Level> levelPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::levelFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, levelPage);
            return levelPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public void save(@NonNull Level level) throws IllegalArgumentException {
        Integer number = level.getNumber();
        if(number < 0)
            throw new IllegalArgumentException("number cannot be negative");

        String name = level.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("name cannot be empty");

        String displayName = level.getDisplayName();

        String description = level.getDescription();

        Long programId = level.getProgramId();
        if(programId <= 0L)
            throw new IllegalArgumentException("programId cannot be negative");
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        if(null == programEntity)
            throw new IllegalArgumentException("program not found");

        Long preacher1 = level.getPreacher1();;
        VolunteerEntity preacher1Entity = null;
        if(preacher1 > 0L)
            preacher1Entity = volunteerRepository.findById(preacher1).orElse(null);

        Long preacher2 = level.getPreacher2();
        VolunteerEntity preacher2Entity = null;
        if(preacher2 > 0L)
            preacher2Entity = volunteerRepository.findById(preacher2).orElse(null);

        Long mentor = level.getMentor();
        VolunteerEntity mentorEntity = null;
        if(mentor > 0L)
            mentorEntity = volunteerRepository.findById(mentor).orElse(null);

        Long coordinator = level.getCoordinator();
        VolunteerEntity coordinatorEntity = null;
        if(coordinator > 0L)
            coordinatorEntity = volunteerRepository.findById(coordinator).orElse(null);

        String status = level.getStatus();
        if(status.isEmpty())
            throw new IllegalArgumentException("status cannot be empty");
        LevelStatus levelStatus = null;
        try {
            levelStatus = LevelStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid audience type: " + status);
            throw new IllegalArgumentException("Invalid audience type: " + status);
        }

        String attendanceUrl = level.getAttendanceUrl();
        String posterUrl = level.getPosterUrl();
        Boolean acceptingNewParticipants = level.getAcceptingNewParticipants();
        DayOfWeek sessionDay = level.getSessionDay();
        LocalTime sessionTime = level.getSessionTime();
        Date expectedStartDate = level.getExpectedStartDate();
        Date actualStartDate = level.getActualStartDate();
        Date expectedEndDate = level.getExpectedEndDate();
        Date actualEndDate = level.getActualEndDate();
        String createdBy;
        if(null == level.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = level.getCreatedBy();

        levelRepository.save(LevelEntity.builder()
                        .number(number)
                        .name(name)
                        .displayName(displayName)
                        .description(description)
                        .program(programEntity)
                        .preacher1(preacher1Entity)
                        .preacher2(preacher2Entity)
                        .mentor(mentorEntity)
                        .coordinator(coordinatorEntity)
                        .status(levelStatus)
                        .attendanceUrl(attendanceUrl)
                        .posterUrl(posterUrl)
                        .acceptingNewParticipants(acceptingNewParticipants)
                        .sessionDay(sessionDay)
                        .sessionTime(sessionTime)
                        .expectedStartDate(expectedStartDate)
                        .actualStartDate(actualStartDate)
                        .expectedEndDate(expectedEndDate)
                        .actualEndDate(actualEndDate)
                        .createdBy(createdBy)
                        .build());
    }

    @Override
    public Level getLevelById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<LevelEntity> levelEntity = levelRepository.findById(id);
        return levelEntity.map(this::levelFromEntity).orElse(null);
    }

    @Override
    public Page<Level> getLevelsByProgram(@NonNull String programName, Pageable pageable) {
        if(programName.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
        ProgramEntity programEntity = programRepository.findByName(programName).orElse(null);
        if(null == programEntity)
            return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0

        Page<LevelEntity> pagedResult = levelRepository.findAllByProgram(programEntity, pageable);
        if (pagedResult.hasContent()) {
            Page<Level> levelPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::levelFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, levelPage);
            return levelPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public Page<Level> getLevelsByPreacher1(Long preacher1, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Level> getLevelByPreacher2(Long preacher2, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Level> getLevelsByMentor(Long Mentor, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Level> getLevelsByCoordinator(Long coordinator, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Level> getLevelsByStatus(@NonNull String status, Pageable pageable) {
        if(status.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
        LevelStatus levelStatus = null;
        try {
            levelStatus = LevelStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid Level Status: " + status);
            return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
        }
        Page<LevelEntity> pagedResult = levelRepository.findAllByStatus(levelStatus, pageable);
        if (pagedResult.hasContent()) {
            Page<Level> levelPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::levelFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, levelPage);
            return levelPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("number"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public Page<Level> getLevelsByAcceptingNewParticipants(@NonNull Boolean acceptingNewParticipants, Pageable pageable) {
        Page<LevelEntity> pagedResult = levelRepository.findAllByAcceptingNewParticipants(acceptingNewParticipants, pageable);
        if (pagedResult.hasContent()) {
            Page<Level> levelPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::levelFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, levelPage);
            return levelPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("name"))); // Regression Spring Boot 3.2.0
    }


    private Level levelFromEntity(@NonNull  LevelEntity levelEntity) {
        Long id = levelEntity.getId();
        if( id <= 0L)
            return null;

        Integer number = levelEntity.getNumber();
        if(number < 0)
            return null;

        String name = levelEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String displayName = levelEntity.getDisplayName();

        String description = levelEntity.getDescription();

        ProgramEntity programEntity = levelEntity.getProgram();
        if(null == programEntity)
            return null;
        Long programId = programEntity.getId();
        if(programId <= 0L)
            return null;
        String programName = programEntity.getName();
        if(null == programName || programName.isEmpty())
            return null;

        VolunteerEntity preacher1Entity = levelEntity.getPreacher1();
        Long preacher1 = 0L;
        if(null != preacher1Entity)
            preacher1 = preacher1Entity.getId();

        VolunteerEntity preacher2Entity = levelEntity.getPreacher1();
        Long preacher2 = 0L;
        if(null != preacher2Entity)
            preacher2 = preacher2Entity.getId();

        VolunteerEntity mentorEntity = levelEntity.getMentor();
        Long mentor = 0L;
        if(null != mentorEntity)
            mentor = mentorEntity.getId();

        VolunteerEntity coordinatorEntity = levelEntity.getCoordinator();
        Long coordinator = 0L;
        if(null != coordinatorEntity)
            coordinator = coordinatorEntity.getId();

        LevelStatus levelStatus = levelEntity.getStatus();
        if(null == levelStatus)
            return null;
        String status = levelStatus.toString();

        String attendanceUrl = levelEntity.getAttendanceUrl();
        String posterUrl = levelEntity.getPosterUrl();
        Boolean acceptingNewParticipants = levelEntity.getAcceptingNewParticipants();
        DayOfWeek sessionDay = levelEntity.getSessionDay();
        LocalTime sessionTime = levelEntity.getSessionTime();
        Date expectedStartDate = levelEntity.getExpectedStartDate();
        Date actualStartDate = levelEntity.getActualStartDate();
        Date expectedEndDate = levelEntity.getExpectedEndDate();
        Date actualEndDate = levelEntity.getActualEndDate();
        String createdBy;
        if(null == levelEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = levelEntity.getCreatedBy();
        Date created = levelEntity.getCreated();
        Date modified = levelEntity.getModified();

        return Level.builder()
                .id(id)
                .number(number)
                .name(name)
                .displayName(displayName)
                .description(description)
                .programId(programId)
                .programName(programName)
                .preacher1(preacher1)
                .preacher2(preacher2)
                .mentor(mentor)
                .coordinator(coordinator)
                .status(status)
                .attendanceUrl(attendanceUrl)
                .posterUrl(posterUrl)
                .acceptingNewParticipants(acceptingNewParticipants)
                .sessionDay(sessionDay)
                .sessionTime(sessionTime)
                .expectedStartDate(expectedStartDate)
                .actualStartDate(actualStartDate)
                .expectedEndDate(expectedEndDate)
                .actualEndDate(actualEndDate)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
