package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.entity.SadhanaFormEntity;
import com.sanjivani.lms.interfaces.ProgramService;
import com.sanjivani.lms.interfaces.SadhanaFormService;
import com.sanjivani.lms.model.Program;
import com.sanjivani.lms.model.SadhanaForm;
import com.sanjivani.lms.repository.ProgramRepository;
import com.sanjivani.lms.repository.SadhanaFormRepository;
import jakarta.transaction.Transactional;
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
public class SadhanaFormServiceImpl implements SadhanaFormService {
    private static final Logger LOG = LoggerFactory.getLogger(SadhanaFormServiceImpl.class);

    @Autowired
    private SadhanaFormRepository sadhanaFormRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramService programService;

    @Override
    @Transactional
    public Long save(@NonNull SadhanaForm sadhanaForm) throws IllegalArgumentException {
        Long programId = sadhanaForm.getProgramId();
        if(programId <= 0)
            throw new IllegalArgumentException("Sadhana Form cannot be generated for an empty Program");
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        if(null == programEntity)
            throw new IllegalArgumentException("Sadhana Form cannot be generated for an empty Program");
        SadhanaFormEntity sadhanaFormEntityFromProgramEntity = programEntity.getSadhanaForm();
        //If SadhanaFormEntity is already existing, then update it
        if(null != sadhanaFormEntityFromProgramEntity) {
            Long id = sadhanaFormEntityFromProgramEntity.getId();
            if (id <= 0)
                throw new IllegalArgumentException("SadhanaFormEntity cannot be without an ID");
            sadhanaForm.setId(id);
            update(sadhanaForm);
            return id;
        }

        //Sadhana
        Boolean earlyJapaRoundsBefore8AM = sadhanaForm.getEarlyJapaRoundsBefore8AM();
        Boolean earlyJapaRoundsAfter8AM = sadhanaForm.getEarlyJapaRoundsAfter8AM();
        Boolean numberOfRounds = sadhanaForm.getNumberOfRounds();
        Boolean first8RoundsCompletedTime = sadhanaForm.getFirst8RoundsCompletedTime();
        Boolean next8RoundsCompletedTime = sadhanaForm.getNext8RoundsCompletedTime();
        Boolean wakeUpTime = sadhanaForm.getWakeUpTime();
        Boolean sleepTime = sadhanaForm.getSleepTime();
        Boolean prabhupadaBookReading = sadhanaForm.getPrabhupadaBookReading();
        Boolean nonPrabhupadaBookReading = sadhanaForm.getNonPrabhupadaBookReading();
        Boolean prabhupadaClassHearing = sadhanaForm.getPrabhupadaClassHearing();
        Boolean guruClassHearing = sadhanaForm.getGuruClassHearing();
        Boolean otherClassHearing = sadhanaForm.getOtherClassHearing();
        Boolean speaker = sadhanaForm.getSpeaker();
        Boolean attendedArti = sadhanaForm.getAttendedArti();
        Boolean mobileInternetUsage = sadhanaForm.getMobileInternetUsage();
        if((null == earlyJapaRoundsBefore8AM || Boolean.FALSE.equals(earlyJapaRoundsAfter8AM)) &&
                (null == earlyJapaRoundsAfter8AM || Boolean.FALSE.equals(earlyJapaRoundsAfter8AM)) &&
                (null == numberOfRounds || Boolean.FALSE.equals(numberOfRounds)) &&
                (null == first8RoundsCompletedTime || Boolean.FALSE.equals(first8RoundsCompletedTime)) &&
                (null == next8RoundsCompletedTime || Boolean.FALSE.equals(next8RoundsCompletedTime)) &&
                (null == wakeUpTime || Boolean.FALSE.equals(wakeUpTime)) &&
                (null == sleepTime || Boolean.FALSE.equals(sleepTime)) &&
                (null == prabhupadaBookReading || Boolean.FALSE.equals(prabhupadaBookReading)) &&
                (null == nonPrabhupadaBookReading || Boolean.FALSE.equals(nonPrabhupadaBookReading)) &&
                (null == prabhupadaClassHearing || Boolean.FALSE.equals(prabhupadaClassHearing)) &&
                (null == guruClassHearing || Boolean.FALSE.equals(guruClassHearing)) &&
                (null == otherClassHearing || Boolean.FALSE.equals(otherClassHearing)) &&
                (null == speaker || Boolean.FALSE.equals(speaker)) &&
                (null == attendedArti || Boolean.FALSE.equals(attendedArti)) &&
                (null == mobileInternetUsage || Boolean.FALSE.equals(mobileInternetUsage)))
            throw new IllegalArgumentException("At least one field has to be configured for saving the form");

        SadhanaFormEntity sadhanaFormEntity = sadhanaFormRepository.save(SadhanaFormEntity.builder()
                .program(programEntity)
                .earlyJapaRoundsBefore8AM(earlyJapaRoundsBefore8AM)
                .earlyJapaRoundsAfter8AM(earlyJapaRoundsAfter8AM)
                .numberOfRounds(numberOfRounds)
                .first8RoundsCompletedTime(first8RoundsCompletedTime)
                .next8RoundsCompletedTime(next8RoundsCompletedTime)
                .wakeUpTime(wakeUpTime)
                .sleepTime(sleepTime)
                .prabhupadaBookReading(prabhupadaBookReading)
                .nonPrabhupadaBookReading(nonPrabhupadaBookReading)
                .prabhupadaClassHearing(prabhupadaClassHearing)
                .guruClassHearing(guruClassHearing)
                .otherClassHearing(otherClassHearing)
                .speaker(speaker)
                .attendedArti(attendedArti)
                .mobileInternetUsage(mobileInternetUsage)
                .build());

        Long Id = sadhanaFormEntity.getId();
        if (Id <= 0)
            throw new IllegalArgumentException("SadhanaFormEntity not saved");
        programService.updateSadhanaForm(programId, Id);
        return Id;
    }

