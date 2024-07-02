package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import com.sanjivani.lms.enums.RSVPOption;
import com.sanjivani.lms.interfaces.RSVPService;
import com.sanjivani.lms.model.RSVP;
import com.sanjivani.lms.repository.*;
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
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class RSVPServiceImpl implements RSVPService {
    private static final Logger LOG = LoggerFactory.getLogger(RSVPServiceImpl.class);

    @Autowired
    private RSVPRepository rsvpRepository;

    @Autowired
    private ScheduledSessionRepository scheduledSessionRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantActivityRepository participantActivityRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void save(@NonNull RSVP rsvp) throws IllegalArgumentException {
        Long scheduledSessionId = rsvp.getScheduledSessionId();
        if (scheduledSessionId <= 0)
            throw new IllegalArgumentException("Scheduled Session Id cannot be less than or equal to 0");
        ScheduledSessionEntity scheduledSessionEntity = scheduledSessionRepository.findById(scheduledSessionId).orElse(null);
        if(scheduledSessionEntity == null)
            throw new IllegalArgumentException("RSVP cannot be marked for non-existing scheduled session");
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
        //Fetch activity with name "RSVP"
        ActivityEntity activityEntity = activityRepository.findByName("RSVP").orElse(null);
        if(null == activityEntity)
            throw new IllegalArgumentException("Activity not found with name 'Attendance', Please add an activity with name 'Attendance' to mark attendance.");
        Long activityId = activityEntity.getId();
        if(activityId <= 0)
            throw new IllegalArgumentException("Activity Id cannot be less than or equal to 0");
        String activityName = activityEntity.getName();
        if(null == activityName || activityName.isEmpty())
            throw new IllegalArgumentException("Activity name cannot be empty");
        String activityDescription = activityEntity.getDescription();

        Long participantId = rsvp.getParticipantId();
        if (participantId <= 0)
            throw new IllegalArgumentException("Participant Id cannot be less than or equal to 0");
        ParticipantEntity participantEntity = participantRepository.findById(participantId).orElse(null);
        if(participantEntity == null)
            throw new IllegalArgumentException("RSVP cannot be marked for non-existing participant");
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


        RSVPOption rsvpOption =  rsvp.getRsvp();

        rsvpRepository.save(RSVPEntity.builder()
                .id(AttendanceKey.builder()
                        .scheduledSessionId(scheduledSessionId)
                        .participantId(participantId)
                        .build())
                .participant(participantEntity)
                .scheduledSession(scheduledSessionEntity)
                .rsvp(rsvpOption)
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
                .activityId(activityId)
                .activityName(activityName)
                .activityDescription(activityDescription)
                .scheduledSessionId(scheduledSessionId)
                .scheduledSessionName(scheduledSessionName)
                .sessionId(sessionId)
                .sessionName(sessionName)
                .sessionCode(sessionCode)
                .courseId(courseId)
                .courseName(courseName)
                .courseCode(courseCode)
                .rsvp(rsvpOption)
                .activityDate(activityDateStr)
                .build());
    }

    @Override
    public RSVP getRSVPByParticipantAndSession(@NonNull Long participantId, @NonNull Long scheduledSessionId) {
        RSVPEntity rsvpEntity = rsvpRepository.findById(AttendanceKey.builder()
                .participantId(participantId)
                .scheduledSessionId(scheduledSessionId)
                .build()).orElse(null);
        if (rsvpEntity == null)
            return null;
        return rsvpFromEntity(rsvpEntity);
    }

    @Override
    public @NonNull Page<RSVP> getRSVP(@NonNull Pageable page) {
        Page<RSVPEntity> pagedResult = rsvpRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<RSVP> rsvpPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::rsvpFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, rsvpPage);
            return rsvpPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public @NonNull Page<RSVP> getRSVPBySession(@NonNull Long scheduledSessionId, @NonNull Pageable page) {
        Page<RSVPEntity> pagedResult = rsvpRepository.findByIdScheduledSessionId(scheduledSessionId, page);
        if (pagedResult.hasContent()) {
            Page<RSVP> rsvpPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::rsvpFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, rsvpPage);
            return rsvpPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private RSVP rsvpFromEntity(@NonNull RSVPEntity rsvpEntity) {
        AttendanceKey attendanceKey = rsvpEntity.getId();
        if(null == attendanceKey)
            return null;
        Long scheduledSessionId = attendanceKey.getScheduledSessionId();
        if(scheduledSessionId <= 0)
            return null;
        Long participantId = attendanceKey.getParticipantId();
        if(participantId <= 0)
            return null;
        ScheduledSessionEntity scheduledSessionEntity = rsvpEntity.getScheduledSession();
        if(null == scheduledSessionEntity)
            return null;
        String scheduledSessionName = scheduledSessionEntity.getName();
        if(null == scheduledSessionName || scheduledSessionName.isEmpty())
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
        RSVPOption rsvpOption = rsvpEntity.getRsvp();
        if(null == rsvpOption)
            return null;

        Date created = rsvpEntity.getCreated();
        Date modified = rsvpEntity.getModified();

        return RSVP.builder()
                .scheduledSessionId(scheduledSessionId)
                .scheduledSessionName(scheduledSessionName)
                .participantId(participantId)
                .levelId(levelId)
                .programId(programId)
                .rsvp(rsvpOption)
                .created(created)
                .modified(modified)
                .build();
    }
}
