package com.kma.engfinity.service.interfaces;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditWithdrawRequest;
import com.kma.engfinity.DTO.request.SearchWithdrawalRequest;

public interface WithdrawalRequestServiceInterface {
    Response<Object> create(EditWithdrawRequest request);
    Response<Object> update(EditWithdrawRequest request);
    Response<Object> delete(String[] ids);
    Response<Object> updateStatus(EditWithdrawRequest request);
    Response<Object> search(SearchWithdrawalRequest request);
}
