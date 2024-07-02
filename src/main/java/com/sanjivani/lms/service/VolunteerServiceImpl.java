package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.interfaces.AuthService;
import com.sanjivani.lms.interfaces.VolunteerService;
import com.sanjivani.lms.model.Course;
import com.sanjivani.lms.model.Volunteer;
import com.sanjivani.lms.repository.RoleRepository;
import com.sanjivani.lms.repository.UserRepository;
import com.sanjivani.lms.repository.UserRoleRepository;
import com.sanjivani.lms.repository.VolunteerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class VolunteerServiceImpl implements VolunteerService {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteerServiceImpl.class);

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    AuthService authService;

    @Override
    public Page<Volunteer> getVolunteers(Pageable pageable) {
        Page<VolunteerEntity> pagedResult = volunteerRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<Volunteer> volunteerPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::volunteerFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, volunteerPage);
            return volunteerPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    @Override
    @Transactional
    public void save(@NonNull Volunteer volunteer) throws IllegalArgumentException {
        String firstName = volunteer.getFirstName();
        if(firstName.isEmpty())
            throw new IllegalArgumentException("First Name cannot be empty");

        String lastName = volunteer.getLastName();
        if(lastName.isEmpty())
            throw new IllegalArgumentException("Last Name cannot be empty");

        String initiatedName = volunteer.getInitiatedName();
        String waNumber = volunteer.getWaNumber();
        if(waNumber.isEmpty())
            throw new IllegalArgumentException("WhatsApp Number cannot be empty");
        String contactNumber = volunteer.getContactNumber();
        if(contactNumber.isEmpty())
            throw new IllegalArgumentException("Contact Number cannot be empty");
        String email = volunteer.getEmail();
        if(email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");
        String password = volunteer.getPassword();
        if(password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        Integer age = volunteer.getAge();
        Gender gender = volunteer.getGender();
        String address = volunteer.getAddress();
        String serviceInterests = volunteer.getServiceInterests();
        String currentServices = volunteer.getCurrentServices();
        String createdBy;
        if(null == volunteer.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = volunteer.getCreatedBy();

        volunteerRepository.save(VolunteerEntity.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .initiatedName(initiatedName)
                        .waNumber(waNumber)
                        .contactNumber(contactNumber)
                        .email(email)
                        .age(age)
                        .gender(gender)
                        .address(address)
                        .serviceInterests(serviceInterests)
                        .currentServices(currentServices)
                        .createdBy(createdBy)
                        .build());

        authService.register(email, password, "ROLE_VOLUNTEER");
    }

    @Override
    public Volunteer getVolunteerById(@NonNull Long id) throws IllegalArgumentException {
        if(id <= 0L)
            return null;
        Optional<VolunteerEntity> volunteerEntity = volunteerRepository.findById(id);
        return volunteerEntity.map(this::volunteerFromEntity).orElse(null);
    }

    @Override
    public Page<Volunteer> getVolunteersByInitiatedName(String initiatedName, Pageable pageable) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Page<Volunteer> getVolunteersByFirstName(String firstName, Pageable pageable) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Page<Volunteer> getVolunteersByLastName(String lastName, Pageable pageable) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Page<Volunteer> getVolunteersByWaNumber(String waNumber, Pageable pageable) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Page<Volunteer> getVolunteersByContactNumber(String contactNumber, Pageable pageable) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Page<Volunteer> getVolunteersByEmail(@NonNull String emailId, Pageable pageable) throws IllegalArgumentException {
        if(emailId.isEmpty())
            return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
        Page<VolunteerEntity> pagedResult = volunteerRepository.findAllByEmail(emailId, pageable);
        if (pagedResult.hasContent()) {
            Page<Volunteer> volunteerPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::volunteerFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, volunteerPage);
            return volunteerPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("id"))); // Regression Spring Boot 3.2.0
    }

    private Volunteer volunteerFromEntity(@NonNull VolunteerEntity volunteerEntity) {
        Long id = volunteerEntity.getId();
        if( null == id || id <= 0L)
            return null;
        String firstName = volunteerEntity.getFirstName();
        if(firstName.isEmpty())
            return null;

        String lastName = volunteerEntity.getLastName();
        if(lastName.isEmpty())
            return null;

        String initiatedName = volunteerEntity.getInitiatedName();
        String waNumber = volunteerEntity.getWaNumber();
        if(waNumber.isEmpty())
            return null;
        String contactNumber = volunteerEntity.getContactNumber();
        if(contactNumber.isEmpty())
            return null;
        String email = volunteerEntity.getEmail();
        if(email.isEmpty())
            return null;

        Integer age = volunteerEntity.getAge();
        Gender gender = volunteerEntity.getGender();
        String address = volunteerEntity.getAddress();
        String serviceInterests = volunteerEntity.getServiceInterests();
        String currentServices = volunteerEntity.getCurrentServices();

        String createdBy;
        if(null == volunteerEntity.getCreatedBy())
            createdBy = "SYSTEM";
        else
            createdBy = volunteerEntity.getCreatedBy();

        Date created = volunteerEntity.getCreated();
        Date modified = volunteerEntity.getModified();

        return Volunteer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .initiatedName(initiatedName)
                .waNumber(waNumber)
                .contactNumber(contactNumber)
                .email(email)
                .age(age)
                .gender(gender)
                .address(address)
                .serviceInterests(serviceInterests)
                .currentServices(currentServices)
                .createdBy(createdBy)
                .created(created)
                .modified(modified)
                .build();
    }
}
