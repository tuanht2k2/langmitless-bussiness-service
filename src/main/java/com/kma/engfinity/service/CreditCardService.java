package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditCreditCardRequest;
import com.kma.engfinity.constants.Constant.*;
import com.kma.engfinity.entity.CreditCard;
import com.kma.engfinity.repository.CreditCardRepository;
import com.kma.engfinity.service.interfaces.CreditCardServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardService implements CreditCardServiceInterface {
    private final CreditCardRepository creditCardRepository;
    private final FileService fileService;
    private final AuthService authService;

    @Override
    public Response<Object> create(EditCreditCardRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getCardNumber()) || ObjectUtils.isEmpty(request.getBank()) || ObjectUtils.isEmpty(request.getAccountId())) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, ErrorMessage.BAD_REQUEST);
            }

            boolean existsByCardNumber = creditCardRepository.existsByCardNumber(request.getCardNumber());
            boolean existsByAccountId = creditCardRepository.existsByAccountId(request.getAccountId());
            if (existsByCardNumber || existsByAccountId) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Credit card or account already existed!");
            }

            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber(request.getCardNumber());
            creditCard.setAccountId(request.getAccountId());
            creditCard.setBank(request.getBank());

            if (request.getQrImage() != null && !request.getQrImage().isEmpty()) {
                String qrImageUrl = fileService.getFileUrl(request.getQrImage());
                creditCard.setQrImage(qrImageUrl);
            }
            creditCardRepository.save(creditCard);

            return Response.getResponse(ErrorCode.OK, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when CreditCardService.create: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    @Override
    public Response<Object> update(EditCreditCardRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getAccountId())) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, ErrorMessage.BAD_REQUEST);
            }

            CreditCard creditCard = creditCardRepository.findByAccountId(request.getAccountId()).orElse(null);

            if (ObjectUtils.isEmpty(creditCard)) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Account do not have credit card");
            }

            if (!ObjectUtils.isEmpty(request.getCardNumber())) {
                creditCard.setCardNumber(request.getCardNumber());
            }
            if (!ObjectUtils.isEmpty(request.getBank())) {
                creditCard.setBank(request.getBank());
            }
            if (request.getQrImage() != null && !request.getQrImage().isEmpty()) {
                String qrImageUrl = fileService.getFileUrl(request.getQrImage());
                creditCard.setQrImage(qrImageUrl);
            }
            creditCardRepository.save(creditCard);

            return Response.getResponse(ErrorCode.OK, ErrorMessage.SUCCESS);

        } catch (Exception e) {
            log.error("An error occurred when CreditCardService.edit: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    @Override
    public Response<Object> details() {
        try {
            Account account = authService.getCurrentAccount();
            CreditCard creditCard = creditCardRepository.findByAccountId(account.getId()).orElse(null);
            if (ObjectUtils.isEmpty(creditCard)) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, ErrorMessage.BAD_REQUEST);
            }

            return Response.getResponse(ErrorCode.OK, creditCard, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when CreditCardService.details: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }
}
