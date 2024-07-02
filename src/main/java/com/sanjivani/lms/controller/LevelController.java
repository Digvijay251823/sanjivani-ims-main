package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.LevelService;
import com.sanjivani.lms.model.Level;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/level")
public class LevelController {

    private static final Logger LOG = LoggerFactory.getLogger(LevelController.class);
    @Autowired
    LevelService levelService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_VOLUNTEER', 'ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Level level, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(level.toString());
            levelService.save(level);
            model.addText("Successfully created Level");
            return new ResponseEntity<>(Response.builder().message("Successfully created Level").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Level>> getLevels(@PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(levelService.getLevels(pageable));
    }

    @GetMapping(value = "/program/{name}")
    @ResponseBody
    public ResponseEntity<Page<Level>> getLevelsByProgram(@NonNull @PathVariable String name,
                                                          @PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(levelService.getLevelsByProgram(name, pageable));
    }

    @GetMapping(value = "/status/{status}")
    @ResponseBody
    public ResponseEntity<Page<Level>> getLevelsByStatus(@NonNull @PathVariable String status,
                                                         @PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(levelService.getLevelsByStatus(status, pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Level> getLevelById(@NonNull @PathVariable Long id) {
        Level level = levelService.getLevelById(id);
        if (null == level)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(level);
    }

    @GetMapping(value = "/new-participant-levels")
    @ResponseBody
    public ResponseEntity<Page<Level>> getLevelsAcceptingNewParticipants(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(levelService.getLevelsByAcceptingNewParticipants(true, pageable));
    }
}
