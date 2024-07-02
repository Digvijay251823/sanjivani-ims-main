package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.ActivityService;
import com.sanjivani.lms.model.Activity;
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
@RequestMapping("/activity")
public class ActivityController {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    ActivityService activityService;

    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Activity activity, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(activity.toString());
            activityService.save(activity);
            model.addText("Successfully created Activity");
            return new ResponseEntity<>(Response.builder().message("Successfully created Activity").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Activity>> getActivities(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(activityService.getActivities(pageable));
    }

    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public ResponseEntity<Activity> getActivityByName(@NonNull @PathVariable String name) {
        Activity activity = activityService.getActivityByName(name);
        if (null == activity)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(activity);
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Activity> getActivityByName(@NonNull @PathVariable Long id) {
        Activity activity = activityService.getActivityById(id);
        if (null == activity)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(activity);
    }

}
