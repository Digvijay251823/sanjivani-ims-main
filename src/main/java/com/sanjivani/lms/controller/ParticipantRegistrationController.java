package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ParticipantRegistrationService;
import com.sanjivani.lms.model.ParticipantRegistration;
import com.sanjivani.lms.model.Response;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/participant-registration")
public class ParticipantRegistrationController {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantRegistrationController.class);
    @Autowired
    ParticipantRegistrationService participantRegistrationService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<Response> register(@Valid @NonNull @RequestBody final ParticipantRegistration participantRegistration, Errors errors, Model model) throws Exception {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(participantRegistration.toString());
            participantRegistrationService.save(participantRegistration);
            model.addText("Successfully registered participant");
            return new ResponseEntity<>(Response.builder().message("Successfully registered participant").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantRegistration>> getParticipantRegistration(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantRegistrationService.getParticipantRegistration(pageable));
    }

    @GetMapping(value = "/filter/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantRegistration>> getParticipantRegistrationByAny( @RequestParam(name = "levelName", required = false) String levelName,
                                                                                  @RequestParam(name = "levelDisplayName", required = false) String levelDisplayName,
                                                                                  @RequestParam(name = "participantContactNumber", required = false) String participantContactNumber,
                                                                                  @RequestParam(name = "participantFirstName", required = false) String participantFirstName,
                                                                                  @RequestParam(name = "participantLastName", required = false) String participantLastName,
                                                                                  @RequestParam(name = "programName", required = false) String programName,
                                                                                  @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if( (null != levelName && !levelName.isEmpty()) ||
                (null != levelDisplayName && !levelDisplayName.isEmpty()) ||
                (null != participantContactNumber && !participantContactNumber.isEmpty()) ||
                (null != participantFirstName && !participantFirstName.isEmpty()) ||
                (null != participantLastName && !participantLastName.isEmpty()) ||
                (null != programName && !programName.isEmpty()) ) {
            return ResponseEntity.ok(participantRegistrationService.getParticipantRegistrationByAny(levelName, levelDisplayName,
                    participantContactNumber, participantFirstName,
                    participantLastName, programName, pageable));
        }
        return ResponseEntity.ok(participantRegistrationService.getParticipantRegistration(pageable));
    }
}
