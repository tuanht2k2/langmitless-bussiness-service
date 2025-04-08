package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditTopicRequest;
import com.kma.engfinity.DTO.request.GetTopicsRequest;
import com.kma.engfinity.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping
    private ResponseEntity<?> create (@RequestBody EditTopicRequest request) {
        return topicService.create(request);
    }

//    @PostMapping("search")
//    public ResponseEntity<?> search (@RequestBody SearchTopicRequest request) {
//        return topicService.search(request);
//    }

    @PostMapping("get-all")
    public ResponseEntity<?> getAllByCourseId (@RequestBody GetTopicsRequest request) {
        return topicService.getAllTopics(request);
    }
}
