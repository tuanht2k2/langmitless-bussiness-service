package com.kma.engfinity.repository;

import com.kma.engfinity.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    boolean existsByCardNumber(String cardNumber);

    boolean existsByAccountId(String accountId);

    Optional<CreditCard> findByAccountId(String accountId);
}
