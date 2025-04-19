package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMultiAccountBalanceRequest;
import com.kma.engfinity.DTO.request.EditPaymentRequest;
import com.kma.engfinity.entity.Payment;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EPaymentStatus;
import com.kma.engfinity.enums.EPaymentType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @Transactional
    public Payment create (EditPaymentRequest request) {
        try {
            Account currentAccount = authService.getCurrentAccount();

            Payment payment = new Payment();
            payment.setCreatedBy(currentAccount);
            payment.setAmount(request.getAmount());
            payment.setDescription(request.getDescription());
            payment.setId(request.getId());
            payment.setAmount(request.getAmount());
            payment.setType(request.getType());
            if (ObjectUtils.isEmpty(request.getStatus())) {
                payment.setStatus(EPaymentStatus.INIT);
            } else {
                payment.setStatus(request.getStatus());
            }
            payment.setCreatedAt(new Date());
            if (request.getType().equals(EPaymentType.TRANSFER)) {
                Optional<Account> receiver = accountRepository.findById(request.getReceiver());
                if (ObjectUtils.isEmpty(currentAccount.getBalance()) || currentAccount.getBalance() < request.getAmount()) throw new CustomException(EError.NOT_ENOUGH_MONEY);
                if (receiver.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
                payment.setReceiver(receiver.get());
            }
            if (payment.getStatus().equals(EPaymentStatus.DONE) && !ObjectUtils.isEmpty(payment.getReceiver())) {
                EditMultiAccountBalanceRequest balanceRequest = new EditMultiAccountBalanceRequest();
                balanceRequest.setBalance(payment.getAmount());
                balanceRequest.setSenderIds(Arrays.asList(payment.getCreatedBy().getId()));
                balanceRequest.setReceiverIds(Arrays.asList(payment.getReceiver().getId()));
                accountService.updateMultiAccountBalance(balanceRequest);
            }
            return paymentRepository.save(payment);
        } catch (Exception e) {
            log.error("An error occurred when create payment: {}", e.getMessage());
            return null;
        }
    }

    public void update (EditPaymentRequest request) {
        Optional<Payment> optionalPayment = paymentRepository.findById(request.getId());
        if (optionalPayment.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Payment payment = optionalPayment.get();
        payment.setStatus(request.getStatus());
        paymentRepository.save(payment);
    }

    public Payment get(String id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        return optionalPayment.get();
    }

    public void transfer (String receiver, Long amount, String description) {
        try {
            EditPaymentRequest editPaymentRequest = new EditPaymentRequest();
            editPaymentRequest.setAmount(amount);
            editPaymentRequest.setReceiver(receiver);
            editPaymentRequest.setDescription(description);
            editPaymentRequest.setStatus(EPaymentStatus.DONE);
            editPaymentRequest.setType(EPaymentType.TRANSFER);
            create(editPaymentRequest);
        } catch (Exception e) {
            log.error("An error occurred when transfer payment: {}", e.getMessage());
        }
    }
}
