package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.LevelStatus;
import com.sanjivani.lms.enums.ProgramType;
import com.sanjivani.lms.interfaces.ParticipantRegistrationService;
import com.sanjivani.lms.model.ParticipantRegistration;
import com.sanjivani.lms.repository.*;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

@Service
public class ParticipantRegistrationServiceImpl implements ParticipantRegistrationService {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantRegistrationServiceImpl.class);

    @Autowired
    private ParticipantRegistrationRepository participantRegistrationRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private LevelRepository levelRepository;

    @Override
    @Transactional
    public void save(ParticipantRegistration participantRegistration) throws Exception {

        //Participant
        Long participantId = participantRegistration.getParticipantId();
        if (participantId <= 0L)
            throw new IllegalArgumentException("Participant Id cannot be less than or equal to 0");
        ParticipantEntity participantEntity = participantRepository.findById(participantId).orElse(null);
        if (participantEntity == null)
            throw new IllegalArgumentException("Participant Registration cannot be done for non-existing participant");
        String participantFirstName = participantEntity.getFirstName();
        String participantLastName = participantEntity.getLastName();
        String participantWaNumber = participantEntity.getWaNumber();
        String participantContactNumber = participantEntity.getContactNumber();
        if(null == participantFirstName || participantFirstName.isEmpty() ||
                null == participantLastName || participantLastName.isEmpty() ||
                null == participantWaNumber || participantWaNumber.isEmpty() ||
                null == participantContactNumber || participantContactNumber.isEmpty())
            throw new IllegalArgumentException("Participant Registration cannot be done for participant with incomplete details");
        String participantEmail = participantEntity.getEmail();
        Integer participantAge = participantEntity.getAge();
        Gender participantGender = participantEntity.getGender();
        String participantAddress = participantEntity.getAddress();
        String participantCity = participantEntity.getCity();

        //Level
        Long levelId = participantRegistration.getLevelId();
        if (levelId <= 0)
            throw new IllegalArgumentException("Level Id cannot be less than or equal to 0");
        LevelEntity levelEntity = levelRepository.findById(levelId).orElse(null);
        if (levelEntity == null)
            throw new IllegalArgumentException("Participant Registration cannot be done for non-existing level");

        //Important check
        Boolean isAcceptingNewParticipants = levelEntity.getAcceptingNewParticipants();
        if (null == isAcceptingNewParticipants || !isAcceptingNewParticipants)
            throw new IllegalArgumentException("Participant Registration cannot be done for level not accepting new participants");

        Integer levelNumber = levelEntity.getNumber();
        if (levelNumber <= 0)
            throw new IllegalArgumentException("Level Number cannot be less than or equal to 0");
        String levelName = levelEntity.getName();
        if (null == levelName || levelName.isEmpty())
            throw new IllegalArgumentException("Level Name cannot be empty");
        String levelDisplayName = levelEntity.getDisplayName();
        LevelStatus levelStatus = levelEntity.getStatus();
        if (null == levelStatus)
            throw new IllegalArgumentException("Level Status cannot be empty");
        DayOfWeek levelSessionDay = levelEntity.getSessionDay();
        LocalTime levelSessionTime = levelEntity.getSessionTime();

        //Program
        ProgramEntity programEntity = levelEntity.getProgram();
        if (programEntity == null)
            throw new IllegalArgumentException("Program Entity cannot be non-existing in level");
        String programName = programEntity.getName();
        if (null == programName || programName.isEmpty())
            throw new IllegalArgumentException("Program Name cannot be empty");
        String programLocation = programEntity.getLocation();
        if (null == programLocation || programLocation.isEmpty())
            throw new IllegalArgumentException("Program Location cannot be empty");
        ProgramType programType = programEntity.getType();
        if (null == programType)
            throw new IllegalArgumentException("Program Type cannot be empty");
        AudienceType audienceType = programEntity.getAudienceType();
        if (null == audienceType)
            throw new IllegalArgumentException("Audience Type cannot be empty");

        participantRegistrationRepository.save(ParticipantRegistrationEntity.builder()
                .programId(programEntity.getId())
                .programName(programName)
                .programLocation(programLocation)
                .programType(programType)
                .audienceType(audienceType)
                .levelId(levelId)
                .levelNumber(levelNumber)
                .levelName(levelName)
                .levelDisplayName(levelDisplayName)
                .levelStatus(levelStatus)
                .levelSessionDay(levelSessionDay)
                .levelSessionTime(levelSessionTime)
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
                .participantEmail(participantEmail)
                .participantAge(participantAge)
                .participantGender(participantGender)
                .participantAddress(participantAddress)
                .participantCity(participantCity)
                .build());
    }

    @Override
    public @NonNull Page<ParticipantRegistration> getParticipantRegistration(@NonNull Pageable page) {
        Page<ParticipantRegistrationEntity> pagedResult = participantRegistrationRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<ParticipantRegistration> participantRegistrationPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantRegistrationFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantRegistrationPage);
            return participantRegistrationPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public @NonNull Page<ParticipantRegistration> getParticipantRegistrationByAny(String levelName, String levelDisplayName,
                                                                          String participantContactNumber, String participantFirstName,
                                                                          String participantLastName, String programName, @NonNull Pageable page) {
        Page<ParticipantRegistrationEntity> pagedResult = participantRegistrationRepository.findAllByAny(
                levelName, levelDisplayName, participantContactNumber, participantFirstName,
                participantLastName, programName, page);
        if (pagedResult.hasContent()) {
            Page<ParticipantRegistration> participantRegistrationPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantRegistrationFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantRegistrationPage);
            return participantRegistrationPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private ParticipantRegistration participantRegistrationFromEntity(@NonNull ParticipantRegistrationEntity participantRegistrationEntity) {
        Long id = participantRegistrationEntity.getId();
        if (id <= 0)
            return null;

        //Program
        Long programId = participantRegistrationEntity.getProgramId();
        if (programId <= 0)
            return null;
        String programName = participantRegistrationEntity.getProgramName();
        if (null == programName || programName.isEmpty())
            return null;
        String programLocation = participantRegistrationEntity.getProgramLocation();
        if (null == programLocation || programLocation.isEmpty())
            return null;
        ProgramType programType = participantRegistrationEntity.getProgramType();
        if (null == programType)
            return null;
        AudienceType audienceType = participantRegistrationEntity.getAudienceType();
        if (null == audienceType)
            return null;

        //Level
        Long levelId = participantRegistrationEntity.getLevelId();
        if (levelId <= 0)
            return null;
        Integer levelNumber = participantRegistrationEntity.getLevelNumber();
        if (levelNumber <= 0)
            return null;
        String levelName = participantRegistrationEntity.getLevelName();
        if (null == levelName || levelName.isEmpty())
            return null;
        String levelDisplayName = participantRegistrationEntity.getLevelDisplayName();
        LevelStatus levelStatus = participantRegistrationEntity.getLevelStatus();
        if (null == levelStatus)
            return null;
        DayOfWeek levelSessionDay = participantRegistrationEntity.getLevelSessionDay();
        LocalTime levelSessionTime = participantRegistrationEntity.getLevelSessionTime();

        //Participant
        Long participantId = participantRegistrationEntity.getParticipantId();
        if(participantId <= 0)
            return null;
        String participantFirstName = participantRegistrationEntity.getParticipantFirstName();
        if(null == participantFirstName || participantFirstName.isEmpty())
            return null;
        String participantLastName = participantRegistrationEntity.getParticipantLastName();
        if(null == participantLastName || participantLastName.isEmpty())
            return null;
        String participantWaNumber = participantRegistrationEntity.getParticipantWaNumber();
        if(null == participantWaNumber || participantWaNumber.isEmpty())
            return null;
        String participantContactNumber = participantRegistrationEntity.getParticipantContactNumber();
        if(null == participantContactNumber || participantContactNumber.isEmpty())
            return null;
        String participantEmail = participantRegistrationEntity.getParticipantEmail();
        Integer participantAge = participantRegistrationEntity.getParticipantAge();
        Gender participantGender = participantRegistrationEntity.getParticipantGender();
        String participantAddress = participantRegistrationEntity.getParticipantAddress();
        String participantCity = participantRegistrationEntity.getParticipantCity();
        Date created = participantRegistrationEntity.getCreated();
        Date modified = participantRegistrationEntity.getModified();

        return ParticipantRegistration.builder()
                .id(id)
                .programId(programId)
                .programName(programName)
                .programLocation(programLocation)
                .programType(programType)
                .audienceType(audienceType)
                .levelId(levelId)
                .levelNumber(levelNumber)
                .levelName(levelName)
                .levelDisplayName(levelDisplayName)
                .levelStatus(levelStatus)
                .levelSessionDay(levelSessionDay)
                .levelSessionTime(levelSessionTime)
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
                .participantEmail(participantEmail)
                .participantAge(participantAge)
                .participantGender(participantGender)
                .participantAddress(participantAddress)
                .participantCity(participantCity)
                .created(created)
                .modified(modified)
                .build();
    }
}
