package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditPaymentRequest;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.entity.Payment;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EPaymentStatus;
import com.kma.engfinity.enums.EPaymentType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    public Payment create (EditPaymentRequest request) {
        Account currentAccount = authService.getCurrentAccount();

        Payment payment = new Payment();
        payment.setCreatedBy(currentAccount);
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setId(request.getId());
        payment.setAmount(request.getAmount());
        payment.setType(request.getType());
        payment.setStatus(EPaymentStatus.INIT);
        payment.setCreatedAt(new Date());
        if (request.getType().equals(EPaymentType.TRANSFER)) {
            Optional<Account> receiver = accountRepository.findById(request.getReceiver());
            if (receiver.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
            payment.setReceiver(receiver.get());
        }
        return paymentRepository.save(payment);
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
}
