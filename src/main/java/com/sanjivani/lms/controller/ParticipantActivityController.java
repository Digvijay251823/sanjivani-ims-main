package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ParticipantActivityService;
import com.sanjivani.lms.model.ParticipantActivity;
import com.sanjivani.lms.model.Response;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/participant-activity")
public class ParticipantActivityController {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantActivityController.class);
    @Autowired
    ParticipantActivityService participantActivityService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<Response> register(@Valid @NonNull @RequestBody final ParticipantActivity participantActivity, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(participantActivity.toString());
            try {
                participantActivityService.save(participantActivity);
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
                return new ResponseEntity<>(Response.builder().message(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            model.addText("Successfully registered activity");
            return new ResponseEntity<>(Response.builder().message("Successfully registered activity").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantActivity>> getParticipantActivity(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(participantActivityService.getParticipantActivity(pageable));
    }

    @GetMapping(value = "/filter/")
    @ResponseBody
    public ResponseEntity<Page<ParticipantActivity>> getParticipantActivityByAny( @RequestParam(name = "activityName", required = false) String activityName,
                                                                                  @RequestParam(name = "courseCode", required = false) String courseCode,
                                                                                  @RequestParam(name = "levelName", required = false) String levelName,
                                                                                  @RequestParam(name = "participantContactNumber", required = false) String participantContactNumber,
                                                                                  @RequestParam(name = "participantFirstName", required = false) String participantFirstName,
                                                                                  @RequestParam(name = "participantLastName", required = false) String participantLastName,
                                                                                  @RequestParam(name = "programName", required = false) String programName,
                                                                                  @RequestParam(name = "scheduledSessionName", required = false) String scheduledSessionName,
                                                                                  @RequestParam(name = "date", required = false) String activityDate,
                                                                                  @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if(null != activityDate && !activityDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            try {
                Date activityDt = formatter.parse(activityDate);
                Date current = new Date();
                if (current.before(activityDt))
                    return ResponseEntity.ok(participantActivityService.getParticipantActivity(pageable));

            } catch(ParseException ex) {
                return ResponseEntity.ok(participantActivityService.getParticipantActivity(pageable));
            }
        }
        if( (null != activityName && !activityName.isEmpty()) ||
                (null != courseCode && !courseCode.isEmpty()) ||
                (null != levelName && !levelName.isEmpty()) ||
                (null != participantContactNumber && !participantContactNumber.isEmpty()) ||
                (null != participantFirstName && !participantFirstName.isEmpty()) ||
                (null != participantLastName && !participantLastName.isEmpty()) ||
                (null != programName && !programName.isEmpty()) ||
                (null != scheduledSessionName && !scheduledSessionName.isEmpty()) ||
                null != activityDate && !activityDate.isEmpty()) {
            return ResponseEntity.ok(participantActivityService.getParticipantActivityByAny(activityName, courseCode, levelName,
                    participantContactNumber, participantFirstName,
                    participantLastName, programName,
                    scheduledSessionName, activityDate ,pageable));
        }
        return ResponseEntity.ok(participantActivityService.getParticipantActivity(pageable));
    }
}
