package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.AssignQuestionToTopicRequest;
import com.kma.engfinity.DTO.request.CourseQuestionRequest;
import com.kma.engfinity.DTO.request.QuestionRequest;
import com.kma.engfinity.DTO.request.TopicRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.QuestionResponse;
import com.kma.engfinity.converter.QuestionConverter;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.QuestionOption;
import com.kma.engfinity.entity.Topic;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.QuestionType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.CourseRepository;
import com.kma.engfinity.repository.QuestionOptionRepository;
import com.kma.engfinity.repository.QuestionRepository;
import com.kma.engfinity.repository.TopicRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private AuthService authService;

  @Autowired
  private QuestionOptionRepository questionOptionRepository;
  @Autowired
  private QuestionConverter questionConverter;

  @Autowired
  private FileService fileService;

  @Autowired
  private TopicRepository topicRepository;


  public ResponseEntity<?> createMultipleChoice(QuestionRequest request) {
    authService.verifyTeacherRole();

    Topic topic = topicRepository.findById(String.valueOf(request.getTopicId()))
        .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

    if (request.getType() != QuestionType.MultipleChoice || request.getOptions() == null) {
      throw new IllegalArgumentException("Dữ liệu không hợp lệ cho câu hỏi MultipleChoice");
    }

    Question question = QuestionConverter.toEntity(request);
    question.setTopic(topic);

    question = questionRepository.save(question);

    if (request.getType() == QuestionType.MultipleChoice && request.getOptions() != null) {
      Question finalQuestion = question;
      List<QuestionOption> options = request.getOptions().stream().map(optionRequest -> {
        QuestionOption option = new QuestionOption();
        option.setQuestion(finalQuestion);
        option.setContent(optionRequest.getContent());
        option.setCorrect(optionRequest.isCorrect());
        return option;
      }).collect(Collectors.toList());
      questionOptionRepository.saveAll(options);
      question.setOptions(options);
    }

    return ResponseEntity.ok(QuestionConverter.toResponse(question));
  }

  public ResponseEntity<?> createPronunciation(UUID topicId, String content,
      MultipartFile audioSample) {
    authService.verifyTeacherRole();

    Topic topic = topicRepository.findById(String.valueOf(topicId))
        .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

    Question question = new Question();
    question.setQuestionType(QuestionType.Pronunciation);
    question.setContent(content);
    question.setTopic(topic);

    String audioPath = fileService.getFileUrl(audioSample);
    question.setAudioSample(audioPath);

    question = questionRepository.save(question);
    return ResponseEntity.ok(QuestionConverter.toResponse(question));
  }

  public ResponseEntity<?> getQuestionsByTopic(CourseQuestionRequest request) {
    authService.verifyTeacherRole();
    if (request.getTopicId() == null) {
      throw new IllegalArgumentException("topic không được để trống");
    }

    Pageable pageable = (request.getLimit() != null) ? PageRequest.of(0, request.getLimit(),
        Sort.by(Sort.Direction.DESC, "createdAt")) :
        Pageable.unpaged();

    Page<Question> questions = questionRepository.searchQuestion(
        request.getTopicId().toString(),
        request.getQuestionType(),
        pageable
    );

    List<QuestionResponse> responses = questions.stream()
        .map(QuestionConverter::toResponse)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responses);
  }

  public ResponseEntity<?> deleteQuestionById(UUID questionId) {
    authService.verifyTeacherRole();
    Question question = questionRepository.findById(String.valueOf(questionId)).orElseThrow(() ->
        new CustomException(EError.BAD_REQUEST));
    questionRepository.delete(question);

    return ResponseEntity.ok(new CommonResponse<>(200, null, "Xóa câu hỏi thành công!"));
  }

  public ResponseEntity<?> updateMultipleChoice(UUID questionId, QuestionRequest request) {
    authService.verifyTeacherRole();
    Question question = questionRepository.findById(String.valueOf(questionId))
        .orElseThrow(() -> new CustomException(EError.NOT_FOUND_QUESTION));

    if (question.getQuestionType() != QuestionType.MultipleChoice) {
      throw new CustomException(EError.INVALID_QUESTION_TYPE);
    }
    questionConverter.updateEntityRequest(request, question);
    questionRepository.save(question);

    return ResponseEntity.ok(QuestionConverter.toResponse(question));
  }

  public ResponseEntity<?> updatePronunciation(UUID questionId, String content,
      MultipartFile audioSample) {
    authService.verifyTeacherRole();
    Question question = questionRepository.findById(String.valueOf(questionId))
        .orElseThrow(() -> new CustomException(EError.NOT_FOUND_QUESTION));

    if (question.getQuestionType() != QuestionType.Pronunciation) {
      throw new CustomException(EError.INVALID_QUESTION_TYPE);
    }
    question.setContent(content);
    if (audioSample != null && !audioSample.isEmpty()) {
      String audioPath = fileService.getFileUrl(audioSample);
      question.setAudioSample(audioPath);
    }
    questionRepository.save(question);
    return ResponseEntity.ok(QuestionConverter.toResponse(question));
  }

//  public ResponseEntity<?> assignQuestionToTopic(AssignQuestionToTopicRequest request) {
//    Topic topic = topicRepository.findById(request.getTopicId())
//        .orElseThrow(() -> new CustomException(EError.NOT_FOUND_TOPIC));
//    List<Question> questions = questionRepository.findAllById(request.getQuestionIds());
//    if (questions.size() != request.getQuestionIds().size()) {
//      throw new CustomException(EError.NOT_FOUND_QUESTION);
//    }
//
//    for (Question question : questions) {
//      question.setTopicId(topic.getId());
//    }
//    questionRepository.saveAll(questions);
//    CommonResponse<?> response = new CommonResponse<>(200, "",
//        "Add question to topic successfully");
//    return ResponseEntity.ok(response);
//  }

  public ResponseEntity<?> getQuestionsByTopicId(TopicRequest request) {
    authService.verifyTeacherRole();
    if (request.getTopicId() == null) {
      throw new IllegalArgumentException("topicId không được để trống");
    }
    List<Question> questions = questionRepository.findByTopicId(request.getTopicId());
    List<QuestionResponse> responses = questions.stream()
        .map(QuestionConverter::toResponse)
        .toList();
    CommonResponse<?> response = new CommonResponse<>(200, responses,
        "Add question to topic successfully");
    return ResponseEntity.ok(response);
  }
}


