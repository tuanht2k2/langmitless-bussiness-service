package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditMessengerRequest;
import com.kma.engfinity.repository.AccountMessengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountMessengerService {
    @Autowired
    private AccountMessengerRepository accountMessengerRepository;

    public void create(String sql) {
        accountMessengerRepository.bulkInsert(sql);
    }
}
