package com.kma.engfinity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CREDIT_CARDS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditCard {
    @Id
    private String cardNumber;
    private String bank;
    private String accountId;
    private String qrImage;
}
