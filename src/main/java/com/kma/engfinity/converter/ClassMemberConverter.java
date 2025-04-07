package com.kma.engfinity.converter;

import com.kma.common.entity.Account;
import com.kma.engfinity.entity.ClassMember;
import com.kma.engfinity.entity.Course;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ClassMemberConverter {

  public static ClassMember toEntity(Course course, Account account) {
    if(course == null || account == null){
      return null;
    }
    ClassMember classMember = new ClassMember();
    classMember.setJoined_at(Instant.now());
    classMember.setCourse(course);
    classMember.setAccount(account);
    return classMember;
  }

}
