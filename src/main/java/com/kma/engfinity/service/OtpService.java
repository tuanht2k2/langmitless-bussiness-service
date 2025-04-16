package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.OtpRequest;
import com.kma.engfinity.DTO.response.OtpResponse;
import com.kma.engfinity.constants.Constant;
import com.kma.engfinity.entity.EmailStatus;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.EmailStatusRepository;
import com.kma.engfinity.service.interfaces.OtpServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class OtpService implements OtpServiceInterface {
    @Autowired
    private EmailStatusRepository emailStatusRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSenderService mailSenderService;

    @Value("${OTP.MAX_RESEND}")
    private Integer maxResend;

    @Value("${OTP.MAX_RETRY}")
    private Integer maxRetry;

    @Override
    public Response<Object> getOtp() {
        try {
            Account account = authService.getCurrentAccount();
            EmailStatus emailStatus = emailStatusRepository.findByEmail(account.getEmail()).orElse(null);

            OtpResponse otpResponse = new OtpResponse();
            if (emailStatus == null) {
                EmailStatus newEmailStatus = new EmailStatus();
                newEmailStatus.setEmail(account.getEmail());
                updateEmailStatus(newEmailStatus);
                otpResponse.setRemainSent(newEmailStatus.getRemainSent());
                return Response.getResponse(200, otpResponse,"Get response successful");
            }
            updateEmailStatus(emailStatus);
            otpResponse.setRemainSent(emailStatus.getRemainSent());
            return Response.getResponse(200, otpResponse,"Get response successful");

        } catch (Exception e) {
            log.error("An error happened when getOtp", e);
            return Response.getResponse(500, e.getMessage());
        }
    }

    private String genOtp () {
        return String.valueOf(99999 + new Random().nextInt(900000));
    }

    private void updateEmailStatus (EmailStatus emailStatus) {
        if (emailStatus.getTimeBlocked() != null && emailStatus.getTimeBlocked().after(new Date()) && 0 == emailStatus.getRemainSent()) {
            throw new CustomException(EError.PHONE_LOCKED);
        }

        if (emailStatus.getRemainSent() != null && emailStatus.getRemainSent() > 0) {
            emailStatus.setRemainSent(emailStatus.getRemainSent() - 1);
        } else {
            emailStatus.setRemainSent(maxResend - 1);
        }
        if (emailStatus.getRemainSent() == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, 1);
            emailStatus.setTimeBlocked(calendar.getTime());
        }
        emailStatus.setRetryTime(maxRetry);
        String otp = genOtp();
        emailStatus.setOtp(passwordEncoder.encode(otp));
        String emailBody = "Mã OTP chuyển tiền của bạn là: " + otp +" , mã sẽ có hiệu lực trong 5p, không cung cấp mã cho bất kỳ ai";
        String emailSubject = "[Langmitless - Mã xác nhận chuyển tiền]";
        mailSenderService.sendEmail(emailStatus.getEmail(), emailSubject, emailBody);
        emailStatusRepository.save(emailStatus);
    }

    @Override
    public Response<Object> verifyOtp (OtpRequest request) {
        try {
            Account account = authService.getCurrentAccount();
            EmailStatus emailStatus = emailStatusRepository.findByEmail(account.getEmail()).orElse(null);
            if (emailStatus == null) {
                throw new CustomException(EError.BAD_REQUEST);
            }
            if (null != emailStatus.getRetryTime() && emailStatus.getRetryTime().equals(0)) {
                throw new CustomException(EError.RESEND_OTP);
            }

            emailStatus.setRetryTime(emailStatus.getRetryTime() - 1);
            boolean isOtpCorrect = passwordEncoder.matches(request.getOtp(), emailStatus.getOtp());
            emailStatus.setStatus(isOtpCorrect ? Constant.Status.OTP_SUCCESS : Constant.Status.OTP_FAILED);
            if (isOtpCorrect) emailStatus.setRemainSent(5);
            emailStatusRepository.save(emailStatus);

            OtpResponse otpResponse = new OtpResponse();
            otpResponse.setRetryTime(emailStatus.getRetryTime());
            otpResponse.setCorrect(isOtpCorrect);
            return Response.getResponse(200, otpResponse, "Verify otp successful");
        } catch (Exception e) {
            log.error("An error happened when verifyOtp: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }
}
