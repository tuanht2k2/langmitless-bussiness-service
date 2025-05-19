package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionScoreResponseV2 {
    private String id;
    private String topicId;
    private QuestionType type;
    private String content;
    private String audioSample;
    private String textSample;
    private String answer;
    private String answerAudio;
    private List<QuestionOptionResponse> option;
    private Float score;
}
