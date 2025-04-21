package com.kma.engfinity.converter;

import com.kma.engfinity.DTO.request.QuestionRequest;
import com.kma.engfinity.DTO.response.QuestionOptionResponse;
import com.kma.engfinity.DTO.response.QuestionResponse;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.QuestionOption;
import com.kma.engfinity.enums.QuestionType;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

  public static Question toEntity(QuestionRequest request) {
    if (request == null) {
      return null;
    }
    Question question = new Question();
    question.setQuestionType(request.getType());
    question.setContent(request.getContent());

    if(request.getType() == QuestionType.Pronunciation){
      question.setAudioSample("placeholder_for_audio_path");
    }

    return question;
  }

  public static QuestionResponse toResponse(Question question) {
    if (question == null) {
      return null;
    }
    QuestionResponse response = new QuestionResponse();
    response.setId(UUID.fromString(question.getId()));
    if (question.getTopic() != null) {
      response.setCourseId(UUID.fromString(question.getTopic().getId()));
    }
    response.setType(question.getQuestionType());
    response.setContent(question.getContent());
    response.setAudioSample(question.getAudioSample());
    if (question.getQuestionType() == QuestionType.MultipleChoice) {
      List<QuestionOptionResponse> optionResponses = question.getOptions().stream()
          .map(option -> {
            QuestionOptionResponse optionResponse = new QuestionOptionResponse();
            optionResponse.setId(UUID.fromString(option.getId()));
            optionResponse.setContent(option.getContent());
            optionResponse.setCorrect(option.isCorrect());
            return optionResponse;
          })
          .collect(Collectors.toList());
      response.setOption(optionResponses);
    }

    return response;
  }

  public void updateEntityRequest(QuestionRequest request, Question question) {
    if (request == null || question == null) {
      return;
    }
    question.setContent(request.getContent());
    if (request.getType() == QuestionType.Pronunciation) {
      if (request.getAudioSample() != null) {
        question.setAudioSample("placeholder_for_audio_path");
      }
    }
    if (question.getQuestionType() == QuestionType.MultipleChoice) {
      question.getOptions().clear();
      if (request.getOptions() != null) {
        List<QuestionOption> newOptions = request.getOptions().stream()
            .map(opt -> {
              QuestionOption option = new QuestionOption();
              option.setContent(opt.getContent());
              option.setCorrect(opt.isCorrect());
              option.setQuestion(question);
              return option;
            }).toList();
        question.getOptions().addAll(newOptions);
      }
    }
  }
}
