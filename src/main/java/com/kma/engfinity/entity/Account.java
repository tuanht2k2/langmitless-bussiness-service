package com.kma.engfinity.entity;

import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EGender;
import com.kma.engfinity.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "accounts")
@Entity
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAccountStatus status;

    @Column(name = "balance")
    private Long balance = 0L;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "identification")
    private String identification;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "last_seen")
    private Date lastSeen;

    @Column(name = "cost")
    private Long cost;
}

