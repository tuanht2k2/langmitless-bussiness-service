package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditCrashRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.entity.Crash;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.CrashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CrashService {
    @Autowired
    CrashRepository crashRepository;

    public ResponseEntity<?> create (EditCrashRequest request) {
        if (request.getCreatedBy() == null) throw new CustomException(EError.BAD_REQUEST);

        Crash crash = new Crash();
        crash.setIp(request.getIp());
        crash.setCreatedBy(request.getCreatedBy());
        crash.setError(request.getError());
        crash.setCreatedAt(new Date());
        Crash createdCrash = crashRepository.save(crash);
        CommonResponse<?> response = new CommonResponse<>(200, createdCrash, "Create crash successfully!");
        return ResponseEntity.ok(response);
    }
}
