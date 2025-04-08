package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.ClassMemberListRequest;
import com.kma.engfinity.DTO.request.ClassMemberRequest;
import com.kma.engfinity.service.ClassMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/class")
public class ClassMemberController {

  @Autowired
  private ClassMemberService classMemberService;

  @PostMapping("/addMember")
  public ResponseEntity<?> addMemberToClass(@RequestBody ClassMemberRequest request) {
    return classMemberService.addMemberToClass(request);
  }

  @PostMapping("/listMembers")
  public ResponseEntity<?> getListMemberClass(@RequestBody ClassMemberListRequest request) {
    return classMemberService.getListMemberClass(request);
  }
}
