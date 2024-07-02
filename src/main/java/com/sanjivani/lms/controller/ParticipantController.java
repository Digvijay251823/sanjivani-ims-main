package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ParticipantService;
import com.sanjivani.lms.model.JapaRoundsParticipantCount;
import com.sanjivani.lms.model.Participant;
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

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantController.class);
    @Autowired
    ParticipantService participantService;

    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Participant participant, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(participant.toString());
            participantService.save(participant);
            model.addText("Successfully created Participant");
            return new ResponseEntity<>(Response.builder().message("Successfully created Participant").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Participant>> getParticipants(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getParticipants(pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Participant> getParticipantById(@NonNull @PathVariable Long id) {
        Participant participant = participantService.getParticipantById(id);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(participant);
    }

    @GetMapping(value = "/email/{email}")
    @ResponseBody
    public ResponseEntity<Page<Participant>> getParticipantByEmail(@NonNull @PathVariable String email,
                                                                @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getParticipantsByEmail(email, pageable));
    }

    @GetMapping(value = "/phone/{contactNumber}")
    @ResponseBody
    public ResponseEntity<Participant> getParticipantByContactNumber(@NonNull @PathVariable String contactNumber) {
        Participant participant = participantService.getParticipantByContactNumber(contactNumber);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(participant);
    }

    @GetMapping(value = "/first-name/{firstName}")
    @ResponseBody
    public ResponseEntity<Page<Participant>> getParticipantByFirstName(@NonNull @PathVariable String firstName,
                                                                   @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getParticipantsByFirstName(firstName, pageable));
    }

    @GetMapping(value = "/last-name/{lastName}")
    @ResponseBody
    public ResponseEntity<Page<Participant>> getParticipantByLastName(@NonNull @PathVariable String lastName,
                                                                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getParticipantsByLastName(lastName, pageable));
    }

    @GetMapping(value = "/chant/{rounds}")
    @ResponseBody
    public ResponseEntity<Page<Participant>> getParticipantByEmail(@NonNull @PathVariable Integer rounds,
                                                                   @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantService.getParticipantsByJapaRounds(rounds, pageable));
    }

    @GetMapping(value = "/stats/japa-rounds-participant-count")
    @ResponseBody
    public ResponseEntity<List<JapaRoundsParticipantCount>> getStatsJapaRoundsParticipantCount() {
        return ResponseEntity.ok(participantService.getGroupByCountOfJapaRounds());
    }
}
