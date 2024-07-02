package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.interfaces.SessionService;
import com.sanjivani.lms.model.ScheduledSession;
import com.sanjivani.lms.model.Session;
import com.sanjivani.lms.repository.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {
    private static final Logger LOG = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private ScheduledSessionRepository scheduledSessionRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Override
    public Page<Session> getSessions(Pageable pageable) {
        Page<SessionEntity> pagedResult = sessionRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Session> sessionPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::sessionFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, sessionPage);
            return sessionPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("code"))); // Regression Spring Boot 3.2.0

    }

    @Override
    public void save(@NonNull Session session) throws IllegalArgumentException {
        String code = session.getCode();
        if(code.isEmpty())
            throw new IllegalArgumentException("Code cannot be null");
        String name = session.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        String description = session.getDescription();
        String courseCode = session.getCourseCode();
        if(courseCode.isEmpty())
            throw new IllegalArgumentException("Course Code cannot be null or empty");
        CourseEntity courseEntity = courseRepository.findByCode(courseCode).orElse(null);
        if(null == courseEntity)
            throw new IllegalArgumentException("Course Code does not exist");
        Integer durationInMinutes = session.getDurationInMinutes();
        if(null == durationInMinutes || durationInMinutes <= 0)
            throw new IllegalArgumentException("Duration cannot be Zero or Negative");

        String  videoUrl = session.getVideoUrl();
        String  presentationUrl = session.getPresentationUrl();
        String notesUrl = session.getNotesUrl();
        String createdBy;
        if(null == session.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = session.getCreatedBy();

        sessionRepository.save(SessionEntity.builder()
                .code(code)
                .name(name)
                .description(description)
                .course(courseEntity)
                .durationInMinutes(durationInMinutes)
                .videoUrl(videoUrl)
                .presentationUrl(presentationUrl)
                .notesUrl(notesUrl)
                .createdBy(createdBy)
                .build());
    }

    @Override
    public Page<Session> getSessionsByCourse(@NonNull String courseCode, Pageable pageable) {
        if(courseCode.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("code"))); // Regression Spring Boot 3.2.0
        CourseEntity courseEntity = courseRepository.findByCode(courseCode).orElse(null);
        if(null == courseEntity)
            return Page.empty(PageRequest.of(0, 10, Sort.by("code"))); // Regression Spring Boot 3.2.0
        Page<SessionEntity> pagedResult = sessionRepository.findAllByCourse(courseEntity, pageable);
        if (pagedResult.hasContent()) {
            Page<Session> sessionPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::sessionFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, sessionPage);
            return sessionPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("code"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public Session getSessionById(Long id) {
        if(id <= 0L)
            return null;
        Optional<SessionEntity> sessionEntity = sessionRepository.findById(id);
        return sessionEntity.map(this::sessionFromEntity).orElse(null);
    }

    @Override
    public Session getSessionByCode(@NonNull String code) {
        if(code.isEmpty())
            return null;
        Optional<SessionEntity> sessionEntity = sessionRepository.findByCode(code);
        return sessionEntity.map(this::sessionFromEntity).orElse(null);
    }

    @Override
    public void schedule(@NonNull ScheduledSession scheduledSession) throws IllegalArgumentException {
        Long sessionId = scheduledSession.getSessionId();
        if(sessionId <= 0L)
            throw new IllegalArgumentException("Session Id cannot be Zero or Negative");
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElse(null);
        if(null == sessionEntity)
            throw new IllegalArgumentException("Session Id does not exist");

        Long levelId = scheduledSession.getLevelId();
        if(levelId <= 0L)
            throw new IllegalArgumentException("Level Id cannot be Zero or Negative");
        LevelEntity levelEntity = levelRepository.findById(levelId).orElse(null);
        if(null == levelEntity)
            throw new IllegalArgumentException("Level Id does not exist");
        ProgramEntity programEntity = levelEntity.getProgram();
        if(null == programEntity)
            throw new IllegalArgumentException("Program Entity does not exist in level entity");

        String name = scheduledSession.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");

        Date startTime = scheduledSession.getStartTime();
        if(null == startTime)
            throw new IllegalArgumentException("Start cannot be null");
        String createdBy;
        if(null == scheduledSession.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = scheduledSession.getCreatedBy();

        scheduledSessionRepository.save(ScheduledSessionEntity.builder()
                        .name(name)
                        .session(sessionEntity)
                        .level(levelEntity)
                        .program(programEntity)
                        .startTime(startTime)
                        .createdBy(createdBy)
                        .build());
    }

    @Override
    public ScheduledSession getScheduledSession(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<ScheduledSessionEntity> sessionEntity = scheduledSessionRepository.findById(id);
        return sessionEntity.map(this::scheduledSessionFromEntity).orElse(null);
    }

    @Override
    public Page<ScheduledSession> getScheduledSessionsByLevel(@NonNull Long id, Pageable pageable) {
        if(id <= 0L)
            return Page.empty(PageRequest.of(0, 10, Sort.by("startTime"))); // Regression Spring Boot 3.2.0
        LevelEntity levelEntity = levelRepository.findById(id).orElse(null);
        if(null == levelEntity)
            return Page.empty(PageRequest.of(0, 10, Sort.by("startTime"))); // Regression Spring Boot 3.2.0
        Page<ScheduledSessionEntity> pagedResult = scheduledSessionRepository.findAllByLevel(levelEntity, pageable);
        if (pagedResult.hasContent()) {
            Page<ScheduledSession> scheduledSessionPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::scheduledSessionFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, scheduledSessionPage);
            return scheduledSessionPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("startTime"))); // Regression Spring Boot 3.2.0
    }

    private Session sessionFromEntity(@NonNull SessionEntity sessionEntity) {
        Long id = sessionEntity.getId();
        if( id <= 0L)
            return null;

        String code = sessionEntity.getCode();
        if(null == code || code.isEmpty())
            return null;

        String name = sessionEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String description = sessionEntity.getDescription();

        CourseEntity courseEntity = sessionEntity.getCourse();
        if(null == courseEntity)
            return null;
        String courseCode = courseEntity.getCode();
        if(null == courseCode || courseCode.isEmpty())
            return null;

        Integer durationInMinutes = sessionEntity.getDurationInMinutes();
        if(null == durationInMinutes || durationInMinutes <= 0)
            return null;

        String  videoUrl = sessionEntity.getVideoUrl();
        String  presentationUrl = sessionEntity.getPresentationUrl();
        String notesUrl = sessionEntity.getNotesUrl();
        String createdBy;
        if(null == sessionEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = sessionEntity.getCreatedBy();
        Date created = sessionEntity.getCreated();
        Date modified = sessionEntity.getModified();
        return Session.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .courseCode(courseCode)
                .durationInMinutes(durationInMinutes)
                .videoUrl(videoUrl)
                .presentationUrl(presentationUrl)
                .notesUrl(notesUrl)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }

    private ScheduledSession scheduledSessionFromEntity(@NonNull ScheduledSessionEntity scheduledSessionEntity) {
        Long id = scheduledSessionEntity.getId();
        if( id <= 0L)
            return null;

        SessionEntity sessionEntity = scheduledSessionEntity.getSession();
        if(null == sessionEntity)
            return null;
        Long sessionId = sessionEntity.getId();
        if( sessionId <= 0L)
            return null;
        String sessionName = sessionEntity.getName();
        if(null == sessionName || sessionName.isEmpty())
            return null;

        CourseEntity courseEntity = sessionEntity.getCourse();
        if(null == courseEntity)
            return null;
        String courseName = courseEntity.getName();
        if(null == courseName || courseName.isEmpty())
            return null;

        LevelEntity levelEntity = scheduledSessionEntity.getLevel();
        if(null == levelEntity)
            return null;
        Long levelId = levelEntity.getId();
        if( levelId <= 0L)
            return null;

        ProgramEntity programEntity = scheduledSessionEntity.getProgram();
        if(null == programEntity)
            return null;
        Long programId = programEntity.getId();
        if( programId <= 0L)
            return null;
        String programName = programEntity.getName();
        if(null == programName || programName.isEmpty())
            return null;

        String name = scheduledSessionEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        Date startTime = scheduledSessionEntity.getStartTime();
        if(null == startTime)
            return null;

        String createdBy;
        if(null == scheduledSessionEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = scheduledSessionEntity.getCreatedBy();

        Date created = scheduledSessionEntity.getCreated();
        Date modified = scheduledSessionEntity.getModified();

        return ScheduledSession.builder()
                .id(id)
                .name(name)
                .courseName(courseName)
                .sessionId(sessionId)
                .sessionName(sessionName)
                .levelId(levelId)
                .programId(programId)
                .programName(programName)
                .startTime(startTime)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
