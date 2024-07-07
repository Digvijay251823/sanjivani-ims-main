package com.sanjivani.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
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

import com.sanjivani.lms.entity.ParticipantEntity;
import com.sanjivani.lms.entity.ParticipantSadhanaEntity;
import com.sanjivani.lms.entity.ProgramEntity;
import com.sanjivani.lms.interfaces.ParticipantSadhanaService;
import com.sanjivani.lms.model.ParticipantSadhana;
import com.sanjivani.lms.repository.ParticipantRepository;
import com.sanjivani.lms.repository.ParticipantSadhanaRepository;
import com.sanjivani.lms.repository.ProgramRepository;

import lombok.NonNull;

@Service
public class ParticipantSadhanaServiceImpl implements ParticipantSadhanaService {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantSadhanaServiceImpl.class);

    @Autowired
    private ParticipantSadhanaRepository participantSadhanaRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ProgramRepository programRepository;

    @Override
    public void save(@NonNull ParticipantSadhana participantSadhana) throws IllegalArgumentException {
        //Participant
        Long participantId = participantSadhana.getParticipantId();
        if (participantId <= 0L)
            throw new IllegalArgumentException("Participant Id cannot be less than or equal to 0");
        ParticipantEntity participantEntity = participantRepository.findById(participantId).orElse(null);
        if (participantEntity == null)
            throw new IllegalArgumentException("Participant Sadhana cannot be registered for non-existing participant");
        String participantFirstName = participantEntity.getFirstName();
        String participantLastName = participantEntity.getLastName();
        String participantWaNumber = participantEntity.getWaNumber();
        String participantContactNumber = participantEntity.getContactNumber();
        if(null == participantFirstName || participantFirstName.isEmpty() ||
                null == participantLastName || participantLastName.isEmpty() ||
                null == participantWaNumber || participantWaNumber.isEmpty() ||
                null == participantContactNumber || participantContactNumber.isEmpty())
            throw new IllegalArgumentException("Participant Sadhana cannot be registered for participant with incomplete details");

        //Program
        Long programId = participantSadhana.getProgramId();
        if (programId <= 0)
            throw new IllegalArgumentException("Program Id cannot be less than or equal to 0");
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        if (programEntity == null)
            throw new IllegalArgumentException("Participant Sadhana cannot be registered for non-existing program");
        String programName = programEntity.getName();
        if(null == programName || programName.isEmpty())
            throw new IllegalArgumentException("Participant Sadhana cannot be registered for program with incomplete details");

        //Sadhana
        Integer numberOfRounds = participantSadhana.getNumberOfRounds();
        LocalTime first8RoundsCompletedTime = participantSadhana.getFirst8RoundsCompletedTime();
        LocalTime next8RoundsCompletedTime = participantSadhana.getNext8RoundsCompletedTime();
        LocalTime wakeUpTime = participantSadhana.getWakeUpTime();
        LocalTime sleepTime = participantSadhana.getSleepTime();
        Integer prabhupadaBookReading = participantSadhana.getPrabhupadaBookReading();
        Integer nonPrabhupadaBookReading = participantSadhana.getNonPrabhupadaBookReading();
        Integer prabhupadaClassHearing = participantSadhana.getPrabhupadaClassHearing();
        Integer guruClassHearing = participantSadhana.getGuruClassHearing();
        Integer otherClassHearing = participantSadhana.getOtherClassHearing();
        String speaker = participantSadhana.getSpeaker();
        Boolean attendedArti = participantSadhana.getAttendedArti();
        Integer mobileInternetUsage = participantSadhana.getMobileInternetUsage();
        String topic= participantSadhana.getTopic();
        String visibleSadhana= participantSadhana.getVisibleSadhana();

        String sadhanaDateString = participantSadhana.getSadhanaDate();
        if(sadhanaDateString.isEmpty()) {
            throw new IllegalArgumentException("Sadhana Date cannot be empty");
        }
        Date sadhanaDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            sadhanaDate = formatter.parse(sadhanaDateString);
        } catch(ParseException ex) {
            throw new IllegalArgumentException("Sadhana Date could not be parsed");
        }
        LOG.info("Formatted String Activity Date: {}", formatter.format(sadhanaDate));
        Date current = new Date();
        if (current.before(sadhanaDate))
            throw new IllegalArgumentException("sadhanaDate Date cannot be in future");


        participantSadhanaRepository.save(ParticipantSadhanaEntity.builder()
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
                .programId(programId)
                .programName(programName)
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
                .sadhanaDate(sadhanaDateString)
                .build());
    }

    @Override
    public @NonNull Page<ParticipantSadhana> getParticipantSadhana(@NonNull Pageable page) {
        Page<ParticipantSadhanaEntity> pagedResult = participantSadhanaRepository.findAll(page);
        if (pagedResult.hasContent()) {
            Page<ParticipantSadhana> participantSadhanaPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantSadhanaFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantSadhanaPage);
            return participantSadhanaPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    public ParticipantSadhana getParticipantSadhanaById(@NonNull Long id) {
        if(id <= 0L)
            return null;
        Optional<ParticipantSadhanaEntity> participantSadhanaEntity = participantSadhanaRepository.findById(id);
        return participantSadhanaEntity.map(this::participantSadhanaFromEntity).orElse(null);
    }

    @Override
    public @NonNull Page<ParticipantSadhana> getParticipantSadhanaByAny(String programName, String participantFirstName, String participantLastName,
                                                                         String participantContactNumber, String sadhanaDate,
                                                                         @NonNull Pageable page) {
        if(null != sadhanaDate && !sadhanaDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            try {
                Date activityDt = formatter.parse(sadhanaDate);
                Date current = new Date();
                if (current.before(activityDt))
                    return Page.empty(PageRequest.of(0, 10, Sort.by("id")));

            } catch(ParseException ex) {
                return Page.empty(PageRequest.of(0, 10, Sort.by("id")));
            }
        }
        Page<ParticipantSadhanaEntity> pagedResult = participantSadhanaRepository.findAllByAny(
                programName, participantFirstName, participantLastName,
                participantContactNumber, sadhanaDate, page);
        if (pagedResult.hasContent()) {
            Page<ParticipantSadhana> participantSadhanaPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantSadhanaFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantSadhanaPage);
            return participantSadhanaPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private ParticipantSadhana participantSadhanaFromEntity(@NonNull ParticipantSadhanaEntity participantSadhanaEntity) {
        Long id = participantSadhanaEntity.getId();
        if (id <= 0)
            return null;

        //Program
        Long programId = participantSadhanaEntity.getProgramId();
        if(programId <= 0)
            return null;
        String programName = participantSadhanaEntity.getProgramName();
        if(null == programName || programName.isEmpty())
            return null;


        //Participant
        Long participantId = participantSadhanaEntity.getParticipantId();
        if(participantId <= 0)
            return null;
        String participantFirstName = participantSadhanaEntity.getParticipantFirstName();
        if(null == participantFirstName || participantFirstName.isEmpty())
            return null;
        String participantLastName = participantSadhanaEntity.getParticipantLastName();
        if(null == participantLastName || participantLastName.isEmpty())
            return null;
        String participantWaNumber = participantSadhanaEntity.getParticipantWaNumber();
        if(null == participantWaNumber || participantWaNumber.isEmpty())
            return null;
        String participantContactNumber = participantSadhanaEntity.getParticipantContactNumber();
        if(null == participantContactNumber || participantContactNumber.isEmpty())
            return null;

        //Sadhana
        Integer numberOfRounds = participantSadhanaEntity.getNumberOfRounds();
        LocalTime first8RoundsCompletedTime = participantSadhanaEntity.getFirst8RoundsCompletedTime();
        LocalTime next8RoundsCompletedTime = participantSadhanaEntity.getNext8RoundsCompletedTime();
        LocalTime wakeUpTime = participantSadhanaEntity.getWakeUpTime();
        LocalTime sleepTime = participantSadhanaEntity.getSleepTime();
        Integer prabhupadaBookReading = participantSadhanaEntity.getPrabhupadaBookReading();
        Integer nonPrabhupadaBookReading = participantSadhanaEntity.getNonPrabhupadaBookReading();
        Integer prabhupadaClassHearing = participantSadhanaEntity.getPrabhupadaClassHearing();
        Integer guruClassHearing = participantSadhanaEntity.getGuruClassHearing();
        Integer otherClassHearing = participantSadhanaEntity.getOtherClassHearing();
        String speaker = participantSadhanaEntity.getSpeaker();
        Boolean attendedArti = participantSadhanaEntity.getAttendedArti();
        Integer mobileInternetUsage = participantSadhanaEntity.getMobileInternetUsage();
        String topic = participantSadhanaEntity.getTopic();
        String visibleSadhana = participantSadhanaEntity.getVisibleSadhana();

        String sadhanaDateString = participantSadhanaEntity.getSadhanaDate();
        if(null == sadhanaDateString || sadhanaDateString.isEmpty())
            return null;

        Date created = participantSadhanaEntity.getCreated();
        Date modified = participantSadhanaEntity.getModified();

        return ParticipantSadhana.builder()
                .id(id)
                .programId(programId)
                .programName(programName)
                .participantId(participantId)
                .participantFirstName(participantFirstName)
                .participantLastName(participantLastName)
                .participantWaNumber(participantWaNumber)
                .participantContactNumber(participantContactNumber)
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
                .topic(topic)
                .visibleSadhana(visibleSadhana)
                .sadhanaDate(sadhanaDateString)
                .created(created)
                .modified(modified)
                .build();
    }
}
