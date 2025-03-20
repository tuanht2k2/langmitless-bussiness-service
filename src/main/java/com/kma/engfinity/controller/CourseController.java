package com.kma.engfinity.controller;

import com.kma.common.dto.request.AiSearchCourseRequest;
import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditCourseRequest;
import com.kma.engfinity.DTO.request.SearchCourseRequest;
import com.kma.engfinity.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditCourseRequest request) {
        return courseService.create(request);
    }

    @PutMapping
    public ResponseEntity<?> update (@RequestBody EditCourseRequest request) {
        return courseService.update(request);
    }

    @PostMapping("search")
    public ResponseEntity<?> search (@RequestBody SearchCourseRequest request) {
        return courseService.search(request);
    }

    @PostMapping("ai-search")
    public Response<Object> aiSearch (@RequestBody AiSearchCourseRequest request) {
        return courseService.aiSearch(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get (@PathVariable String id) {
        return courseService.getCourseDetails(id);
    }
}
