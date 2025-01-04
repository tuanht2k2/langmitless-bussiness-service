package com.kma.engfinity.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public String getAccountNotificationUrl (String accountId) {
        return "/topic/" + accountId + "/notifications";
    }

    public String getMessengerWebRTCUrl (String accountId) {
        return "/topic/accounts/" + accountId + "/messengers";
    }

    public String getClientIpAddress (HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0]; // Lấy IP đầu tiên trong header
        }
        return request.getRemoteAddr();
    }
}
