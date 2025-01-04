package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.AuthenticationRequest;
import com.kma.engfinity.DTO.request.EditAccountRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.LoginResponse;
import com.kma.engfinity.DTO.response.PrivateAccountResponse;
import com.kma.engfinity.JWT.JWTUtil;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.ERole;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.repository.AccountRepository;
import com.nimbusds.jose.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper mapper;

    @Autowired
    JWTUtil jwtUtil;

    public ResponseEntity<CommonResponse<?>> authenticate (AuthenticationRequest request) {
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!isAuthenticated) throw new CustomException(EError.INCORRECT_PASSWORD);

        try {
            String token = jwtUtil.generateToken(account);
            PrivateAccountResponse accountResponse = mapper.map(account, PrivateAccountResponse.class);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setData(accountResponse);
            CommonResponse<LoginResponse> response = new CommonResponse<>(200, loginResponse, "Login successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public Account getCurrentAccount() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken != null && authenticationToken.getPrincipal() instanceof Account) {
            return (Account) authenticationToken.getPrincipal();
        }
        return null;
    }

    public ResponseEntity<?> checkValidRegisterInfo(EditAccountRequest request) {
        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) throw new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        if (accountRepository.existsByEmail(request.getEmail())) throw new CustomException(EError.EXISTED_BY_EMAIL);
        CommonResponse<?> response = new CommonResponse<>(200, true, "Info is valid!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
