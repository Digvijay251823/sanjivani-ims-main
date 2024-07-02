package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ProgramService;
import com.sanjivani.lms.model.Program;
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
@RequestMapping("/program")
public class ProgramController {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);
    @Autowired
    ProgramService programService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Program program, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(program.toString());
            programService.save(program);
            model.addText("Successfully created Program");
            return new ResponseEntity<>(Response.builder().message("Successfully created Program").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Program>> getPrograms(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(programService.getPrograms(pageable));
    }

    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public ResponseEntity<Program> getProgramByName(@NonNull @PathVariable String name) {
        Program program = programService.getProgramByName(name);
        if (null == program)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(program);
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Program> getProgramById(@NonNull @PathVariable Long id) {
        Program program = programService.getProgramById(id);
        if (null == program)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(program);
    }

}
