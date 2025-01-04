package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditReviewRequest;
import com.kma.engfinity.DTO.request.SearchReviewRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.ReviewResponse;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.entity.Review;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EReviewType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    public ResponseEntity<?> create (EditReviewRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        Review review = modelMapper.map(request, Review.class);
        review.setCreatedBy(currentAccount.getId());
        if (request.getType().equals(EReviewType.ACCOUNT)) {
            Optional<Account> optionalAccount = accountRepository.findById(request.getReviewedAccountId());
            if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
            review.setReviewedAccount(optionalAccount.get().getId());
        }
//        if (request.getType().equals(EReviewType.TOPIC)) {}
        reviewRepository.save(review);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Create review successfully!");

        return ResponseEntity.ok(response);
    }

//    public List<ReviewResponse> search (SearchReviewRequest request) {
//        reviewRepository
//    }
}

