package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.AnswerQuestionRequest;
import com.kma.engfinity.entity.Question;
import com.kma.engfinity.entity.QuestionOption;
import com.kma.engfinity.entity.UserScore;
import com.kma.engfinity.repository.UserScoreRepository;
import com.kma.engfinity.service.interfaces.AsyncUserScoreServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncUserScoreService implements AsyncUserScoreServiceInterface {
    private final UserScoreRepository userScoreRepository;
    private final FileService fileService;

    @Override
    @Async
    public void answerMultipleChoice(AnswerQuestionRequest request, List<QuestionOption> options, String accountId) {
        try {
            int score = calculateMultipleChoiceScore(request.getAnsweredText(), options);

            UserScore userScore = new UserScore();
            userScore.setTopicId(request.getTopicId());
            userScore.setQuestionId(request.getQuestionId());
            userScore.setScore((float) score);
            userScore.setAnsweredText(request.getAnsweredText());
            userScore.setCreatedAt(Instant.now());
            userScore.setUserId(accountId);
            userScore.setTransactionId(request.getTransactionId());
            userScoreRepository.save(userScore);
        } catch (Exception e) {
            log.error("An error occurred when UserScoreService.answerMultipleChoice", e);
        }
    }

    @Override
    @Async
    public void answerPronunciation(AnswerQuestionRequest request, Question question, String accountId, String audioPath) {
        try {
            String studentTranscript = AssemblyAISpeechToText.transcribeAudio(audioPath);
            Float score = calculatePronunciationScore(question.getTextSample(), studentTranscript);

            UserScore userScore = new UserScore();
            userScore.setTopicId(request.getTopicId());
            userScore.setQuestionId(request.getQuestionId());
            userScore.setScore(score);
            userScore.setAnsweredText(studentTranscript);
            userScore.setCreatedAt(Instant.now());
            userScore.setUserId(accountId);
            userScore.setTransactionId(request.getTransactionId());
            userScore.setAudioUrl(audioPath);

        } catch (Exception e) {
            log.error("An error occurred when UserScoreService.answerPronunciation", e);
        }
    }

    private Float calculatePronunciationScore (String correctAnswer, String studentTranscript) {
        try {
            LevenshteinDistance ld = new LevenshteinDistance();
            int distance = ld.apply(correctAnswer.toUpperCase(), studentTranscript.toUpperCase());
            return (1 - ((float) distance / Math.max(correctAnswer.length(), studentTranscript.length()))) * 100;
        } catch (Exception e) {
            log.error("An error occurred when UserScoreService.calculatePronunciationScore", e);
            return 0.0f;
        }
    }

    private int calculateMultipleChoiceScore(String answeredText, List<QuestionOption> options) {
        for (QuestionOption option : options) {
            if (option.isCorrect() && option.getContent().equalsIgnoreCase(answeredText)) {
                return 10;
            }
        }
        return 0;
    }
}
