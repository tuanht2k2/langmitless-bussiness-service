package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.AssignQuestionToTopicRequest;
import com.kma.engfinity.DTO.request.CourseQuestionRequest;
import com.kma.engfinity.DTO.request.QuestionRequest;
import com.kma.engfinity.DTO.request.TopicRequest;
import com.kma.engfinity.service.QuestionService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/question")
public class QuestionController {

  @Autowired
  private QuestionService questionService;

  @PostMapping("/multiple-choice")
  public ResponseEntity<?> creatMultipleChoice(@RequestBody QuestionRequest request) {
    return questionService.createMultipleChoice(request);
  }

  @PostMapping(value = "/pronunciation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createPronunciation(
      @RequestParam("topicId") UUID topicId,
      @RequestParam("content") String content,
      @RequestParam("audioSample") MultipartFile audioSample) {
    return questionService.createPronunciation(topicId, content, audioSample);
  }

  @PostMapping("/by-course")
  public ResponseEntity<?> getQuestionsByCourse(@RequestBody CourseQuestionRequest request) {
    return questionService.getQuestionsByTopic(request);
  }

  @DeleteMapping("/{questionId}")
  public ResponseEntity<?> deleteQuestionById(@PathVariable UUID questionId) {
    return questionService.deleteQuestionById(questionId);
  }

  @PostMapping("/multiple-choice/update/{questionId}")
  public ResponseEntity<?> updateMultipleChoice(
      @PathVariable UUID questionId, @RequestBody QuestionRequest request) {
    return questionService.updateMultipleChoice(questionId, request);
  }

  @PostMapping(value = "/pronunciation/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updatePronunciation(
      @RequestParam("questionId") UUID questionId,
      @RequestParam("content") String content,
      @RequestParam("audioSample") MultipartFile audioSample) {
    return questionService.updatePronunciation(questionId, content, audioSample);
  }

//  @PostMapping("/assign-to-topic")
//  public ResponseEntity<?> assignToTopic(@RequestBody AssignQuestionToTopicRequest request) {
//    return questionService.assignQuestionToTopic(request);
//  }
  @PostMapping("/by-topic")
  public ResponseEntity<?> getQuestionsByTopic(@RequestBody TopicRequest request) {

    return questionService.getQuestionsByTopicId(request);
  }
}
