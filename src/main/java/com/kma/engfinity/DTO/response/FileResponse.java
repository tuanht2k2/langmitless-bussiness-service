package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EFileType;
import lombok.Data;

@Data
public class FileResponse {
    private String id;
    private String url;
    private EFileType type;
    private String postId;
}
