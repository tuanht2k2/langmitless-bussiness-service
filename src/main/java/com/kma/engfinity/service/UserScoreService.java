package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.AnswerQuestionRequest;
import com.kma.engfinity.DTO.request.QuestionScoreRequest;
import com.kma.engfinity.DTO.request.UserScoreByTopicRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.QuestionScoreResponse;
import com.kma.engfinity.DTO.response.TopicScoreResponse;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.QuestionOption;
import com.kma.engfinity.entity.UserScore;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.QuestionType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.QuestionRepository;
import com.kma.engfinity.repository.UserScoreRepository;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserScoreService {

  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private UserScoreRepository userScoreRepository;

  @Autowired
  private FileService fileService;

  public ResponseEntity<?>  answerQuestion(AnswerQuestionRequest request) {
    Question question = questionRepository.findById(request.getQuestionId())
        .orElseThrow(() -> new CustomException(
            EError.NOT_FOUND_QUESTION));
    UserScore userScore;
    if (question.getQuestionType() == QuestionType.MultipleChoice) {
      userScore = answerMultipleChoice(request, question);
    } else {
      userScore = answerPronunciation(request, question);
    }
    userScoreRepository.save(userScore);
    CommonResponse<?> commonResponse = new CommonResponse<>(200, userScore,
        "Answered and scored successfully!");
    return ResponseEntity.ok(commonResponse);
  }

  private UserScore answerMultipleChoice(AnswerQuestionRequest request, Question question) {
    int score = calculateMultipleChoiceScore(request.getAnsweredText(), question.getOptions());

    UserScore userScore = new UserScore();
    userScore.setTopicId(request.getTopicId());
    userScore.setQuestionId(request.getQuestionId());
    userScore.setScore((float) score);
    userScore.setAnsweredText(request.getAnsweredText());
    userScore.setPronunciationScore(0.0f);
    userScore.setCreatedAt(Instant.now());

    return userScore;
  }

  public UserScore answerPronunciation(AnswerQuestionRequest request, Question question) {

    String audioPath = fileService.getFileUrl(request.getAnswerFile());
    String correctTranscript = AssemblyAISpeechToText.transcribeAudio(question.getAudioSample());
    String studentTranscript = AssemblyAISpeechToText.transcribeAudio(audioPath);
    PronunciationResult result = calculatePronunciationScore(correctTranscript, studentTranscript);

    UserScore userScore = new UserScore();
    userScore.setTopicId(request.getTopicId());
    userScore.setQuestionId(request.getQuestionId());
    userScore.setScore((float) result.score());
    userScore.setAnsweredText(studentTranscript);
    userScore.setPronunciationScore((float) result.percentage());
    userScore.setCreatedAt(Instant.now());
    return userScore;
  }


  private int calculateMultipleChoiceScore(String answeredText, List<QuestionOption> options) {
    for (QuestionOption option : options) {
      if (option.isCorrect() && option.getContent().equalsIgnoreCase(answeredText)) {
        return 10;
      }
    }
    return 0;
  }

  private PronunciationResult calculatePronunciationScore(String correctAnswer, String studentTranscript) {
    List<String> correctWords = Arrays.asList(correctAnswer.trim().toLowerCase().split("\\s+"));
    List<String> spokenWords = Arrays.asList(studentTranscript.trim().toLowerCase().split("\\s+"));

    if (correctWords.isEmpty()) {
      return new PronunciationResult(0, 0.0);
    }

    int match = 0;
    for (int i = 0; i < Math.min(correctWords.size(), spokenWords.size()); i++) {
      if (correctWords.get(i).equals(spokenWords.get(i))) {
        match++;
      }
    }

    double percentage = (double) match / correctWords.size();
    int score = (int) Math.round(percentage * 10);
    double rawPercentage = Math.round(percentage * 10000.0) / 100.0;

    return new PronunciationResult(score, rawPercentage);
  }


  public record PronunciationResult(int score, double percentage) {

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


}
