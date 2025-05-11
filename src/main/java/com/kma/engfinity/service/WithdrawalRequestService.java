package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.common.enums.ERole;
import com.kma.engfinity.DTO.request.EditMultiAccountBalanceRequest;
import com.kma.engfinity.DTO.request.EditWithdrawRequest;
import com.kma.engfinity.DTO.request.SearchWithdrawalRequest;
import com.kma.engfinity.DTO.response.WithdrawalResponse;
import com.kma.engfinity.constants.Constant.*;
import com.kma.engfinity.entity.WithdrawalRequest;
import com.kma.engfinity.repository.WithdrawalRequestRepository;
import com.kma.engfinity.service.interfaces.WithdrawalRequestServiceInterface;
import com.kma.engfinity.utils.DtoUtils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@Transactional
public class WithdrawalRequestService implements WithdrawalRequestServiceInterface {
    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WithdrawalRequestRepository withdrawalRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileService fileService;

    @Override
    public Response<Object> create(EditWithdrawRequest request) {
        try {
            Account account = authService.getCurrentAccount();

            if (ObjectUtils.isEmpty(request.getAmount()) || ObjectUtils.isEmpty(account.getBalance()) || account.getBalance() < request.getAmount()) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, ErrorMessage.BAD_REQUEST);
            }

            WithdrawalRequest withdrawalRequest = objectMapper.convertValue(request, WithdrawalRequest.class);
            withdrawalRequest.setCreatedAt(new Date());
            withdrawalRequest.setCreatedBy(account.getId());
            withdrawalRequest.setStatus(WithdrawalRequestStatus.INIT);
            withdrawalRequestRepository.save(withdrawalRequest);

            EditMultiAccountBalanceRequest accountBalanceRequest = new EditMultiAccountBalanceRequest();
            accountBalanceRequest.setBalance(request.getAmount());
            accountBalanceRequest.setSenderIds(Arrays.asList(account.getId()));
            accountService.updateMultiAccountBalance(accountBalanceRequest);

            return Response.getResponse(200, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred while WithdrawalRequestService.create", e);
            return Response.getResponse(ErrorCode.SERVICE_ERROR, ErrorMessage.SERVICE_ERROR);
        }
    }

    @Override
    public Response<Object> update(EditWithdrawRequest request) {
        return null;
    }

    @Override
    public Response<Object> delete(String[] ids) {
        return null;
    }

    @Override
    public Response<Object> updateStatus(EditWithdrawRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getId())) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Withdraw request id is empty");
            }
            WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(request.getId()).orElse(null);
            if (ObjectUtils.isEmpty(withdrawalRequest)) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Withdraw request id is not valid");
            }
            Account account = authService.getCurrentAccount();

            if (account.getRole().equals(ERole.ADMIN) ||
                    (account.getId().equals(withdrawalRequest.getCreatedBy()) &&
                            Arrays.asList(WithdrawalRequestStatus.CANCELLED).contains(withdrawalRequest.getStatus()))) {

                if (request.getStatus().equals(WithdrawalRequestStatus.ACCEPTED)) {
                    if (request.getImage() == null) {
                        return Response.getResponse(ErrorCode.BAD_REQUEST, "Withdraw request image is empty");
                    }
                    withdrawalRequest.setProcessedBy(account.getId());
                    String fileUrl = fileService.getFileUrl(request.getImage());
                    withdrawalRequest.setImageUrl(fileUrl);
                }
                if (!ObjectUtils.isEmpty(request.getAdminNote())) {
                    withdrawalRequest.setAdminNote(request.getAdminNote());
                }
                withdrawalRequest.setStatus(request.getStatus());
                withdrawalRequest.setUpdatedBy(account.getId());
                account.setUpdatedAt(new Date());
                withdrawalRequestRepository.save(withdrawalRequest);
                return Response.getResponse(ErrorCode.OK, ErrorMessage.SUCCESS);
            }

            return Response.getResponse(ErrorCode.BAD_REQUEST, ErrorMessage.PERMISSION_DENIED);
        } catch (Exception e) {
            log.error("An error occurred while WithdrawalRequestService.updateStatus: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, ErrorMessage.SERVICE_ERROR);
        }
    }

    @Override
    public Response<Object> search(SearchWithdrawalRequest request) {
        try {
            Pageable pageable = PageableBuilder.build(request.getPage(), request.getPageSize(), request.getSortBy(), request.getSortDir());
            Page<WithdrawalResponse> response = withdrawalRequestRepository.search(request.getStatus(), request.getCreatedBy(), pageable);

            return Response.getResponse(ErrorCode.OK, response, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when WithdrawalRequestService.search: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }
}
