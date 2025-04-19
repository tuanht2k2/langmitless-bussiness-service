package com.kma.engfinity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountsCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String courseId;

    private String accountId;
}
