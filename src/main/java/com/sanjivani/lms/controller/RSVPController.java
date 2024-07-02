package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.AttendanceService;
import com.sanjivani.lms.interfaces.RSVPService;
import com.sanjivani.lms.model.Attendance;
import com.sanjivani.lms.model.RSVP;
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
@RequestMapping("/rsvp")
public class RSVPController {

    private static final Logger LOG = LoggerFactory.getLogger(RSVPController.class);
    @Autowired
    RSVPService rsvpService;

    @PostMapping(value = "/mark")
    @ResponseBody
    public ResponseEntity<Response> mark(@Valid @NonNull @RequestBody final RSVP rsvp, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(rsvp.toString());
            rsvpService.save(rsvp);
            model.addText("Successfully marked rsvp");
            return new ResponseEntity<>(Response.builder().message("Successfully marked rsvp").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/session/{id}")
    @ResponseBody
    public ResponseEntity<Page<RSVP>> getRSVPBySession(@NonNull @PathVariable Long id, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(rsvpService.getRSVPBySession(id, pageable));
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<RSVP>> getRSVP(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(rsvpService.getRSVP(pageable));
    }

    @GetMapping(value = "/find/")
    @ResponseBody
    public ResponseEntity<RSVP> getRSVPByParticipantAndSession(@NonNull @RequestParam(name = "participantId", required = false) Long participantId,
                                                                     @NonNull @RequestParam(name = "scheduledSessionId", required = false) Long scheduledSessionId) {
        RSVP rsvp = rsvpService.getRSVPByParticipantAndSession(participantId, scheduledSessionId);
        if (null == rsvp)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(rsvp);
    }
}
