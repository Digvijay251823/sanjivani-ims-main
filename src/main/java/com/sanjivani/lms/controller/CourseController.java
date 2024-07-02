package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.model.Course;
import com.sanjivani.lms.interfaces.CourseService;
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
@RequestMapping("/course")
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    @Autowired
    CourseService courseService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Course course, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(course.toString());
            courseService.save(course);
            model.addText("Successfully created Course");
            return new ResponseEntity<>(Response.builder().message("Successfully created Course").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Course>> getSessions(@PageableDefault(size = 10, sort = "code", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(courseService.getCourses(pageable));
    }

    @GetMapping(value = "/{code}")
    @ResponseBody
    public ResponseEntity<Course> getCourseByCode(@NonNull @PathVariable String code) {
        Course course = courseService.getCourseByCode(code);
        if (null == course)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(course);
    }

}
