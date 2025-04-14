package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.*;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getAccount (@PathVariable String id) {
        return accountService.getAccount(id);
    }

    @PostMapping("search")
    public ResponseEntity<?> search (@RequestBody SearchAccountRequest request) {
        return accountService.search(request);
    }

    @PostMapping("delete")
    public ResponseEntity<?> delete (@RequestBody CommonDeleteRequest request) {
        return accountService.delete(request);
    }

    @PostMapping("search-by-phone-numbers")
    public ResponseEntity<?> searchByPhoneNumbers (@RequestBody SearchAccountByPhoneNumbersRequest request) {
        return accountService.searchByPhoneNumbers(request);
    }

    @PostMapping("update")
    public ResponseEntity<?> update (@RequestBody EditAccountRequest request) {
        return accountService.update(request);
    }

    @PostMapping("update-role")
    public ResponseEntity<?> updateRole (@RequestBody EditAccountRequest request) {
        return accountService.updateRole(request);
    }

    @PostMapping("become-a-teacher")
    public ResponseEntity<?> becomeATeacher (@RequestBody EditAccountRequest request) {
        return accountService.becomeATeacher(request);
    }

    @PostMapping("update-status")
    public ResponseEntity<?> updateStatus (@RequestBody EditAccountStatusRequest request) {
        return accountService.updateStatus(request);
    }

    @PutMapping("teachers")
    public ResponseEntity<?> updateTeacherInfo (@RequestBody EditTeacherRequest request) {
        return accountService.updateTeacherInfo(request);
    }

    @PostMapping("find-by-phone")
    public Response<Object> findByPhone (@RequestBody SearchAccountByPhoneNumbersRequest request) {
        return accountService.getByPhoneNumber(request.getPhoneNumbers().getFirst());
    }

}
