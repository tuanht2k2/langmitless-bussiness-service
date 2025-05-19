package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.*;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.constants.Constant.*;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.UserScore;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.QuestionType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.QuestionRepository;
import com.kma.engfinity.repository.UserScoreRepository;

import java.util.*;

import com.kma.engfinity.service.interfaces.AsyncUserScoreServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserScoreService {
  private final QuestionRepository questionRepository;
  private final UserScoreRepository userScoreRepository;
  private final AuthService authService;
  private final AsyncUserScoreServiceInterface asyncUserScoreService;
  private final FileService fileService;

  public ResponseEntity<?> answerQuestion(AnswerQuestionRequest request) {
    try {
      Account account = authService.getCurrentAccount();

      Question question = questionRepository.findById(request.getQuestionId())
              .orElseThrow(() -> new CustomException(
                      EError.NOT_FOUND_QUESTION));

      if (question.getQuestionType() == QuestionType.MultipleChoice) {
        Hibernate.initialize(question.getOptions());
        asyncUserScoreService.answerMultipleChoice(request, question.getOptions(), account.getId());
      } else {
        String audioPath = fileService.getFileUrl(request.getAnswerFile());
        asyncUserScoreService.answerPronunciation(request, question, account.getId(), audioPath);
      }

      return ResponseEntity.ok(Response.getResponse(ErrorCode.OK, ErrorMessage.SUCCESS));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  public ResponseEntity<?> getAverageScoreByTopic(UserScoreByTopicRequest request) {
    List<TopicScoreResponse> scores = userScoreRepository.
        getUserScoreGroupedByTopic(String.valueOf(request.getUserId()));
    CommonResponse<?> commonResponse = new CommonResponse<>(200, scores, "Average scores by topic fetched successfully!");
    return ResponseEntity.ok(commonResponse);
  }

  public ResponseEntity<?> getScoreByQuestion(QuestionScoreRequest request) {
    UserScore userScore = userScoreRepository.findTopByTopicIdAndQuestionIdOrderByCreatedAtDesc(
        request.getTopicId(),
        request.getQuestionId()
    ).orElseThrow(() -> new CustomException(EError.NOT_FOUND_QUESTION));

    QuestionScoreResponse response = new QuestionScoreResponse(
        userScore.getScore(),
        userScore.getPronunciationScore(),
        userScore.getAnsweredText()
    );

    CommonResponse<?> commonResponse = new CommonResponse<>(200, response, "Latest score by question fetched successfully!");
    return ResponseEntity.ok(commonResponse);
  }

  public Response<Object> getTopicScoreHistory (TransactionScoreRequest request) {
    try {
      List<Object[]> data = userScoreRepository.getTopicScoreHistory(request.getTopicId(), request.getUserId(), request.getTransactionId());
      if (ObjectUtils.isEmpty(data)) {
        return Response.getResponse(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND);
      }

      TransactionScoreResponse transactionScore = new TransactionScoreResponse();
      transactionScore.setTopicId(request.getTopicId());
      transactionScore.setUserId(request.getUserId());
      transactionScore.setTransactionId(request.getTransactionId());

      Map<String, QuestionScoreResponseV2> questionsMap = new HashMap<>();

      for (Object[] row : data) {
        if (ObjectUtils.isEmpty(transactionScore.getScore())) {
          transactionScore.setScore(Float.parseFloat(String.valueOf(row[17])));
        }

        boolean isCollected = !ObjectUtils.isEmpty(questionsMap.get(String.valueOf(row[0])));
        QuestionScoreResponseV2 question = !isCollected ? new QuestionScoreResponseV2() : questionsMap.get(String.valueOf(row[0]));

        if (!isCollected) {
          question.setId(String.valueOf(row[0]));
          question.setTopicId(String.valueOf(row[1]));
          question.setType(QuestionType.valueOf(String.valueOf(row[2])));
          question.setContent(String.valueOf(row[3]));
          question.setAudioSample(String.valueOf(row[4]));
          question.setTextSample(String.valueOf(row[5]));
          question.setAnswerAudio(String.valueOf(row[10]));
          question.setAnswer(String.valueOf(row[11]));
          question.setScore(row[12] != null ? Float.parseFloat(String.valueOf(row[12])) : 0f);
        }

        if (question.getType().equals(QuestionType.MultipleChoice)) {
          boolean isOptionsCollected = !ObjectUtils.isEmpty(question.getOption());
          List<QuestionOptionResponse> options = isOptionsCollected ? question.getOption() : new ArrayList<>();

          QuestionOptionResponse option = new QuestionOptionResponse();
          option.setId(row[14] != null ? UUID.fromString(String.valueOf(row[14])) : null);
          option.setContent(String.valueOf(row[15]));

          option.setCorrect(Boolean.parseBoolean(String.valueOf(row[16])));
          options.add(option);
          question.setOption(options);
        }

        questionsMap.put(question.getId(), question);
      }


      transactionScore.setQuestions(questionsMap.values().stream().toList());

      return Response.getResponse(ErrorCode.OK, transactionScore, ErrorMessage.SUCCESS);
    } catch (Exception e) {
      log.error("An error occurred when UserScoreService.getTopicScoreHistory", e);
      return Response.getResponse(ErrorCode.SERVICE_ERROR, ErrorMessage.SERVICE_ERROR);
    }
  }

  public Response<Object> searchTransactionsByTopic (SearchTransactionRequest request) {
    try {
      List<String> transactions = userScoreRepository.searchTransactionsByTopic(request.getTopicId(), request.getUserId());
      return Response.getResponse(ErrorCode.OK, transactions, ErrorMessage.SUCCESS);
    } catch (Exception e) {
      log.error("An error occurred when UserScoreService.getTransactionsByTopic", e);
      return Response.getResponse(ErrorCode.SERVICE_ERROR, ErrorMessage.SERVICE_ERROR);
    }
  }
}
