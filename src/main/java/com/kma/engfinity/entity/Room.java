package com.kma.engfinity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kma.engfinity.enums.ERoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "accounts_rooms",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> members = new HashSet<>();

    private Integer expectedCallDuration;

    private Integer actualCallDuration;

    @Enumerated(EnumType.STRING)
    private ERoomStatus status;

    @OneToOne(mappedBy = "room", fetch = FetchType.EAGER)
    @JsonIgnore
    private Hire hire;

}
