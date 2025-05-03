package com.kma.engfinity.entity;

import com.kma.engfinity.enums.EMessengerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "messengers")
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private EMessengerType type;

    private String name;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    private String createdBy;

    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;
}
