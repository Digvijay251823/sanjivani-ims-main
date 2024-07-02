package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
    Page<Activity> getActivities(Pageable pageable);
    void save(Activity activity) throws IllegalArgumentException;
    Activity getActivityById(Long id);
    Activity getActivityByName(String name);
}
