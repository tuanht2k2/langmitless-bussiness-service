package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditRequestLogRequest;
import com.kma.engfinity.DTO.request.StatisticsRequest;
import com.kma.engfinity.entity.Request;
import com.kma.engfinity.repository.RequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ModelMapper modelMapper;

    public void create(EditRequestLogRequest request) {
        Request requestLog = modelMapper.map(request, Request.class);
        requestLog.setCreatedAt(new Date());
        requestRepository.save(requestLog);
    }

    public List<Request> statistics(StatisticsRequest request) {
        return requestRepository.statistics(request.getFrom(), request.getTo());
    }
}
