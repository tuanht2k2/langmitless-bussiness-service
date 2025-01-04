package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EFileType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditFileRequest {
    private EFileType type;
    private MultipartFile file;
}
