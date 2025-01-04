package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditFileRequest;
import com.kma.engfinity.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/v1/files")
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping("upload")
    public ResponseEntity<?> upload (@ModelAttribute EditFileRequest request) {
        return fileService.create(request);
    }

    @PostMapping("search")
    public ResponseEntity<?> search () {
        return fileService.search();
    }
}
