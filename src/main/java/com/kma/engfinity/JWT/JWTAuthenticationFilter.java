package com.kma.engfinity.JWT;

import com.kma.common.entity.Account;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (!hasAuthorizationBearer(request)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String token = getToken(request);
        if (ObjectUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request,response);
            throw new CustomException(EError.UNAUTHENTICATED);
        }
        setAuthenticationContext(token, request);
        filterChain.doFilter(request,response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("c");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    private String getToken (HttpServletRequest request) {

//        String header = request.getHeader("Authorization");
//        return header.split(" ")[1].trim();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        String tokenFromParam = request.getParameter("token");
        return (tokenFromParam != null && !tokenFromParam.isEmpty()) ? tokenFromParam : null;
    }

    private void setAuthenticationContext (String token, HttpServletRequest request) {
        Account account = getAccount(token);

        UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(account, token , null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Account getAccount(String token) {
        String phoneNumber = jwtUtil.getSubject(token);
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        return optionalAccount.get();
    }
}
