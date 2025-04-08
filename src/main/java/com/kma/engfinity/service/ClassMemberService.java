package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.ClassMemberListRequest;
import com.kma.engfinity.DTO.request.ClassMemberRequest;
import com.kma.engfinity.DTO.response.ClassMemberResponse;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.converter.ClassMemberConverter;
import com.kma.engfinity.entity.ClassMember;
import com.kma.engfinity.entity.Course;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.ClassMemberRepository;
import com.kma.engfinity.repository.CourseRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClassMemberService {
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ClassMemberRepository classMemberRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private AccountRepository accountRepository;

  public ResponseEntity<?> addMemberToClass(ClassMemberRequest request){
    Course course = courseRepository.findById(String.valueOf(request.getCourseId()))
        .orElseThrow(() -> new CustomException(EError.COURSE_NOT_EXISTED));
    Account account = accountRepository.findById(request.getAccountId())
        .orElseThrow(() -> new CustomException(EError.EXISTED_BY_USERNAME));


    boolean exists = classMemberRepository.existsByCourseIdAndAccountId(request.getCourseId(), request.getAccountId());

    if (exists) {
      throw new CustomException(EError.MEMBER_IN_THIS_COURSE);
    }


    ClassMember classMember = ClassMemberConverter.toEntity(course,account);
    classMemberRepository.save(classMember);
    CommonResponse<?> response = new CommonResponse<>(200,null,"Add member successfully");
    return ResponseEntity.ok(response);
  }
  public ResponseEntity<?> getListMemberClass(ClassMemberListRequest request){
    Course course = courseRepository.findById(String.valueOf(request.getCourseId()))
        .orElseThrow(() -> new CustomException(EError.COURSE_NOT_EXISTED));

    Pageable pageable = (request.getPage() != null && request.getSize() != null) ? PageRequest.of(request.getPage(),request.getSize(),
        Sort.by("joinedAt").descending()) : Pageable.unpaged();

    List<ClassMember> classMembers = classMemberRepository.findByCourseId(request.getCourseId(),pageable);

    List<ClassMemberResponse> responseList = classMembers.stream().map(member -> {
      ClassMemberResponse response = new ClassMemberResponse();
      response.setAccountId(String.valueOf(member.getAccount().getId()));
      response.setJoinedAt(member.getJoined_at());
      return response;
    }).toList();

    CommonResponse<?> commonResponse = new CommonResponse<>(200,responseList,"Get list member successfully");

    return ResponseEntity.ok(commonResponse);
  }
}
