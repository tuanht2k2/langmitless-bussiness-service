package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditCrashRequest;
import com.kma.engfinity.DTO.request.StatisticsRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.CrashStatisticsResponse;
import com.kma.engfinity.entity.Crash;
import com.kma.engfinity.entity.Request;
import com.kma.engfinity.repository.CrashRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CrashService {
    @Autowired
    CrashRepository crashRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RequestService requestService;
    @Autowired
    AuthService authService;
    @Autowired
    ModelMapper mapper;


    public ResponseEntity<?> create (EditCrashRequest request) {
        Account account = authService.getCurrentAccount();
//        if (request.getCreatedBy() == null) throw new CustomException(EError.BAD_REQUEST);

        Crash crash = mapper.map(request, Crash.class);
        crash.setCreatedBy(account.getId());
        crash.setCreatedAt(new Date());
        Crash createdCrash = crashRepository.save(crash);
        CommonResponse<?> response = new CommonResponse<>(200, createdCrash, "Create crash successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> statistics (StatisticsRequest request) {
        List<Crash> crashes = crashRepository.statistics(request.getFrom(), request.getTo());
        List<Request> requestLogs = requestService.statistics(request);
        CrashStatisticsResponse crashStatisticsResponse = new CrashStatisticsResponse();
        crashStatisticsResponse.setCrashes(crashes);
        crashStatisticsResponse.setRequests(requestLogs);
        crashStatisticsResponse.setTotalUser(accountService.findAll().size());
        CommonResponse<?> response = new CommonResponse<>(200, crashStatisticsResponse, "Statistics successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> test () throws InterruptedException {
        Thread.sleep(1000);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Statistics successfully!");
        return ResponseEntity.ok(response);
    }
}
