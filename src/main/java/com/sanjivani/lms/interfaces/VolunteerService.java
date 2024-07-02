package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Volunteer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VolunteerService {
    Page<Volunteer> getVolunteers(Pageable pageable);
    void save(Volunteer volunteer) throws IllegalArgumentException;
    Volunteer getVolunteerById(Long id) throws IllegalArgumentException;

    Page<Volunteer> getVolunteersByInitiatedName(String initiatedName, Pageable pageable) throws IllegalArgumentException;
    Page<Volunteer> getVolunteersByFirstName(String firstName, Pageable pageable) throws IllegalArgumentException;
    Page<Volunteer> getVolunteersByLastName(String lastName, Pageable pageable) throws IllegalArgumentException;
    Page<Volunteer> getVolunteersByWaNumber(String waNumber, Pageable pageable) throws IllegalArgumentException;
    Page<Volunteer> getVolunteersByContactNumber(String contactNumber, Pageable pageable) throws IllegalArgumentException;
    Page<Volunteer> getVolunteersByEmail(String emailId, Pageable pageable) throws IllegalArgumentException;

}
