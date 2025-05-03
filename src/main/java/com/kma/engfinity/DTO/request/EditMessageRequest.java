package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EMessageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EditMessageRequest {
    private String id;
    private String messengerId;
    private String content;
//    private EMessageType type;
    private List<MultipartFile> files;
}
