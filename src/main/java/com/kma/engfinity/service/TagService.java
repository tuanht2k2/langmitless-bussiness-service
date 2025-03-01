package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditTagRequest;
import com.kma.engfinity.DTO.request.SearchTagRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.TagResponse;
import com.kma.engfinity.entity.Tag;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    public ResponseEntity<?> create (EditTagRequest request) {
        Account account = authService.getCurrentAccount();
        if (tagRepository.existsByName(request.getName())) throw new CustomException(EError.BAD_REQUEST);
        Tag tag = modelMapper.map(request, Tag.class);
        tag.setCreatedBy(account.getId());
        tagRepository.save(tag);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Create tag successfully!");
        return ResponseEntity.ok(response);
    }

    public Tag get (String id) {
        return tagRepository.findById(id).orElse(null);
    }

    public ResponseEntity<?> search (SearchTagRequest request) {
        List<Tag> tags = tagRepository.findByLanguageAndName(request.getLanguage(), request.getName());
        List<TagResponse> tagResponses = tags.stream().map(this::tagToTagResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, tagResponses, "Search tag successfully!");
        return ResponseEntity.ok(response);
    }

    public TagResponse tagToTagResponse (Tag tag) {
        return modelMapper.map(tag, TagResponse.class);
    }
}
