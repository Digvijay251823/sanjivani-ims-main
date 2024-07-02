package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.SadhanaFormService;
import com.sanjivani.lms.model.Response;
import com.sanjivani.lms.model.SadhanaForm;
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
@RequestMapping("/sadhana-form")
public class SadhanaFormController {

    private static final Logger LOG = LoggerFactory.getLogger(SadhanaFormController.class);
    @Autowired
    SadhanaFormService sadhanaFormService;

    @PostMapping(value = "/generate")
    @ResponseBody
    public ResponseEntity<Long> generate(@Valid @NonNull @RequestBody final SadhanaForm sadhanaForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(sadhanaForm.toString());
            Long id = sadhanaFormService.save(sadhanaForm);
            if(null == id || id <= 0) {
                model.addText("Failed to save form");
                return ResponseEntity.internalServerError().build();
            }
            model.addText("Successfully saved form and generated id");
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<Response> update(@Valid @NonNull @RequestBody final SadhanaForm sadhanaForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(sadhanaForm.toString());
            sadhanaFormService.update(sadhanaForm);
            model.addText("Successfully updated form");
            return new ResponseEntity<>(Response.builder().message("Successfully updated form").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<SadhanaForm>> getSadhanaForm(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(sadhanaFormService.getSadhanaForm(pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<SadhanaForm> getSadhanaFormById(@NonNull @PathVariable Long id) {
        SadhanaForm sadhanaForm = sadhanaFormService.getSadhanaFormById(id);
        if (null == sadhanaForm)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(sadhanaForm);
    }
}
