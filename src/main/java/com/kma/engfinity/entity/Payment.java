package com.kma.engfinity.entity;

import com.kma.common.entity.Account;
import com.kma.engfinity.enums.EPaymentStatus;
import com.kma.engfinity.enums.EPaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    private String id;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private Account receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EPaymentType type;

    private Long amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EPaymentStatus status;
}