    @Override
    public void update(@NonNull SadhanaForm sadhanaForm) throws IllegalArgumentException {
        Long id = sadhanaForm.getId();
        if (id <= 0)
            throw new IllegalArgumentException("SadhanaFormEntity not found");

        SadhanaFormEntity sadhanaFormEntity = sadhanaFormRepository.findById(id).orElse(null);
        if(null == sadhanaFormEntity)
            throw new IllegalArgumentException("SadhanaFormEntity not found");

        Long programId = sadhanaForm.getProgramId();
        if(programId <= 0)
            throw new IllegalArgumentException("Sadhana Form cannot be updated for an empty Program");
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        if(null == programEntity)
            throw new IllegalArgumentException("Sadhana Form cannot be updated for an empty Program");
        ProgramEntity programEntityFromSadhanaForm = sadhanaFormEntity.getProgram();
        if(null == programEntityFromSadhanaForm)
            throw new IllegalArgumentException("Sadhana Form cannot be existing with an empty Program");
        Long programIdFromSadhanaForm = programEntityFromSadhanaForm.getId();
        if(programIdFromSadhanaForm <= 0)
            throw new IllegalArgumentException("Sadhana Form cannot be existing with an empty Program");
        if(!programIdFromSadhanaForm.equals(programId))
            throw new IllegalArgumentException("Sadhana Form cannot be updated for a different Program");

        //Sadhana
        Boolean earlyJapaRoundsBefore8AM = sadhanaForm.getEarlyJapaRoundsBefore8AM();
        Boolean earlyJapaRoundsAfter8AM = sadhanaForm.getEarlyJapaRoundsAfter8AM();
        Boolean numberOfRounds = sadhanaForm.getNumberOfRounds();
        Boolean first8RoundsCompletedTime = sadhanaForm.getFirst8RoundsCompletedTime();
        Boolean next8RoundsCompletedTime = sadhanaForm.getNext8RoundsCompletedTime();
        Boolean wakeUpTime = sadhanaForm.getWakeUpTime();
        Boolean sleepTime = sadhanaForm.getSleepTime();
        Boolean prabhupadaBookReading = sadhanaForm.getPrabhupadaBookReading();
        Boolean nonPrabhupadaBookReading = sadhanaForm.getNonPrabhupadaBookReading();
        Boolean prabhupadaClassHearing = sadhanaForm.getPrabhupadaClassHearing();
        Boolean guruClassHearing = sadhanaForm.getGuruClassHearing();
        Boolean otherClassHearing = sadhanaForm.getOtherClassHearing();
        Boolean speaker = sadhanaForm.getSpeaker();
        Boolean attendedArti = sadhanaForm.getAttendedArti();
        Boolean mobileInternetUsage = sadhanaForm.getMobileInternetUsage();
        if((null == earlyJapaRoundsBefore8AM || Boolean.FALSE.equals(earlyJapaRoundsAfter8AM)) &&
                (null == earlyJapaRoundsAfter8AM || Boolean.FALSE.equals(earlyJapaRoundsAfter8AM)) &&
                (null == numberOfRounds || Boolean.FALSE.equals(numberOfRounds)) &&
                (null == first8RoundsCompletedTime || Boolean.FALSE.equals(first8RoundsCompletedTime)) &&
                (null == next8RoundsCompletedTime || Boolean.FALSE.equals(next8RoundsCompletedTime)) &&
                (null == wakeUpTime || Boolean.FALSE.equals(wakeUpTime)) &&
                (null == sleepTime || Boolean.FALSE.equals(sleepTime)) &&
                (null == prabhupadaBookReading || Boolean.FALSE.equals(prabhupadaBookReading)) &&
                (null == nonPrabhupadaBookReading || Boolean.FALSE.equals(nonPrabhupadaBookReading)) &&
                (null == prabhupadaClassHearing || Boolean.FALSE.equals(prabhupadaClassHearing)) &&
                (null == guruClassHearing || Boolean.FALSE.equals(guruClassHearing)) &&
                (null == otherClassHearing || Boolean.FALSE.equals(otherClassHearing)) &&
                (null == speaker || Boolean.FALSE.equals(speaker)) &&
                (null == attendedArti || Boolean.FALSE.equals(attendedArti)) &&
                (null == mobileInternetUsage || Boolean.FALSE.equals(mobileInternetUsage)))
            throw new IllegalArgumentException("At least one field has to be configured for updating the form");

        sadhanaFormEntity.setEarlyJapaRoundsBefore8AM(earlyJapaRoundsBefore8AM);
        sadhanaFormEntity.setEarlyJapaRoundsAfter8AM(earlyJapaRoundsAfter8AM);
        sadhanaFormEntity.setNumberOfRounds(numberOfRounds);
        sadhanaFormEntity.setFirst8RoundsCompletedTime(first8RoundsCompletedTime);
        sadhanaFormEntity.setNext8RoundsCompletedTime(next8RoundsCompletedTime);
        sadhanaFormEntity.setWakeUpTime(wakeUpTime);
        sadhanaFormEntity.setSleepTime(sleepTime);
        sadhanaFormEntity.setPrabhupadaBookReading(prabhupadaBookReading);
        sadhanaFormEntity.setNonPrabhupadaBookReading(nonPrabhupadaBookReading);
        sadhanaFormEntity.setPrabhupadaClassHearing(prabhupadaClassHearing);
        sadhanaFormEntity.setGuruClassHearing(guruClassHearing);
        sadhanaFormEntity.setOtherClassHearing(otherClassHearing);
        sadhanaFormEntity.setSpeaker(speaker);
        sadhanaFormEntity.setAttendedArti(attendedArti);
        sadhanaFormEntity.setMobileInternetUsage(mobileInternetUsage);

        sadhanaFormRepository.save(sadhanaFormEntity);
    }

