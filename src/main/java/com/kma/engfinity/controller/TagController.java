package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditTagRequest;
import com.kma.engfinity.DTO.request.SearchTagRequest;
import com.kma.engfinity.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping
    public Response<Object> create (@RequestBody EditTagRequest request) {
        return tagService.create(request);
    }

    @PostMapping("search")
    public Response<Object> search (@RequestBody SearchTagRequest request) {
        return tagService.search(request);
    }
}
