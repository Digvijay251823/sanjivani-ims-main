package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.CourseEntity;
import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.SadhanaFormEntity;
import com.sanjivani.lms.entity.VolunteerEntity;
import com.sanjivani.lms.enums.AudienceType;
import com.sanjivani.lms.enums.ProgramType;
import com.sanjivani.lms.interfaces.ProgramService;
import com.sanjivani.lms.model.Program;
import com.sanjivani.lms.repository.ProgramRepository;
import com.sanjivani.lms.repository.SadhanaFormRepository;
import com.sanjivani.lms.repository.VolunteerRepository;
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
public class ProgramServiceImpl implements ProgramService {
    private static final Logger LOG = LoggerFactory.getLogger(ProgramServiceImpl.class);

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private SadhanaFormRepository sadhanaFormRepository;

    @Override
    public Page<Program> getPrograms(Pageable pageable) {
        Page<ProgramEntity> pagedResult = programRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Program> programPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::programFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, programPage);
            return programPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public void save(@NonNull Program program) throws IllegalArgumentException {
        String name = program.getName();
        if(name.isEmpty())
            throw new IllegalArgumentException("name cannot be empty");

        String description = program.getDescription();

        Long incharge = program.getIncharge();
        VolunteerEntity inchargeEntity = null;
        if(incharge > 0L)
            inchargeEntity = volunteerRepository.findById(incharge).orElse(null);

        Long preacher = program.getPreacher();
        VolunteerEntity preacherEntity = null;
        if(preacher > 0L)
            preacherEntity = volunteerRepository.findById(preacher).orElse(null);

        Long mentor = program.getMentor();
        VolunteerEntity mentorEntity = null;
        if(mentor > 0L)
            mentorEntity = volunteerRepository.findById(mentor).orElse(null);

        Long coordinator = program.getCoordinator();
        VolunteerEntity coordinatorEntity = null;
        if(coordinator > 0L)
            coordinatorEntity = volunteerRepository.findById(coordinator).orElse(null);

        Long sadhanaForm = program.getSadhanaForm();
        SadhanaFormEntity sadhanaFormEntity = null;
        if(sadhanaForm > 0L)
            sadhanaFormEntity = sadhanaFormRepository.findById(sadhanaForm).orElse(null);

        String audienceType = program.getAudienceType();
        if(audienceType.isEmpty())
            throw new IllegalArgumentException("audience type cannot be empty");
        AudienceType audienceTypeEnum = null;
        try {
            audienceTypeEnum = AudienceType.valueOf(audienceType);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid audience type: " + audienceType);
            throw new IllegalArgumentException("Invalid audience type: " + audienceType);
        }

        String type = program.getType();
        if(type.isEmpty())
            throw new IllegalArgumentException("program type cannot be empty");
        ProgramType programTypeEnum = null;
        try {
            programTypeEnum = ProgramType.valueOf(type);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid program type: " + type);
            throw new IllegalArgumentException("Invalid program type: " + type);
        }

        String location = program.getLocation();
        if(location.isEmpty())
            throw new IllegalArgumentException("location cannot be empty");

        String createdBy;
        if(null == program.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = program.getCreatedBy();

        programRepository.save(ProgramEntity.builder()
                .name(name)
                .description(description)
                .incharge(inchargeEntity)
                .preacher(preacherEntity)
                .mentor(mentorEntity)
                .coordinator(coordinatorEntity)
                .sadhanaForm(sadhanaFormEntity)
                .type(programTypeEnum)
                .audienceType(audienceTypeEnum)
                .location(location)
                .createdBy(createdBy)
                .build());
    }

    @Override
    public void updateSadhanaForm(@NonNull Long id, @NonNull Long sadhanaFormId) throws IllegalArgumentException {
        if (id <= 0)
            throw new IllegalArgumentException("Program Entity not found");

        ProgramEntity programEntity = programRepository.findById(id).orElse(null);
        if(null == programEntity)
            throw new IllegalArgumentException("Program Entity not found");

        if(sadhanaFormId <= 0)
            throw new IllegalArgumentException("Sadhana Form Entity not found");
        SadhanaFormEntity sadhanaFormEntity = sadhanaFormRepository.findById(sadhanaFormId).orElse(null);
        if(null == sadhanaFormEntity)
            throw new IllegalArgumentException("Sadhana Form Entity not found");

        programEntity.setSadhanaForm(sadhanaFormEntity);
        programRepository.save(programEntity);
    }

    @Override
    public Program getProgramById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<ProgramEntity> programEntity = programRepository.findById(id);
        return programEntity.map(this::programFromEntity).orElse(null);
    }

    @Override
    public Program getProgramByName(@NonNull String name) {
        if(name.isEmpty())
            return null;
        Optional<ProgramEntity> programEntity = programRepository.findByName(name);
        return programEntity.map(this::programFromEntity).orElse(null);
    }

    @Override
    public Page<Program> getProgramsByIncharge(Long incharge, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Program> getProgramsByPreacher(Long preacher, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Program> getProgramsByMentor(Long Mentor, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Program> getProgramsByCoordinator(Long coordinator, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Program> getProgramsByLocation(String location, Pageable pageable) {
        return null;
    }

    private Program programFromEntity(@NonNull  ProgramEntity programEntity) {
        Long id = programEntity.getId();
        if( id <= 0L)
            return null;

        String name = programEntity.getName();
        if(null == name || name.isEmpty())
            return null;

        String description = programEntity.getDescription();

        VolunteerEntity inchargeEntity = programEntity.getIncharge();
        Long incharge = 0L;
        if(null != inchargeEntity)
            incharge = inchargeEntity.getId();

        VolunteerEntity preacherEntity = programEntity.getPreacher();
        Long preacher = 0L;
        if(null != preacherEntity)
            preacher = preacherEntity.getId();

        VolunteerEntity mentorEntity = programEntity.getMentor();
        Long mentor = 0L;
        if(null != mentorEntity)
            mentor = mentorEntity.getId();

        VolunteerEntity coordinatorEntity = programEntity.getCoordinator();
        Long coordinator = 0L;
        if(null != coordinatorEntity)
            coordinator = coordinatorEntity.getId();

        SadhanaFormEntity sadhanaFormEntity = programEntity.getSadhanaForm();
        Long sadhanaForm = 0L;
        if(null != sadhanaFormEntity)
            sadhanaForm = sadhanaFormEntity.getId();

        String audienceType = programEntity.getAudienceType().toString();
        String type = programEntity.getType().toString();
        String location = programEntity.getLocation();

        String createdBy;
        if(null == programEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = programEntity.getCreatedBy();

        Date created = programEntity.getCreated();
        Date modified = programEntity.getModified();

        return Program.builder()
                .id(id)
                .name(name)
                .description(description)
                .incharge(incharge)
                .preacher(preacher)
                .mentor(mentor)
                .coordinator(coordinator)
                .sadhanaForm(sadhanaForm)
                .type(type)
                .audienceType(audienceType)
                .location(location)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
