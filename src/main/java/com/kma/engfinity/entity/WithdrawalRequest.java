package com.kma.engfinity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "withdrawal_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "admin_note", length = 255)
    private String adminNote;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "processed_by", length = 50)
    private String processedBy;
}

