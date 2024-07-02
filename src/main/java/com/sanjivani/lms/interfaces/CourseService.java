package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    Page<Course> getCourses(Pageable pageable);
    void save(Course course) throws IllegalArgumentException;
    Course getCourseByCode(String code);
}
