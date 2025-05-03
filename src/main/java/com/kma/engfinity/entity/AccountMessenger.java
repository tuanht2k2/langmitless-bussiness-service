package com.kma.engfinity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts_messengers")
public class AccountMessenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String accountId;
    private String messengerId;
}
