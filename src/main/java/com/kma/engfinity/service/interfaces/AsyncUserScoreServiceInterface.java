package com.kma.engfinity.service.interfaces;

import com.kma.engfinity.DTO.request.AnswerQuestionRequest;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.QuestionOption;

import java.util.List;

public interface AsyncUserScoreServiceInterface {
    void answerMultipleChoice(AnswerQuestionRequest request, List<QuestionOption> options, String accountId);
    void answerPronunciation(AnswerQuestionRequest request, Question question, String accountId, String audioPath);
}
