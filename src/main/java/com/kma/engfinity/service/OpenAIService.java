package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.OpenAiRequest;
import com.kma.engfinity.DTO.response.OpenAiResponse;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {
    @Value("${GEMINI_API.URL}")
    private String API_URL;

    @Value("${GEMINI_API.KEY}")
    private String API_KEY;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    public String getOpenAiResponse(String prompt) {
        OpenAiRequest request = generateOpenAiRequest(prompt);
        OpenAiResponse response = restTemplate.postForObject(API_URL + API_KEY, request, OpenAiResponse.class);
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            OpenAiResponse.Candidate candidate = response.getCandidates().getFirst();
            return candidate.getContent().getParts().getFirst().getText();
        } else {
            throw new CustomException(EError.OPEN_API_ERROR);
        }
    }


    private OpenAiRequest generateOpenAiRequest(String prompt) {
        OpenAiRequest request = new OpenAiRequest();
        OpenAiRequest.Part part = new OpenAiRequest.Part();
        part.setText(prompt);
        OpenAiRequest.Content content = new OpenAiRequest.Content();
        content.getParts().add(part);
        request.getContents().add(content);
        return request;
    }

}
