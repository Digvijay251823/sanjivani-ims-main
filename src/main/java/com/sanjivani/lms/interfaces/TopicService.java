package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {
    Page<Topic> getTopics(Pageable pageable);
    void save(Topic topic) throws IllegalArgumentException;
    Topic getTopicById(Long id);
    Topic getTopicByName(String name);
}
