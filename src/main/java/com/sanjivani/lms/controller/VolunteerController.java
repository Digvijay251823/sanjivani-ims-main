package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.VolunteerService;
import com.sanjivani.lms.model.Response;
import com.sanjivani.lms.model.Volunteer;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    private static final Logger LOG = LoggerFactory.getLogger(VolunteerController.class);
    @Autowired
    VolunteerService volunteerService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Volunteer volunteer, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(volunteer.toString());
            volunteerService.save(volunteer);
            model.addText("Successfully created Volunteer");
            return new ResponseEntity<>(Response.builder().message("Successfully created Volunteer").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Volunteer>> getVolunteers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(volunteerService.getVolunteers(pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Volunteer> getVolunteerById(@NonNull @PathVariable Long id) {
        Volunteer volunteer = volunteerService.getVolunteerById(id);
        if (volunteer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(volunteer);
    }

    @GetMapping(value = "/email/{email}")
    @ResponseBody
    public ResponseEntity<Page<Volunteer>> getVolunteerByEmail(@NonNull @PathVariable String email,
                                                         @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(volunteerService.getVolunteersByEmail(email, pageable));
    }

}
