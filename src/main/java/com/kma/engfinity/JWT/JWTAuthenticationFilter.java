package com.kma.engfinity.JWT;

import com.kma.engfinity.DTO.request.EditRequestLogRequest;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.service.CommonService;
import com.kma.engfinity.service.RequestService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CommonService commonService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        EditRequestLogRequest logRequest = new EditRequestLogRequest();
        logRequest.setIp(commonService.getClientIpAddress(request));
        requestService.create(logRequest);

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getToken(request);
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request,response);
            return;
        }
        setAuthenticationContext(token, request);
        filterChain.doFilter(request,response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    private String getToken (HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1].trim();
    }

    private void setAuthenticationContext (String token, HttpServletRequest request) {
        Account account = getAccount(token);

        UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(account, null , null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Account getAccount(String token) {
        String phoneNumber = jwtUtil.getSubject(token);
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        return optionalAccount.get();
    }
}
