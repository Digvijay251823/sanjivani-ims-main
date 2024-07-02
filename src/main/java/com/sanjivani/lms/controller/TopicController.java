package com.sanjivani.lms.controller;

import ch.qos.logback.core.model.Model;
import com.sanjivani.lms.interfaces.TopicService;
import com.sanjivani.lms.model.Response;
import com.sanjivani.lms.model.Topic;
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
@RequestMapping("/topic")
public class TopicController {

    private static final Logger LOG = LoggerFactory.getLogger(TopicController.class);
    @Autowired
    TopicService topicService;

    @PostMapping(value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> create(@Valid @NonNull @RequestBody final Topic topic, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            LOG.info(topic.toString());
            topicService.save(topic);
            model.addText("Successfully created Topic");
            return new ResponseEntity<>(Response.builder().message("Successfully created Topic").build(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<Topic>> getTopics(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(topicService.getTopics(pageable));
    }

    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public ResponseEntity<Topic> getTopicByName(@NonNull @PathVariable String name) {
        Topic topic = topicService.getTopicByName(name);
        if (null == topic)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(topic);
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<Topic> getTopicById(@NonNull @PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        if (null == topic)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(topic);
    }

}
