package com.sanjivani.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
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
import com.sanjivani.lms.entity.CourseEntity;
import com.sanjivani.lms.entity.LevelEntity;
import com.sanjivani.lms.entity.ParticipantActivityEntity;
import com.sanjivani.lms.entity.ParticipantEntity;
import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.ScheduledSessionEntity;
import com.sanjivani.lms.entity.SessionEntity;
import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import com.sanjivani.lms.enums.RSVPOption;
import com.sanjivani.lms.interfaces.ParticipantActivityService;
import com.sanjivani.lms.model.ParticipantActivity;
import com.sanjivani.lms.repository.ActivityRepository;
import com.sanjivani.lms.repository.ParticipantActivityRepository;
import com.sanjivani.lms.repository.ParticipantRepository;
import com.sanjivani.lms.repository.ProgramRepository;
import com.sanjivani.lms.repository.ScheduledSessionRepository;

import jakarta.transaction.Transactional;
import lombok.NonNull;

@Service
public class ParticipantActivityServiceImpl implements ParticipantActivityService {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantActivityServiceImpl.class);

    @Autowired
    private ParticipantActivityRepository participantActivityRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ScheduledSessionRepository scheduledSessionRepository;

    @Override
    @Transactional
    public void save(ParticipantActivity participantActivity) throws Exception {
        String description = participantActivity.getDescription();
        //Participant
        Long participantId = participantActivity.getParticipantId();
        if (participantId <= 0L)
            throw new IllegalArgumentException("Participant Id cannot be less than or equal to 0");
        ParticipantEntity participantEntity = participantRepository.findById(participantId).orElse(null);
        if (participantEntity == null)
            throw new IllegalArgumentException("Participant Activity cannot be registered for non-existing participant");
        String participantFirstName = participantEntity.getFirstName();
        String participantLastName = participantEntity.getLastName();
        String participantWaNumber = participantEntity.getWaNumber();
        String participantContactNumber = participantEntity.getContactNumber();
        if(null == participantFirstName || participantFirstName.isEmpty() ||
                null == participantLastName || participantLastName.isEmpty() ||
                null == participantWaNumber || participantWaNumber.isEmpty() ||
                null == participantContactNumber || participantContactNumber.isEmpty())
            throw new IllegalArgumentException("Participant Activity cannot be registered for participant with incomplete details");
        String participantEmail = participantEntity.getEmail();

        //Program
        Long programId = participantActivity.getProgramId();
        if (programId <= 0)
            throw new IllegalArgumentException("Program Id cannot be less than or equal to 0");
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        if (programEntity == null)
            throw new IllegalArgumentException("Participant Activity cannot be registered for non-existing program");
        String programName = programEntity.getName();
        String programLocation = programEntity.getLocation();
        ProgramType programType = programEntity.getType();
        AudienceType audienceType = programEntity.getAudienceType();
        if(null == programName || programName.isEmpty() ||
                null == programLocation || programLocation.isEmpty() ||
                null == programType || null == audienceType)
            throw new IllegalArgumentException("Participant Activity cannot be registered for program with incomplete details");

        //Activity
        Long activityId = participantActivity.getActivityId();
        if (activityId <= 0)
            throw new IllegalArgumentException("Activity Id cannot be less than or equal to 0");
        ActivityEntity activityEntity = activityRepository.findById(activityId).orElse(null);
        if (activityEntity == null)
            throw new IllegalArgumentException("Participant Activity cannot be registered for non-existing activity");
        String activityName = activityEntity.getName();
        if(null == activityName || activityName.isEmpty())
            throw new IllegalArgumentException("Participant Activity cannot be registered for activity with incomplete details");
        String activityDescription = activityEntity.getDescription();

        //Scheduled Session
        Long scheduledSessionId = participantActivity.getScheduledSessionId();
        ScheduledSessionEntity scheduledSessionEntity = null;
        if (null != scheduledSessionId && scheduledSessionId > 0) {
            scheduledSessionEntity = scheduledSessionRepository.findById(scheduledSessionId).orElse(null);
            if (scheduledSessionEntity == null)
                throw new IllegalArgumentException("Participant Activity cannot be registered for non-existing scheduled session");
        }
        String scheduledSessionName = null;
        Long sessionId = null;
        String sessionCode = null;
        String sessionName = null;
        Long courseId = null;
        String courseCode = null;
        String courseName = null;
        Long levelId = null;
        Integer levelNumber = null;
        String levelName = null;
        LevelStatus levelStatus = null;

        if(null != scheduledSessionEntity) {
            scheduledSessionName = scheduledSessionEntity.getName();
            SessionEntity sessionEntity = scheduledSessionEntity.getSession();
            if (null == sessionEntity)
                throw new IllegalArgumentException("Session Entity cannot be non-existing in scheduled session");
            sessionId = sessionEntity.getId();
            if (sessionId <= 0)
                throw new IllegalArgumentException("Session Id cannot be less than or equal to 0");
            sessionCode = sessionEntity.getCode();
            if (null == sessionCode || sessionCode.isEmpty())
                throw new IllegalArgumentException("Session Code cannot be empty");
            sessionName = sessionEntity.getName();
            if (null == sessionName || sessionName.isEmpty())
                throw new IllegalArgumentException("Session Name cannot be empty");
            CourseEntity courseEntity = sessionEntity.getCourse();
            if (null == courseEntity)
                throw new IllegalArgumentException("Course Entity cannot be non-existing in session");
            courseId = courseEntity.getId();
            if (courseId <= 0)
                throw new IllegalArgumentException("Course Id cannot be less than or equal to 0");
            courseCode = courseEntity.getCode();
            if (null == courseCode || courseCode.isEmpty())
                throw new IllegalArgumentException("Course Code cannot be empty");
            courseName = courseEntity.getName();
            if (null == courseName || courseName.isEmpty())
                throw new IllegalArgumentException("Course Name cannot be empty");
            LevelEntity levelEntity = scheduledSessionEntity.getLevel();
            if (null == levelEntity)
                throw new IllegalArgumentException("Level Entity cannot be non-existing in scheduled session");
            levelId = levelEntity.getId();
            if (levelId <= 0)
                throw new IllegalArgumentException("Level Id cannot be less than or equal to 0");
            levelNumber = levelEntity.getNumber();
            if (levelNumber <= 0)
                throw new IllegalArgumentException("Level Number cannot be less than or equal to 0");
            levelName = levelEntity.getName();
            if (null == levelName || levelName.isEmpty())
                throw new IllegalArgumentException("Level Name cannot be empty");
            levelStatus = levelEntity.getStatus();
            if (null == levelStatus)
                throw new IllegalArgumentException("Level Status cannot be empty");
        }

        RSVPOption rsvpOption = participantActivity.getRsvp();

        //Activity Date
        String activityDateString = participantActivity.getActivityDate();
        if(activityDateString.isEmpty())
            throw new IllegalArgumentException("Activity Date cannot be empty");
        Date activityDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            activityDate = formatter.parse(activityDateString);
        } catch(ParseException ex) {
            throw new IllegalArgumentException("Activity Date could not be parsed");
        }
        LOG.info("Formatted String Activity Date: {}", formatter.format(activityDate));
        Date current = new Date();
        if (current.before(activityDate))
            throw new IllegalArgumentException("Activity Date cannot be in future");

        participantActivityRepository.save(ParticipantActivityEntity.builder()
                .description(description)
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
                .participantEmail(participantEmail)
                .programId(programId)
                .programName(programName)
                .programLocation(programLocation)
                .programType(programType)
                .audienceType(audienceType)
                .activityId(activityId)
                .activityName(activityName)
                .activityDescription(activityDescription)
                .scheduledSessionId(scheduledSessionId)
                .scheduledSessionName(scheduledSessionName)
                .sessionId(sessionId)
                .sessionCode(sessionCode)
                .sessionName(sessionName)
                .courseId(courseId)
                .courseCode(courseCode)
                .courseName(courseName)
                .levelId(levelId)
                .levelNumber(levelNumber)
                .levelName(levelName)
                .levelStatus(levelStatus)
                .rsvp(rsvpOption)
                .activityDate(activityDateString)
                .build());

        //Update ParticipantEntity
        Integer japaRounds = switch(activityName) {
            case "Chanting 1 Round" -> 1;
            case "Chanting 2 Rounds" -> 2;
            case "Chanting 3 Rounds" -> 3;
            case "Chanting 4 Rounds" -> 4;
            case "Chanting 5 Rounds" -> 5;
            case "Chanting 6 Rounds" -> 6;
            case "Chanting 7 Rounds" -> 7;
            case "Chanting 8 Rounds" -> 8;
            case "Chanting 9 Rounds" -> 9;
            case "Chanting 10 Rounds" -> 10;
            case "Chanting 11 Rounds" -> 11;
            case "Chanting 12 Rounds" -> 12;
            case "Chanting 13 Rounds" -> 13;
            case "Chanting 14 Rounds" -> 14;
            case "Chanting 15 Rounds" -> 15;
            case "Chanting 16 Rounds" -> 16;
            default -> 0;
        };
        if (japaRounds > 0)
            try {
                participantRepository.updateJapaRounds(participantId, japaRounds);
            } catch (Exception ex) {
                LOG.error("Error updating Japa Rounds for Participant Id: {}", participantId);
                throw new Exception("Error updating Japa Rounds for Participant Id: " + participantId.toString());
            }
    }

    @Override
    public @NonNull Page<ParticipantActivity> getParticipantActivity(@NonNull Pageable page) {
        Page<ParticipantActivityEntity> pagedResult = participantActivityRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<ParticipantActivity> participantActivityPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantActivityFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantActivityPage);
            return participantActivityPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public @NonNull Page<ParticipantActivity> getParticipantActivityByAny(String activityName, String courseCode, String levelName,
                                                                          String participantContactNumber, String participantFirstName,
                                                                          String participantLastName, String programName,
                                                                          String scheduledSessionName, String activityDate, @NonNull Pageable page) {
        if(null != activityDate && !activityDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            try {
                Date activityDt = formatter.parse(activityDate);
                Date current = new Date();
                if (current.before(activityDt))
                    return Page.empty(PageRequest.of(0, 10, Sort.by("id")));

            } catch(ParseException ex) {
                return Page.empty(PageRequest.of(0, 10, Sort.by("id")));
            }
        }
        Page<ParticipantActivityEntity> pagedResult = participantActivityRepository.findAllByAny(
                activityName, courseCode, levelName, participantContactNumber, participantFirstName,
                participantLastName, programName, scheduledSessionName, activityDate, page);
        if (pagedResult.hasContent()) {
            Page<ParticipantActivity> participantActivityPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantActivityFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantActivityPage);
            return participantActivityPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private ParticipantActivity participantActivityFromEntity(@NonNull ParticipantActivityEntity participantActivityEntity) {
        Long id = participantActivityEntity.getId();
        if (id <= 0)
            return null;

        String description = participantActivityEntity.getDescription();

        //Program
        Long programId = participantActivityEntity.getProgramId();
        if(programId <= 0)
            return null;
        String programName = participantActivityEntity.getProgramName();
        if(null == programName || programName.isEmpty())
            return null;
        String programLocation = participantActivityEntity.getProgramLocation();
        if(null == programLocation || programLocation.isEmpty())
            return null;
        ProgramType programTypeEnum = participantActivityEntity.getProgramType();
        if(null == programTypeEnum)
            return null;
        String programType = programTypeEnum.name();
        AudienceType audienceTypeEnum = participantActivityEntity.getAudienceType();
        if(null == audienceTypeEnum)
            return null;
        String audienceType = audienceTypeEnum.name();
        //Level
        Long levelId = participantActivityEntity.getLevelId();
        Integer levelNumber = participantActivityEntity.getLevelNumber();
        String levelName = participantActivityEntity.getLevelName();
        String levelStatus = null;
        LevelStatus levelStatusEnum = participantActivityEntity.getLevelStatus();
        if(null != levelStatusEnum)
            levelStatus = levelStatusEnum.name();

        //Participant
        Long participantId = participantActivityEntity.getParticipantId();
        if(participantId <= 0)
            return null;
        String participantFirstName = participantActivityEntity.getParticipantFirstName();
        if(null == participantFirstName || participantFirstName.isEmpty())
            return null;
        String participantLastName = participantActivityEntity.getParticipantLastName();
        if(null == participantLastName || participantLastName.isEmpty())
            return null;
        String participantWaNumber = participantActivityEntity.getParticipantWaNumber();
        if(null == participantWaNumber || participantWaNumber.isEmpty())
            return null;
        String participantContactNumber = participantActivityEntity.getParticipantContactNumber();
        if(null == participantContactNumber || participantContactNumber.isEmpty())
            return null;
        String participantEmail = participantActivityEntity.getParticipantEmail();
        Long activityId = participantActivityEntity.getActivityId();
        if(activityId <= 0)
            return null;
        String activityName = participantActivityEntity.getActivityName();
        if(null == activityName || activityName.isEmpty())
            return null;
        String activityDescription = participantActivityEntity.getActivityDescription();

        //Session
        Long scheduledSessionId = participantActivityEntity.getScheduledSessionId();
        String scheduledSessionName = participantActivityEntity.getScheduledSessionName();
        Long sessionId = participantActivityEntity.getSessionId();
        String sessionCode = participantActivityEntity.getSessionCode();
        String sessionName = participantActivityEntity.getSessionName();

        //Course
        Long courseId = participantActivityEntity.getCourseId();
        String courseCode = participantActivityEntity.getCourseCode();
        String courseName = participantActivityEntity.getCourseName();

        String activityDateString = participantActivityEntity.getActivityDate();
        if(null == activityDateString || activityDateString.isEmpty())
            return null;

        //RSVP
        RSVPOption rsvpOption = participantActivityEntity.getRsvp();
        Long membersComming = participantActivityEntity.getMembersComming();

        Date created = participantActivityEntity.getCreated();
        Date modified = participantActivityEntity.getModified();

        return ParticipantActivity.builder()
                .id(id)
                .description(description)
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
                .sessionCode(sessionCode)
                .sessionName(sessionName)
                .courseId(courseId)
                .courseCode(courseCode)
                .courseName(courseName)
                .rsvp(rsvpOption)
                .membersComming(membersComming)
                .activityDate(activityDateString)
                .created(created)
                .modified(modified)
                .build();
    }
}
