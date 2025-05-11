package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.common.enums.ERole;
import com.kma.engfinity.DTO.request.*;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.entity.Hire;
import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.ETransferType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.HireRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthService authService;

    @Autowired
    HireRepository hireRepository;

    @Autowired
    WebSocketService webSocketService;

    private BCryptPasswordEncoder passwordEncoder;

    AccountService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ResponseEntity<CommonResponse<String>> createAccount (EditAccountRequest editAccountRequest) {
        if (accountRepository.existsByEmail(editAccountRequest.getEmail()))
            throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByPhoneNumber(editAccountRequest.getPhoneNumber()))
            throw  new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        String encodedPassword = passwordEncoder.encode(editAccountRequest.getPassword());
        Account newAccount = modelMapper.map(editAccountRequest, Account.class);
        newAccount.setBalance(0L);
        newAccount.setCreatedAt(new Date());
        newAccount.setRole(ERole.USER);
        newAccount.setPassword(encodedPassword);
        accountRepository.save(newAccount);

        CommonResponse<String> response = new CommonResponse<>(200, "", "Create account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonResponse<?>> getAccount (String id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Account account = optionalAccount.get();
        Account currentAccount = authService.getCurrentAccount();
        CommonResponse<?> response;
        if (account.getId().equals(currentAccount.getId())) {
            PrivateAccountResponse accountResponse = modelMapper.map(account, PrivateAccountResponse.class);
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        } else {
            PublicAccountResponse accountResponse = modelMapper.map(account, PublicAccountResponse.class);
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Account getAccountById (String id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        return optionalAccount.get();
    }

    public PublicAccountResponse accountToPublicAccountResponse(Account account) {
        return modelMapper.map(account, PublicAccountResponse.class);
    }

    public ResponseEntity<?> search (SearchAccountRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        List<Account> accounts = accountRepository.search(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword(), request.getRole().name());
        List<PublicAccountResponse> accountResponses = accounts.stream().filter(x -> !Objects.equals(x.getId(), currentAccount.getId())).map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> delete (CommonDeleteRequest request) {
        accountRepository.deleteAllById(request.getIds());
        CommonResponse<?> response = new CommonResponse<>(200, null, "Delete accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> searchByPhoneNumbers (SearchAccountByPhoneNumbersRequest request) {
        List<Account> accounts = accountRepository.searchByPhoneNumbers(request.getPhoneNumbers());
        List<PublicAccountResponse> accountResponses = accounts.stream().map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update (EditAccountRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        if (request.getName() != null) account.setName(request.getName());
        if (request.getPhoneNumber() != null) account.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) account.setEmail(request.getEmail());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Update account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateRole (EditAccountRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        if (!currentAccount.getRole().equals(ERole.ADMIN)) throw new CustomException(EError.UNAUTHORIZED);
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        account.setRole(request.getRole());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Update role successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public void updateBalance(EditAccountBalanceRequest request) {
        try {
            Optional<Account> optionalAccount = accountRepository.findById(request.getId());
            if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
            Account account = optionalAccount.get();
            if (request.getType().equals(ETransferType.INCOMING)) {
                account.setBalance(account.getBalance() + request.getAmount());
            } else {
                if (account.getBalance() < request.getAmount()) throw new CustomException(EError.NOT_ENOUGH_MONEY);
                account.setBalance(account.getBalance() - request.getAmount());
            }
            accountRepository.save(account);
        } catch (Exception e) {
            log.error("An error occurred when updateBalance: {}", e.getMessage());
        }
    }

    @Transactional
    public void updateMultiAccountBalance (EditMultiAccountBalanceRequest request) {
        try {
            log.info("updateMultiAccountBalance request: {}", objectMapper.writeValueAsString(request));
            if (!ObjectUtils.isEmpty(request.getReceiverIds())) {
                accountRepository.updateBalance(request.getReceiverIds(), request.getBalance(), true);
                for (String id : request.getReceiverIds()) {
                    String destination = "/topic/" + id + "/notifications";
                    String content = "Giao dịch: + " + request.getBalance() + " VNĐ";
                    NotificationResponse response = new NotificationResponse(content);
                    webSocketService.sendData(destination, response);
                }
            }
            if (!ObjectUtils.isEmpty(request.getSenderIds())) {
                accountRepository.updateBalance(request.getSenderIds(), request.getBalance(), false);
                for (String id : request.getSenderIds()) {
                    String destination = "/topic/" + id + "/notifications";
                    String content = "Giao dịch: - " + request.getBalance() + " VNĐ";
                    NotificationResponse response = new NotificationResponse(content);
                    webSocketService.sendData(destination, response);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred when updateMultiAccountBalance: {}", e.getMessage());
        }
    }

    public ResponseEntity<?> becomeATeacher (EditAccountRequest request) {
        if (accountRepository.existsByIdentification(request.getIdentification())) throw new CustomException(EError.EXISTED_BY_IDENTIFICATION_NUMBER);
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        account.setRole(ERole.TEACHER);
        account.setAddress(request.getAddress());
        account.setGender(request.getGender());
        account.setFullName(request.getFullName());
        account.setIdentification(request.getIdentification());
        accountRepository.save(account);
        PrivateAccountResponse privateAccountResponse = modelMapper.map(account, PrivateAccountResponse.class);
        CommonResponse<?> response = new CommonResponse<>(200, privateAccountResponse, "Register successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateStatus (EditAccountStatusRequest request) {
        Account account = this.getAccountById(request.getId());
        if (request.getStatus().equals(EAccountStatus.BLOCKED) || request.getStatus().equals(EAccountStatus.OFFLINE)) {
            account.setLastSeen(new Date());
        }
        account.setStatus(request.getStatus());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, account, "Update status successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateTeacherInfo (EditTeacherRequest request) {
        Account account = this.getAccountById(request.getId());
        if (!account.getRole().equals(ERole.TEACHER)) throw new CustomException(EError.BAD_REQUEST);
        if (request.getCost() != null) account.setCost(request.getCost());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, account, "Update teacher info successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<Account> findAll () {
        return accountRepository.findAll();
    }

    public Response<Object> getByPhoneNumber (String phoneNumber) {
        try {
            PublicAccountResponse account = accountRepository.findPublicInfoByPhoneNumber(phoneNumber);
            if (ObjectUtils.isEmpty(account)) throw new CustomException(EError.USER_NOT_EXISTED);
            return Response.getResponse(200, account, "Get account successfully!");
        } catch (Exception e) {
            log.error("An error occurred when getByPhoneNumber: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }

    public Response<Object> searchHireHistory (SearchHireHistoryRequest request) {
        try {
            Account account = accountRepository.findById(request.getAccountId()).orElse(null);
            if (ObjectUtils.isEmpty(account)) throw new CustomException(EError.USER_NOT_EXISTED);

            List<Hire> hires = hireRepository.findByTeacher(request.getAccountId());
            PublicAccountResponse response = objectMapper.convertValue(account, PublicAccountResponse.class);
            response.setHireHistory(hires);

            return Response.getResponse(200, response, "Get account successfully!");
        } catch (Exception e) {
            log.error("An error occurred when getByPhoneNumber: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }
}
