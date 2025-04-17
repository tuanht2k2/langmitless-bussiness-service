package com.kma.engfinity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailStatus {
    @Id
    private String email;
    private String otp;
    private Integer remainSent;
    private Date timeBlocked;
    private String status;
    private Integer retryTime;
}
