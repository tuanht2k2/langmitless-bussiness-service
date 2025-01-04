package com.kma.engfinity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "crashes")
public class Crash {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String ip;

    private String error;

    private String createdBy;

    private Date createdAt;
}
