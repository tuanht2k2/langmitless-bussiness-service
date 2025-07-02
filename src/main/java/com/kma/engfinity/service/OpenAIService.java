package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.common.dto.response.ChatbotResponse;
import com.kma.common.dto.response.CommonCourseResponse;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.AskChatbotRequest;
import com.kma.engfinity.DTO.request.OpenAiRequest;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.constants.Constant;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.entity.Topic;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.CourseRepository;
import com.kma.engfinity.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OpenAIService {
    @Value("${GEMINI_API.URL}")
    private String API_URL;

    @Value("${GEMINI_API.KEY}")
    private String API_KEY;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    TopicService topicService;

    @Autowired
    MessageService messageService;

    @Autowired
    AccountRepository accountRepository;

    public Response<Object> askAboutCourse(AskChatbotRequest request) {
        try {
            Course course = courseRepository.findById(request.getCourseId()).orElse(null);
            if (ObjectUtils.isEmpty(course)) {
                throw new CustomException(EError.BAD_REQUEST);
            }
            String createdBy = course.getCreatedBy();
            Account account = accountRepository.findById(createdBy).orElse(null);
            String createdByName = ObjectUtils.isEmpty(account) ? Constant.MockConstant.USER_NAME : account.getName();

            course.setCreatedBy(null);

            AiCourseDTO courseDto = objectMapper.convertValue(course, AiCourseDTO.class);
            courseDto.setCreatedByName(createdByName);
            List<Topic> topics = topicRepository.findByCourse(course.getId());
            courseDto.setTopics(topics.stream().map(this::topicToTopicResponse).toList());

            String courseString = objectMapper.writeValueAsString(courseDto);
            StringBuilder prompt = new StringBuilder().append("Người dùng hỏi: ")
                    .append(request.getMessage())
                    .append(", Dữ liệu của ứng dụng: ")
                    .append(courseString)
                    .append(",(Đơn vị tiền tệ là VNĐ), Dựa vào câu hỏi và các trường dữ liệu mà ứng dụng cung cấp, hãy giúp người dùng trả lời, với cương vị bạn là người quản trị hệ thống");

            OpenAiRequest openAiRequest = generateOpenAiRequest(prompt.toString());
            OpenAiResponse response = restTemplate.postForObject(API_URL + API_KEY, openAiRequest, OpenAiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                OpenAiResponse.Candidate candidate = response.getCandidates().get(0);
                ChatbotResponse chatbotResponse = new ChatbotResponse(candidate.getContent().getParts().get(0).getText(), Arrays.asList(objectMapper.convertValue(courseDto, CommonCourseResponse.class)));
                messageService.sendChatbotMessage(chatbotResponse);
                return Response.getResponse(200, "Successfully!");
            }
            throw new CustomException(EError.OPEN_API_ERROR);
        } catch (Exception e) {
            log.error("An error occurred while askAboutCourse", e);
            return Response.getResponse(500, e.getMessage());
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

    private TopicResponse topicToTopicResponse (Topic topic) {
        return topicService.topicToTopicResponse(topic);
    }

}
