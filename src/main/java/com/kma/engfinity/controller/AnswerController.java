package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.AnswerQuestionRequest;
import com.kma.engfinity.DTO.request.QuestionScoreRequest;
import com.kma.engfinity.DTO.request.UserScoreByTopicRequest;
import com.kma.engfinity.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/answer")
public class AnswerController {

  @Autowired
  private UserScoreService userScoreService;

  @PostMapping("/MultipleChoice")
  public ResponseEntity<?> answerQuestionMultipleChoice(
      @RequestBody AnswerQuestionRequest request) {
    return userScoreService.answerQuestion(request);
  }

  @PostMapping("/Pronunciation")
  public ResponseEntity<?> answerQuestionPronunciation(
      @RequestParam("topicId") String topicId,
      @RequestParam("questionId") String questionId,
      @RequestParam(value = "answerFile", required = false) MultipartFile answerFile,
      @RequestParam("transactionId") String transactionId
  ) {
    AnswerQuestionRequest request = new AnswerQuestionRequest();
    request.setTopicId(topicId);
    request.setQuestionId(questionId);
    request.setAnswerFile(answerFile);
    request.setTransactionId(transactionId);

    return userScoreService.answerQuestion(request);
  }

  @PostMapping("/topic-score")
  public ResponseEntity<?> getAverageScoreByTopic(@RequestBody UserScoreByTopicRequest request) {
    return userScoreService.getAverageScoreByTopic(request);
  }

  @PostMapping("/get-score-by-question")
  public ResponseEntity<?> getScoreByQuestion(@RequestBody QuestionScoreRequest request) {
    return userScoreService.getScoreByQuestion(request);
  }
}
