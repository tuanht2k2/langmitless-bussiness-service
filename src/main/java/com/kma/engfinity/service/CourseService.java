package com.kma.engfinity.service;

import com.kma.common.constant.Constant;
import com.kma.common.dto.request.AiResponse;
import com.kma.common.dto.request.AiSearchCourseRequest;
import com.kma.common.dto.response.ChatbotResponse;
import com.kma.common.dto.response.CommonCourseResponse;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.BuyCourseRequest;
import com.kma.engfinity.DTO.request.EditCourseRequest;
import com.kma.engfinity.DTO.request.SearchCourseRequest;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.entity.Message;
import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private MessageService messageService;

    public ResponseEntity<?> create (EditCourseRequest request) {
        Account account = authService.getCurrentAccount();
        Course course = modelMapper.map(request, Course.class);
        course.setCreatedBy(account.getId());
        courseRepository.save(course);
        CommonResponse<?> commonResponse = new CommonResponse<>(200, null, "Create course successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public ResponseEntity<?> update (EditCourseRequest request) {
        Course course = courseRepository.findById(request.getId()).orElse(null);
        if (course == null) throw new CustomException(EError.BAD_REQUEST);
        if (request.getName() != null) course.setName(request.getName());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getLanguage() != null) course.setLanguage(request.getLanguage());
        if (request.getCost() != null) course.setCost(request.getCost());
        courseRepository.save(course);
        CommonResponse<?> commonResponse = new CommonResponse<>(200, null, "Update course successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public ResponseEntity<?> getCourseDetails (String id) {
        List<Object[]> data = courseRepository.getCourseDetails(id);
        CourseResponse course = null;
        List<TopicResponse> topics = new ArrayList<>();
        for (Object[] row : data) {
            if (course == null) {
                course = new CourseResponse();
                course.setId((String) row[0]);
                course.setLanguage((String) row[1]);
                course.setName((String) row[2]);
                course.setDescription((String) row[3]);
                course.setCost((Long) row[4]);
                course.setCreatedAt((Timestamp) row[5]);

                PublicAccountResponse createdBy = new PublicAccountResponse();
                createdBy.setId((String) row[6]);
                createdBy.setName((String) row[7]);
                createdBy.setStatus(EAccountStatus.valueOf(String.valueOf(row[8])));
                createdBy.setProfileImage((String) row[9]);
                course.setCreatedBy(createdBy);
            }

            TopicResponse topic = new TopicResponse();
            topic.setId((String) row[10]);
            topic.setDescription((String) row[11]);
            TagResponse tag = new TagResponse();
            tag.setId((String) row[12]);
            tag.setName((String) row[13]);
            topic.setTag(tag);
            topics.add(topic);
        }
        if (course == null) throw new CustomException(EError.BAD_REQUEST);
        course.setTopics(topics);
        CommonResponse<?> commonResponse = new CommonResponse<>(200, course, "Get course detail successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public Course getCourse (String id) {
        return courseRepository.findById(id).orElse(null);
    }

    public ResponseEntity<?> buy (BuyCourseRequest request) {
//        Account account = accountService.getAccountById(request.getAccountId());
//        Course course = courseRepository.findById(request.getCourseId()).orElse(null);
//        if (course == null) throw new CustomException(EError.BAD_REQUEST);
//        EditAccountBalanceRequest editAccountBalanceRequest = EditAccountBalanceRequest.builder().id(request.getAccountId()).amount(course.getCost()).type(ETransferType.DEPOSIT).build();
//        accountService.updateBalance(editAccountBalanceRequest);
//        course.getMembers().add(account);
//        courseRepository.save(course);
        CommonResponse<?> commonResponse = new CommonResponse<>(200, null, "Buy course successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public CourseResponse courseToCourseResponse (Course course) {
        CourseResponse courseResponse = modelMapper.map(course, CourseResponse.class);
        Account account = accountService.getAccountById(course.getCreatedBy());
        if (account != null) {
            courseResponse.setCreatedBy(modelMapper.map(account, PublicAccountResponse.class));
        }
        return courseResponse;
    }

    private CommonCourseResponse courseToCommonCourseResponse (Course course) {
        return modelMapper.map(course, CommonCourseResponse.class);
    }

    public ResponseEntity<?> search (SearchCourseRequest request) {
        List<Object[]> data = courseRepository.search(request.getPage(), request.getPageSize(), request.getKeyword(), request.getCreatedBy());
        List<CourseResponse> courses = data.stream().map(row -> {
            CourseResponse course = new CourseResponse();
            course.setId(String.valueOf(row[0]));
            course.setLanguage(String.valueOf(row[1]));
            course.setName(String.valueOf(row[2]));
            course.setDescription(String.valueOf(row[3]));
            course.setCost(Long.valueOf(String.valueOf(row[4])));
            PublicAccountResponse createdBy = new PublicAccountResponse();
            createdBy.setId(String.valueOf(row[5]));
            createdBy.setName(String.valueOf(row[6]));
            course.setCreatedBy(createdBy);
            return course;
        }).toList();

        CommonResponse<?> commonResponse = new CommonResponse<>(200, courses, "Search courses successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public Response<Object> aiSearch (AiSearchCourseRequest request) {
        try {
            ChatbotResponse response = new ChatbotResponse();
            response.setMessage(request.getDialogResponse());
            if (ObjectUtils.isEmpty(request.getDialogResponse())) {
                Integer maxCost = null;
                Integer minCost = null;
                switch (request.getCost()) {
                    case Constant.CoursePrice.CHEAP:
                        maxCost = Constant.CoursePriceValue.CHEAP;
                        break;
                    case Constant.CoursePrice.MEDIUM:
                        maxCost = Constant.CoursePriceValue.MEDIUM;
                        minCost = Constant.CoursePriceValue.CHEAP;
                        break;
                    case Constant.CoursePrice.EXPENSIVE:
                        minCost = Constant.CoursePriceValue.MEDIUM;
                        break;
                    default:
                        maxCost = Constant.CoursePriceValue.FREE;
                }
                List<Course> courses = courseRepository.aiSearchCourse(request.getLanguage(), request.getLevel(), minCost, maxCost);
                List<CommonCourseResponse> courseResponses = courses.stream().map(this::courseToCommonCourseResponse).toList();
                response.setCourses(courseResponses);
            }
            messageService.sendChatbotMessage(response);
            return Response.getResponse(200, response,"Search course successful");
        } catch (Exception e) {
            log.error("An error happened when search course: {}",e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }
}
