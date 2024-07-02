package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ParticipantActivityService;
import com.sanjivani.lms.interfaces.ParticipantSadhanaService;
import com.sanjivani.lms.model.ParticipantActivity;
import com.sanjivani.lms.model.ParticipantSadhana;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RestController
@RequestMapping("/participant-sadhana")
public class ParticipantSadhanaController {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantSadhanaController.class);
    @Autowired
    ParticipantSadhanaService participantSadhanaService;

    @PostMapping(value = "/record")
    @ResponseBody
    public ResponseEntity<Response> record(@Valid @NonNull @RequestBody final ParticipantSadhana participantSadhana, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(participantSadhana.toString());
            participantSadhanaService.save(participantSadhana);
            model.addText("Successfully recorded Participant Sadhana");
            return new ResponseEntity<>(Response.builder().message("Successfully recorded Participant Sadhana").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantSadhana>> getParticipantSadhana(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantSadhanaService.getParticipantSadhana(pageable));
    }

    @GetMapping(value = "/filter/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantSadhana>> getParticipantActivityByAny( @RequestParam(name = "programName", required = false) String programName,
                                                                                 @RequestParam(name = "participantFirstName", required = false) String participantFirstName,
                                                                                 @RequestParam(name = "participantLastName", required = false) String participantLastName,
                                                                                 @RequestParam(name = "participantContactNumber", required = false) String participantContactNumber,
                                                                                 @RequestParam(name = "date", required = false) String sadhanaDate,
                                                                                 @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if(null != sadhanaDate && !sadhanaDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            try {
                Date sadhanaDt = formatter.parse(sadhanaDate);
                Date current = new Date();
                if (current.before(sadhanaDt))
                    return ResponseEntity.ok(participantSadhanaService.getParticipantSadhana(pageable));

            } catch(ParseException ex) {
                return ResponseEntity.ok(participantSadhanaService.getParticipantSadhana(pageable));
            }
        }
        if( (null != participantContactNumber && !participantContactNumber.isEmpty()) ||
                (null != participantFirstName && !participantFirstName.isEmpty()) ||
                (null != participantLastName && !participantLastName.isEmpty()) ||
                (null != programName && !programName.isEmpty()) ||
                null != sadhanaDate && !sadhanaDate.isEmpty()) {
            return ResponseEntity.ok(participantSadhanaService.getParticipantSadhanaByAny(
                    programName, participantFirstName, participantLastName,
                    participantContactNumber,  sadhanaDate, pageable));
        }
        return ResponseEntity.ok(participantSadhanaService.getParticipantSadhana(pageable));
    }
}
