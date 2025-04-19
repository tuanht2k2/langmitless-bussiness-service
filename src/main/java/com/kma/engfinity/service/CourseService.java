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
import com.kma.engfinity.DTO.request.StudentSearchCourseRequest;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.entity.AccountsCourses;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.ERole;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountCourseRepository;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.CourseRepository;
import com.kma.engfinity.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private TopicRepository topicRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountCourseRepository accountCourseRepository;

    @Autowired
    private PaymentService paymentService;

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
        Account currentAccount = authService.getCurrentAccount();

        List<Object[]> data = courseRepository.getCourseDetails(id);
        CourseResponse course = null;
        List<TopicResponse> topics = new ArrayList<>();
        List<Account> accounts = accountRepository.findMembersByCourseId(id);
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

            List<PublicAccountResponse> members = accounts.stream().map(acc -> {
                PublicAccountResponse res = new PublicAccountResponse();
                res.setId(acc.getId());
                res.setName(acc.getName());
                res.setProfileImage(acc.getProfileImage());
                return res;
            }).toList();

        if (course == null) throw new CustomException(EError.BAD_REQUEST);
        course.setTopics(topics);
        course.setMembers(members);

        course.setMember(accountCourseRepository.existsByAccountIdAndCourseId(currentAccount.getId(), id) || currentAccount.getId().equals(id));
        CommonResponse<?> commonResponse = new CommonResponse<>(200, course, "Get course detail successfully!");
        return ResponseEntity.ok(commonResponse);
    }

    public Course getCourse (String id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public Response<Object> buy (BuyCourseRequest request) {
        try {
            Account currentAccount = authService.getCurrentAccount();

            Course course = courseRepository.findById(request.getCourseId()).orElse(null);
            if (ObjectUtils.isEmpty(course)) throw new CustomException(EError.BAD_REQUEST);
            if (currentAccount.getBalance() < course.getCost()) throw new CustomException(EError.NOT_ENOUGH_MONEY);
            if (accountCourseRepository.existsByAccountIdAndCourseId(currentAccount.getId(), request.getCourseId())) throw new CustomException(EError.BAD_REQUEST);
            if (currentAccount.getId().equals(course.getCreatedBy())) throw new CustomException(EError.BAD_REQUEST);

            paymentService.transfer(course.getCreatedBy(), course.getCost(), "Người dùng: " + currentAccount.getFullName() + "mua khóa học" + course.getName());
            AccountsCourses accountsCourses = new AccountsCourses();
            accountsCourses.setCourseId(course.getId());
            accountsCourses.setAccountId(currentAccount.getId());
            accountCourseRepository.save(accountsCourses);

            return Response.getResponse(200, "Buy course successfully!");
        } catch (Exception e) {
            log.error("An error happened when buy course: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }

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

            List<TopicResponse> topics = topicRepository.findByCourse(course.getId())
                .stream()
                .map(topic -> {
                    TopicResponse res = new TopicResponse();
                    res.setId(topic.getId());
                    res.setDescription(topic.getDescription());
                    TagResponse tagRes =new TagResponse();
                    tagRes.setId(tagRes.getId());
                    tagRes.setName(tagRes.getName());
                    tagRes.setLanguage(tagRes.getLanguage());
                    return res;
                })
                .toList();
            course.setTopics(topics);

            List<PublicAccountResponse> members = accountRepository.findMembersByCourseId(course.getId())
                .stream()
                .map(user -> {
                    PublicAccountResponse member = new PublicAccountResponse();
                    member.setId(user.getId());
                    member.setProfileImage(user.getProfileImage());
                    member.setName(user.getName());
                    member.setRole(ERole.USER);
                    return member;
                })
                .toList();
            course.setMembers(members);

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
                List<Course> courses = courseRepository.aiSearchCourse(request.getLanguage(), request.getLevel(), minCost, maxCost, null);
                List<CommonCourseResponse> courseResponses = courses.stream().map(this::courseToCommonCourseResponse).toList();
                response.setCourses(courseResponses);
            }
            messageService.sendChatbotMessage(response);
            return Response.getResponse(200, response,"Search course successful");
        } catch (Exception e) {
            log.error("An error happened when aiSearch course: {}",e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }

    public Response<Object> studentSearch (StudentSearchCourseRequest request) {
        try {
            Integer maxCost = null;
            Integer minCost = null;
            switch (request.getPrice()) {
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

            List<Course> courses = courseRepository.aiSearchCourse(request.getLanguage(), request.getLevel(), minCost, maxCost, request.getName());
            List<CommonCourseResponse> courseResponses = courses.stream().map(this::courseToCommonCourseResponse).toList();

            return Response.getResponse(200, courseResponses,"Search course successful");
        } catch (Exception e) {
            log.error("An error happened when studentSearch course: {}",e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }
}
