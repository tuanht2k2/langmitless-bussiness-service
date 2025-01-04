package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditTopicRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.TagResponse;
import com.kma.engfinity.DTO.response.TopicResponse;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.entity.Tag;
import com.kma.engfinity.entity.Topic;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.TopicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    @Lazy
    private CourseService courseService;

    public ResponseEntity<?> create (EditTopicRequest request) {
        Tag tag = tagService.get(request.getTagId());
        if (tag == null) throw new CustomException(EError.TAG_NOT_EXISTED);
        Course course = courseService.getCourse(request.getCourseId());
        if (course == null) throw new CustomException(EError.COURSE_NOT_EXISTED);

        Account account = authService.getCurrentAccount();
        Topic topic = modelMapper.map(request, Topic.class);
        topic.setCreatedBy(account.getId());
        topic.setTag(request.getTagId());
        topic.setCourse(request.getCourseId());
        topicRepository.save(topic);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Create topic successfully!");
        return ResponseEntity.ok(response);
    }

    public List<TopicResponse> getAllByCourseId(String courseId) {
        List<Object[]> data = topicRepository.getAllByCourseId(courseId);
        return data.stream().map(row -> {
            TopicResponse topic = new TopicResponse();
            TagResponse tag = new TagResponse();
            tag.setId(String.valueOf(row[2]));
            tag.setName(String.valueOf(row[3]));
            topic.setId(String.valueOf(row[0]));
            topic.setDescription(String.valueOf(row[1]));
            topic.setTag(tag);
            return topic;
        }).toList();
    }

    public TopicResponse topicToTopicResponse(Topic topic) {
        return modelMapper.map(topic, TopicResponse.class);
    }
}
