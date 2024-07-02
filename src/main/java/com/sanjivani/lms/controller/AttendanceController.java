package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.AttendanceService;
import com.sanjivani.lms.model.Attendance;
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
@RequestMapping("/attendance")
public class AttendanceController {

    private static final Logger LOG = LoggerFactory.getLogger(AttendanceController.class);
    @Autowired
    AttendanceService attendanceService;

    @PostMapping(value = "/mark")
    @ResponseBody
    public ResponseEntity<Response> mark(@Valid @NonNull @RequestBody final Attendance attendance, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(attendance.toString());
            attendanceService.save(attendance);
            model.addText("Successfully marked attendance");
            return new ResponseEntity<>(Response.builder().message("Successfully marked attendance").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/session/{id}")
    @ResponseBody
    public ResponseEntity<Page<Attendance>> getAttendanceBySession(@NonNull @PathVariable Long id, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySession(id, pageable));
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Attendance>> getAttendance(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(attendanceService.getAttendance(pageable));
    }

    @GetMapping(value = "/find/")
    @ResponseBody
    public ResponseEntity<Attendance> getAttendanceByParticipantAndSession(@NonNull @RequestParam(name = "participantId", required = false) Long participantId,
                                                                           @NonNull @RequestParam(name = "scheduledSessionId", required = false) Long scheduledSessionId) {
        Attendance attendance = attendanceService.getAttendanceByParticipantAndSession(participantId, scheduledSessionId);
        if (null == attendance)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(attendance);
    }
}
