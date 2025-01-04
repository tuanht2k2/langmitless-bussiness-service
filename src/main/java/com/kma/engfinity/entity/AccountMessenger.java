package com.kma.engfinity.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts_messengers")
public class AccountMessenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String account;
    private String messenger;
}
