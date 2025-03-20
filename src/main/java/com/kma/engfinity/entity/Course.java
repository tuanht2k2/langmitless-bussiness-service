package com.kma.engfinity.entity;

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
@Table(name = "courses", indexes = {
        @Index(name = "idx_course_name", columnList = "name"),
        @Index(name = "idx_course_created_at", columnList = "created_at"),
        @Index(name = "idx_course_updated_at", columnList = "updated_at")
})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private String name;

    @Column(length = 1024)
    private String description;

    private Long cost = 0L;

    private String language;

    private Byte level;
}

