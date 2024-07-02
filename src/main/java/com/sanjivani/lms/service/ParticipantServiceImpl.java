package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.ParticipantEntity;
import com.sanjivani.lms.entity.ParticipantTopicEntity;
import com.sanjivani.lms.entity.ParticipantTopicKey;
import com.sanjivani.lms.entity.TopicEntity;
import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.ParticipantStatus;
import com.sanjivani.lms.interfaces.JapaRoundsParticipantCountInterface;
import com.sanjivani.lms.interfaces.ParticipantService;
import com.sanjivani.lms.model.JapaRoundsParticipantCount;
import com.sanjivani.lms.model.Participant;
import com.sanjivani.lms.model.Topic;
import com.sanjivani.lms.repository.ParticipantRepository;
import com.sanjivani.lms.repository.ParticipantTopicRepository;
import com.sanjivani.lms.repository.TopicRepository;
import jakarta.transaction.Transactional;
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
public class ParticipantServiceImpl implements ParticipantService {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantTopicRepository participantTopicRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Override
    @Transactional
    public Page<Participant> getParticipants(Pageable pageable) {
        Page<ParticipantEntity> pagedResult = participantRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public void save(@NonNull Participant participant) throws IllegalArgumentException {
        String firstName = participant.getFirstName();
        if(firstName.isEmpty())
            throw new IllegalArgumentException("First Name cannot be empty");

        String lastName = participant.getLastName();
        if(lastName.isEmpty())
            throw new IllegalArgumentException("Last Name cannot be empty");

        String waNumber = participant.getWaNumber();
        if(waNumber.isEmpty())
            throw new IllegalArgumentException("WhatsApp Number cannot be empty");
        String contactNumber = participant.getContactNumber();
        if(contactNumber.isEmpty())
            throw new IllegalArgumentException("Contact Number cannot be empty");

        String email = participant.getEmail();
        Integer japaRounds = participant.getJapaRounds();
        if (null == japaRounds || japaRounds < 0)
            japaRounds = 0;
        Integer age = participant.getAge();
        Gender gender = participant.getGender();
        String address = participant.getAddress();
        String city = participant.getCity();
        String maritalStatus = participant.getMaritalStatus();
        String education = participant.getEducation();
        String occupation = participant.getOccupation();
        String reference = participant.getReference();
        String notes = participant.getNotes();
        ParticipantStatus status = participant.getStatus();
        Integer numberOfChildren = participant.getNumberOfChildren();
        String createdBy;
        if(null == participant.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = participant.getCreatedBy();

        ParticipantEntity participantEntity = participantRepository.save(ParticipantEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .waNumber(waNumber)
                .contactNumber(contactNumber)
                .email(email)
                .japaRounds(japaRounds)
                .age(age)
                .gender(gender)
                .address(address)
                .city(city)
                .maritalStatus(maritalStatus)
                .education(education)
                .occupation(occupation)
                .reference(reference)
                .notes(notes)
                .status(status)
                .numberOfChildren(numberOfChildren)
                .createdBy(createdBy)
                .build());
        Long participantId = participantEntity.getId();
        if(null == participantId || participantId <= 0L)
            throw new IllegalArgumentException("Participant ID cannot be empty");
        List<Topic> interestedTopics = participant.getInterestedTopics();
        if(null != interestedTopics && !interestedTopics.isEmpty()) {
            interestedTopics.forEach((it) -> {
                Long topicId = it.getId();
                if(null == topicId || topicId <= 0L)
                    throw new IllegalArgumentException("Topic ID cannot be empty");
                TopicEntity topicEntity = topicRepository.findById(topicId).orElse(null);
                if(null == topicEntity)
                    throw new IllegalArgumentException("Topic not found");
                participantTopicRepository.save(ParticipantTopicEntity.builder()
                        .id(ParticipantTopicKey.builder()
                                .participant(participantId)
                                .topic(topicId)
                                .build())
                        .topic(topicEntity)
                        .participant(participantEntity)
                        .build());
            });
        }
    }

    @Override
    @Transactional
    public Participant getParticipantById(@NonNull Long id) throws IllegalArgumentException {
        if(id <= 0L)
            return null;
        Optional<ParticipantEntity> participantEntity = participantRepository.findById(id);
        return participantEntity.map(this::participantFromEntity).orElse(null);
    }

    @Override
    @Transactional
    public Page<Participant> getParticipantsByFirstName(String firstName, Pageable pageable) throws IllegalArgumentException {
        if(firstName.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
        Page<ParticipantEntity> pagedResult = participantRepository.findAllByFirstName(firstName, pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public Page<Participant> getParticipantsByLastName(String lastName, Pageable pageable) throws IllegalArgumentException {
        if(lastName.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
        Page<ParticipantEntity> pagedResult = participantRepository.findAllByLastName(lastName, pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public Page<Participant> getParticipantsByWaNumber(String waNumber, Pageable pageable) throws IllegalArgumentException {
        if(waNumber.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
        Page<ParticipantEntity> pagedResult = participantRepository.findAllByWaNumber(waNumber, pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public Participant getParticipantByContactNumber(@NonNull String contactNumber)  {
        if(contactNumber.isEmpty())
            return null;
        Optional<ParticipantEntity> participantEntity = participantRepository.findByContactNumber(contactNumber);
        return participantEntity.map(this::participantFromEntity).orElse(null);
    }

    @Override
    @Transactional
    public Page<Participant> getParticipantsByEmail(@NonNull String emailId, Pageable pageable) throws IllegalArgumentException {
        if(emailId.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
        Page<ParticipantEntity> pagedResult = participantRepository.findAllByEmail(emailId, pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public Page<Participant> getParticipantsByJapaRounds(Integer rounds, Pageable pageable) throws IllegalArgumentException {
        if(rounds < 0 || rounds > 16)
            throw new IllegalArgumentException("Rounds cannot be negative or more than 16");
        Page<ParticipantEntity> pagedResult = participantRepository.findAllByJapaRounds(rounds, pageable);
        if (pagedResult.hasContent()) {
            Page<Participant> participantPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::participantFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, participantPage);
            return participantPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @NonNull
    public List<JapaRoundsParticipantCount> getGroupByCountOfJapaRounds() {
        List<JapaRoundsParticipantCountInterface> japaRoundsParticipantCounts = participantRepository.findGroupByCountOfJapaRounds();
        return japaRoundsParticipantCounts.stream()
                .map((japaRoundsParticipantCount) -> JapaRoundsParticipantCount.builder()
                        .japaRounds(japaRoundsParticipantCount.getJapaRounds())
                        .participantCount(japaRoundsParticipantCount.getParticipantCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateJapaRounds(@NonNull Long id, @NonNull Integer japaRounds) throws IllegalArgumentException {
        if(id <= 0L)
            throw new IllegalArgumentException("Participant ID cannot be empty");
        if (japaRounds < 0)
            japaRounds = 0;
        participantRepository.updateJapaRounds(id, japaRounds);
    }

    private Topic topicFromParticipantTopicEntity(@NonNull ParticipantTopicEntity participantTopicEntity) {
        TopicEntity topicEntity = participantTopicEntity.getTopic();
        if(null == topicEntity)
            return null;
        Long id = topicEntity.getId();
        if( null == id || id <= 0L)
            return null;
        String name = topicEntity.getName();
        if(name.isEmpty())
            return null;
        String description = topicEntity.getDescription();
        if(description.isEmpty())
            return null;
        String createdBy;
        if(null == topicEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = topicEntity.getCreatedBy();

        Date created = topicEntity.getCreated();
        Date modified = topicEntity.getModified();

        return Topic.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }

    private Participant participantFromEntity(@NonNull ParticipantEntity participantEntity) {
        Long id = participantEntity.getId();
        if( null == id || id <= 0L)
            return null;
        String firstName = participantEntity.getFirstName();
        if(firstName.isEmpty())
            return null;

        String lastName = participantEntity.getLastName();
        if(lastName.isEmpty())
            return null;

        String waNumber = participantEntity.getWaNumber();
        if(waNumber.isEmpty())
            return null;
        String contactNumber = participantEntity.getContactNumber();
        if(contactNumber.isEmpty())
            return null;

        List<Topic> interestedTopics = participantTopicRepository.findByIdParticipant(id)
                .stream()
                .map(this::topicFromParticipantTopicEntity)
                .toList();

        String email = participantEntity.getEmail();
        Integer japaRounds = participantEntity.getJapaRounds();
        Integer age = participantEntity.getAge();
        Gender gender = participantEntity.getGender();
        String address = participantEntity.getAddress();
        String city = participantEntity.getCity();
        String maritalStatus = participantEntity.getMaritalStatus();
        String education = participantEntity.getEducation();
        String occupation = participantEntity.getOccupation();
        String reference = participantEntity.getReference();
        String notes = participantEntity.getNotes();
        ParticipantStatus status = participantEntity.getStatus();
        Integer numberOfChildren = participantEntity.getNumberOfChildren();

        String createdBy;
        if(null == participantEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = participantEntity.getCreatedBy();

        Date created = participantEntity.getCreated();
        Date modified = participantEntity.getModified();

        return Participant.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .waNumber(waNumber)
                .contactNumber(contactNumber)
                .email(email)
                .japaRounds(japaRounds)
                .interestedTopics(interestedTopics)
                .age(age)
                .gender(gender)
                .address(address)
                .city(city)
                .maritalStatus(maritalStatus)
                .education(education)
                .occupation(occupation)
                .reference(reference)
                .notes(notes)
                .status(status)
                .numberOfChildren(numberOfChildren)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
