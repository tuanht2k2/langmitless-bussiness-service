package com.kma.engfinity.repository;

import com.kma.engfinity.DTO.response.WithdrawalResponse;
import com.kma.engfinity.entity.WithdrawalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, String> {
    @Query("""
        SELECT new com.kma.engfinity.DTO.response.WithdrawalResponse(
            w.id, w.createdAt, w.updatedAt, 
            a1.id, a1.name, a1.profileImage,
            w.amount, w.status, w.description, w.adminNote, w.imageUrl,
            a2.id, a2.name, a2.profileImage) 
        FROM WithdrawalRequest w 
        JOIN Account a1 ON w.createdBy = a1.id
        LEFT JOIN Account a2 ON w.processedBy = a2.id
        WHERE (:status IS NULL OR w.status = :status)
        AND (:createdBy IS NULL OR w.createdBy = :createdBy)   
    """)
    Page<WithdrawalResponse> search(@Param("status") String status, @Param("createdBy") String createdBy, Pageable pageable);
}
