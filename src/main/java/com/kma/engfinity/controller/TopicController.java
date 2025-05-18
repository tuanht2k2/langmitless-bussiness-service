package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditTopicRequest;
import com.kma.engfinity.DTO.request.GetTopicsRequest;
import com.kma.engfinity.DTO.request.SearchTransactionRequest;
import com.kma.engfinity.DTO.request.TransactionScoreRequest;
import com.kma.engfinity.service.TopicService;
import com.kma.engfinity.service.UserScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final UserScoreService userScoreService;

    @PostMapping
    private ResponseEntity<?> create (@RequestBody EditTopicRequest request) {
        return topicService.create(request);
    }

    @PostMapping("get-all")
    public ResponseEntity<?> getAllByCourseId (@RequestBody GetTopicsRequest request) {
        return topicService.getAllTopics(request);
    }

    @PostMapping("/get-score-by-transaction")
    public Response<Object> getScoreByTransaction(@RequestBody TransactionScoreRequest request) {
        return userScoreService.getTopicScoreHistory(request);
    }

    @PostMapping("/search-transaction-by-topic")
    public Response<Object> searchTransaction(@RequestBody SearchTransactionRequest request) {
        return userScoreService.searchTransactionsByTopic(request);
    }
}
