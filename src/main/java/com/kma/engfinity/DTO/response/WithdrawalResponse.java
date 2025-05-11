package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalResponse {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private PublicAccountResponse createdBy;
    private String updatedBy;
    private Long amount;
    private String status;
    private String description;
    private String adminNote;
    private String imageUrl;
    private PublicAccountResponse processedBy;

    public WithdrawalResponse(String id, Date createdAt, Date updatedAt, String creatorId, String creatorName, String creatorProfileImage, Long amount, String status, String description, String adminNote, String imageUrl, String processorId, String processorName, String processorProfileImage) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        PublicAccountResponse createdBy = new PublicAccountResponse();
        createdBy.setId(creatorId);
        createdBy.setName(creatorName);
        createdBy.setProfileImage(creatorProfileImage);
        this.createdBy = createdBy;

        this.amount = amount;
        this.status = status;
        this.description = description;
        this.adminNote = adminNote;
        this.imageUrl = imageUrl;

        PublicAccountResponse processedBy = new PublicAccountResponse();
        processedBy.setId(processorId);
        processedBy.setName(processorName);
        processedBy.setProfileImage(processorProfileImage);
        this.processedBy = processedBy;
    }
}