    @Override
    public @NonNull Page<SadhanaForm> getSadhanaForm(@NonNull Pageable page) {
        Page<SadhanaFormEntity> pagedResult = sadhanaFormRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<SadhanaForm> sadhanaFormPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::sadhanaFormFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, sadhanaFormPage);
            return sadhanaFormPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public SadhanaForm getSadhanaFormById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<SadhanaFormEntity> sadhanaFormEntity = sadhanaFormRepository.findById(id);
        return sadhanaFormEntity.map(this::sadhanaFormFromEntity).orElse(null);
    }

    private SadhanaForm sadhanaFormFromEntity(@NonNull SadhanaFormEntity sadhanaFormEntity) {
        Long id = sadhanaFormEntity.getId();
        if (id <= 0)
            return null;

        ProgramEntity programEntity = sadhanaFormEntity.getProgram();
        if(null == programEntity)
            return null;
        Long programId = programEntity.getId();
        if(programId <= 0)
            return null;

        //Sadhana
        Boolean earlyJapaRoundsBefore8AM = sadhanaFormEntity.getEarlyJapaRoundsBefore8AM();
        Boolean earlyJapaRoundsAfter8AM = sadhanaFormEntity.getEarlyJapaRoundsAfter8AM();
        Boolean numberOfRounds = sadhanaFormEntity.getNumberOfRounds();
        Boolean first8RoundsCompletedTime = sadhanaFormEntity.getFirst8RoundsCompletedTime();
        Boolean next8RoundsCompletedTime = sadhanaFormEntity.getNext8RoundsCompletedTime();
        Boolean wakeUpTime = sadhanaFormEntity.getWakeUpTime();
        Boolean sleepTime = sadhanaFormEntity.getSleepTime();
        Boolean prabhupadaBookReading = sadhanaFormEntity.getPrabhupadaBookReading();
        Boolean nonPrabhupadaBookReading = sadhanaFormEntity.getNonPrabhupadaBookReading();
        Boolean prabhupadaClassHearing = sadhanaFormEntity.getPrabhupadaClassHearing();
        Boolean guruClassHearing = sadhanaFormEntity.getGuruClassHearing();
        Boolean otherClassHearing = sadhanaFormEntity.getOtherClassHearing();
        Boolean speaker = sadhanaFormEntity.getSpeaker();
        Boolean attendedArti = sadhanaFormEntity.getAttendedArti();
        Boolean mobileInternetUsage = sadhanaFormEntity.getMobileInternetUsage();
        Date created = sadhanaFormEntity.getCreated();
        Date modified = sadhanaFormEntity.getModified();

        return SadhanaForm.builder()
                .id(id)
                .programId(programId)
                .earlyJapaRoundsBefore8AM(earlyJapaRoundsBefore8AM)
                .earlyJapaRoundsAfter8AM(earlyJapaRoundsAfter8AM)
                .numberOfRounds(numberOfRounds)
                .first8RoundsCompletedTime(first8RoundsCompletedTime)
                .next8RoundsCompletedTime(next8RoundsCompletedTime)
                .wakeUpTime(wakeUpTime)
                .sleepTime(sleepTime)
                .prabhupadaBookReading(prabhupadaBookReading)
                .nonPrabhupadaBookReading(nonPrabhupadaBookReading)
                .prabhupadaClassHearing(prabhupadaClassHearing)
                .guruClassHearing(guruClassHearing)
                .otherClassHearing(otherClassHearing)
                .speaker(speaker)
                .attendedArti(attendedArti)
                .mobileInternetUsage(mobileInternetUsage)
                .created(created)
                .modified(modified)
                .build();
    }
}
