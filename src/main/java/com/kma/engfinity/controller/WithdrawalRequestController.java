package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditWithdrawRequest;
import com.kma.engfinity.DTO.request.SearchWithdrawalRequest;
import com.kma.engfinity.service.WithdrawalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/business/withdrawal-requests")
public class WithdrawalRequestController {
    @Autowired
    private WithdrawalRequestService withdrawalRequestService;

    @PostMapping("create")
    public Response<Object> create (@RequestBody EditWithdrawRequest request) {
        return withdrawalRequestService.create(request);
    }

    @PostMapping("update-status")
    public Response<Object> updateStatus (@ModelAttribute EditWithdrawRequest request) {
        return withdrawalRequestService.updateStatus(request);
    }

    @PostMapping("search")
    public Response<Object> search (@RequestBody SearchWithdrawalRequest request) {
        return withdrawalRequestService.search(request);
    }
}
