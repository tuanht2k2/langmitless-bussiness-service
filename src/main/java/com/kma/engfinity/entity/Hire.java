package com.kma.engfinity.entity;

import com.kma.engfinity.enums.EHireStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hires")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Hire {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @ManyToOne
    @JoinColumn(name = "teacher")
    private Account teacher;

    private Long cost;

    private Byte totalTime;

    @Enumerated(EnumType.STRING)
    private EHireStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room")
    private Room room;
}
