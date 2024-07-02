package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import com.sanjivani.lms.interfaces.AttendanceService;
import com.sanjivani.lms.model.Attendance;
import com.sanjivani.lms.repository.*;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private static final Logger LOG = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ParticipantActivityRepository participantActivityRepository;

    @Autowired
    private ScheduledSessionRepository scheduledSessionRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    @Transactional
    public void save(@NonNull Attendance attendance) throws IllegalArgumentException {
        Long scheduledSessionId = attendance.getScheduledSessionId();
        if (scheduledSessionId <= 0)
            throw new IllegalArgumentException("Scheduled Session Id cannot be less than or equal to 0");
        ScheduledSessionEntity scheduledSessionEntity = scheduledSessionRepository.findById(scheduledSessionId).orElse(null);
        if(scheduledSessionEntity == null)
            throw new IllegalArgumentException("Attendance cannot be marked for non-existing scheduled session");
        String scheduledSessionName = scheduledSessionEntity.getName();
        if(null == scheduledSessionName || scheduledSessionName.isEmpty())
            throw new IllegalArgumentException("Scheduled session name cannot be empty");
        LevelEntity levelEntity = scheduledSessionEntity.getLevel();
        if(null == levelEntity)
            throw new IllegalArgumentException("Level not found for scheduled session");
        Long levelId = levelEntity.getId();
        if(levelId <= 0)
            throw new IllegalArgumentException("Level Id cannot be less than or equal to 0");
        Integer levelNumber = levelEntity.getNumber();
        if(levelNumber <= 0)
            throw new IllegalArgumentException("Level number cannot be less than or equal to 0");
        String levelName = levelEntity.getName();
        if(null == levelName || levelName.isEmpty())
            throw new IllegalArgumentException("Level name cannot be empty");
        LevelStatus levelStatus = levelEntity.getStatus();
        if(null == levelStatus)
            throw new IllegalArgumentException("Level status cannot be empty");
        SessionEntity sessionEntity = scheduledSessionEntity.getSession();
        if(null == sessionEntity)
            throw new IllegalArgumentException("Session not found for scheduled session");
        Long sessionId = sessionEntity.getId();
        if(sessionId <= 0)
            throw new IllegalArgumentException("Session Id cannot be less than or equal to 0");
        String sessionName = sessionEntity.getName();
        if(null == sessionName || sessionName.isEmpty())
            throw new IllegalArgumentException("Session name cannot be empty");
        String sessionCode = sessionEntity.getCode();
        if(null == sessionCode || sessionCode.isEmpty())
            throw new IllegalArgumentException("Session code cannot be empty");
        CourseEntity courseEntity = sessionEntity.getCourse();
        if(null == courseEntity)
            throw new IllegalArgumentException("Course not found for session");
        Long courseId = courseEntity.getId();
        if(courseId <= 0)
            throw new IllegalArgumentException("Course Id cannot be less than or equal to 0");
        String courseName = courseEntity.getName();
        if(null == courseName || courseName.isEmpty())
            throw new IllegalArgumentException("Course name cannot be empty");
        String courseCode = courseEntity.getCode();
        if(null == courseCode || courseCode.isEmpty())
            throw new IllegalArgumentException("Course code cannot be empty");

        Long participantId = attendance.getParticipantId();
        if (participantId <= 0)
            throw new IllegalArgumentException("Participant Id cannot be less than or equal to 0");
        ParticipantEntity participantEntity = participantRepository.findById(participantId).orElse(null);
        if(participantEntity == null)
            throw new IllegalArgumentException("Attendance cannot be marked for non-existing participant");
        String participantFirstName = participantEntity.getFirstName();
        if(null == participantFirstName || participantFirstName.isEmpty())
            throw new IllegalArgumentException("Participant first name cannot be empty");
        String participantLastName = participantEntity.getLastName();
        if(null == participantLastName || participantLastName.isEmpty())
            throw new IllegalArgumentException("Participant last name cannot be empty");
        String participantWaNumber = participantEntity.getWaNumber();
        if(null == participantWaNumber || participantWaNumber.isEmpty())
            throw new IllegalArgumentException("Participant WA number cannot be empty");
        String participantContactNumber = participantEntity.getContactNumber();
        if(null == participantContactNumber || participantContactNumber.isEmpty())
            throw new IllegalArgumentException("Participant contact number cannot be empty");
        String participantEmail = participantEntity.getEmail();

        //Fetch activity with name "Attendance"
        ActivityEntity activityEntity = activityRepository.findByName("Attendance").orElse(null);
        if(null == activityEntity)
            throw new IllegalArgumentException("Activity not found with name 'Attendance', Please add an activity with name 'Attendance' to mark attendance.");

        attendanceRepository.save(AttendanceEntity.builder()
                .id(AttendanceKey.builder()
                        .scheduledSessionId(scheduledSessionId)
                        .participantId(participantId)
                        .build())
                .participant(participantEntity)
                .scheduledSession(scheduledSessionEntity)
                .build());
        // Mark participant activity
        ProgramEntity programEntity = scheduledSessionEntity.getProgram();
        if(null == programEntity)
            throw new IllegalArgumentException("Program not found for scheduled session");
        Long programId = programEntity.getId();
        if(programId <= 0)
            throw new IllegalArgumentException("Program Id cannot be less than or equal to 0");
        String programName = programEntity.getName();
        if(null == programName || programName.isEmpty())
            throw new IllegalArgumentException("Program name cannot be empty");
        String programLocation = programEntity.getLocation();
        if(null == programLocation || programLocation.isEmpty())
            throw new IllegalArgumentException("Program location cannot be empty");
        AudienceType audienceType = programEntity.getAudienceType();
        if(null == audienceType)
            throw new IllegalArgumentException("Audience type cannot be empty");
        ProgramType programType = programEntity.getType();
        if(null == programType)
            throw new IllegalArgumentException("Program type cannot be empty");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Date activityDate = new Date();
        String activityDateStr = formatter.format(activityDate);
        if(activityDateStr.isEmpty())
            throw new IllegalArgumentException("Activity date cannot be empty");
        LOG.info("Formatted String Activity Date: {}", formatter.format(activityDate));

        participantActivityRepository.save(ParticipantActivityEntity.builder()
                .programId(programId)
                .programName(programName)
                .programLocation(programLocation)
                .programType(programType)
                .audienceType(audienceType)
                .levelId(levelId)
                .levelNumber(levelNumber)
                .levelName(levelName)
                .levelStatus(levelStatus)
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
                .participantEmail(participantEmail)
                .activityId(activityEntity.getId())
                .activityName(activityEntity.getName())
                .scheduledSessionId(scheduledSessionId)
                .scheduledSessionName(scheduledSessionName)
                .sessionId(sessionId)
                .sessionName(sessionName)
                .sessionCode(sessionCode)
                .courseId(courseId)
                .courseName(courseName)
                .courseCode(courseCode)
                .activityDate(activityDateStr)
                .build());
    }

    @Override
    public Attendance getAttendanceByParticipantAndSession(@NonNull Long participantId, @NonNull Long scheduledSessionId) {
        AttendanceEntity attendanceEntity = attendanceRepository.findById(AttendanceKey.builder()
                .participantId(participantId)
                .scheduledSessionId(scheduledSessionId)
                .build()).orElse(null);
        if (attendanceEntity == null)
            return null;
        return attendanceFromEntity(attendanceEntity);
    }

    @Override
    public @NonNull Page<Attendance> getAttendanceBySession(@NonNull Long scheduledSessionId, @NonNull Pageable page) {
        Page<AttendanceEntity> pagedResult = attendanceRepository.findByIdScheduledSessionId(scheduledSessionId, page);
        if (pagedResult.hasContent()) {
            Page<Attendance> attendancePage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::attendanceFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, attendancePage);
            return attendancePage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public @NonNull Page<Attendance> getAttendance(@NonNull Pageable page) {
        Page<AttendanceEntity> pagedResult = attendanceRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<Attendance> attendancePage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::attendanceFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, attendancePage);
            return attendancePage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private Attendance attendanceFromEntity(@NonNull AttendanceEntity attendanceEntity) {
        AttendanceKey attendanceKey = attendanceEntity.getId();
        if(null == attendanceKey)
            return null;
        Long scheduledSessionId = attendanceKey.getScheduledSessionId();
        if(scheduledSessionId <= 0)
            return null;
        Long participantId = attendanceKey.getParticipantId();
        if(participantId <= 0)
            return null;
        ScheduledSessionEntity scheduledSessionEntity = attendanceEntity.getScheduledSession();
        if(null == scheduledSessionEntity)
            return null;
        ProgramEntity programEntity = scheduledSessionEntity.getProgram();
        if(null == programEntity)
            return null;
        Long programId = programEntity.getId();
        if(programId <= 0)
            return null;
        LevelEntity levelEntity = scheduledSessionEntity.getLevel();
        if(null == levelEntity)
            return null;
        Long levelId = levelEntity.getId();
        if(levelId <= 0)
            return null;
        Date startTime = scheduledSessionEntity.getStartTime();
        if(null == startTime)
            return null;

        Date created = attendanceEntity.getCreated();

        return Attendance.builder()
                .scheduledSessionId(scheduledSessionId)
                .participantId(participantId)
                .levelId(levelId)
                .programId(programId)
                .startTime(startTime)
                .created(created)
                .build();
    }
}
