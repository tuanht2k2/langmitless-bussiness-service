package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.AuthenticationRequest;
import com.kma.engfinity.DTO.request.EditAccountRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.service.AccountService;
import com.kma.engfinity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @PostMapping("/log-in")
    public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> register (@RequestBody EditAccountRequest request) {
        return accountService.createAccount(request);
    }

    @PostMapping("/check-valid-register-info")
    public ResponseEntity<?> checkValidRegisterInfo (@RequestBody EditAccountRequest request) {
        return authService.checkValidRegisterInfo(request);
    }
}
