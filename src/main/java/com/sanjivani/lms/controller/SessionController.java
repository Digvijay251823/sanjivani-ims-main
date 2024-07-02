package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.model.Response;
import com.sanjivani.lms.model.ScheduledSession;
import com.sanjivani.lms.model.Session;
import com.sanjivani.lms.interfaces.SessionService;
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
@RequestMapping("/session")
public class SessionController {

    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);
    @Autowired
    SessionService sessionService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Session session, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(session.toString());
            sessionService.save(session);
            model.addText("Successfully created Session");
            return new ResponseEntity<>(Response.builder().message("Successfully created Session").build(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/schedule")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_VOLUNTEER', 'ROLE_ADMIN')")
    public ResponseEntity<Response> schedule(@Valid @NonNull @RequestBody final ScheduledSession scheduledSession, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                LOG.info(scheduledSession.toString());
                sessionService.schedule(scheduledSession);
                model.addText("Successfully scheduled Session");
            } catch(Exception ex) {
                LOG.error("Exception occurred while scheduling session: {}", ex.getMessage());
                return new ResponseEntity<>(Response.builder().message(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Response.builder().message("Successfully scheduled Session").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Session>> getSessions(@PageableDefault(size = 10, sort = "code", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(sessionService.getSessions(pageable));
    }

    @GetMapping(value = "/course/{code}")
    @ResponseBody
    public ResponseEntity<Page<Session>> getSessionsByCourse(@NonNull @PathVariable String code, @PageableDefault(size = 10, sort = "code", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(sessionService.getSessionsByCourse(code, pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Session> getSessionById(@NonNull @PathVariable Long id) {
        Session session = sessionService.getSessionById(id);
        if (null == session)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(session);
    }

    @GetMapping(value = "/code/{code}")
    @ResponseBody
    public ResponseEntity<Session> getSessionByCode(@NonNull @PathVariable String code) {
        Session session = sessionService.getSessionByCode(code);
        if (null == session)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(session);
    }

    @GetMapping(value = "/scheduled/{id}")
    @ResponseBody
    public ResponseEntity<ScheduledSession> getScheduledSession(@NonNull @PathVariable Long id) {
        ScheduledSession scheduledSession = sessionService.getScheduledSession(id);
        if (null == scheduledSession)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(scheduledSession);
    }

    @GetMapping(value = "/scheduled/level/{id}")
    @ResponseBody
    public ResponseEntity<Page<ScheduledSession>> getScheduledSessionsByLevel(@NonNull @PathVariable Long id, @PageableDefault(size = 10, sort = "startTime", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(sessionService.getScheduledSessionsByLevel(id, pageable));
    }

}
