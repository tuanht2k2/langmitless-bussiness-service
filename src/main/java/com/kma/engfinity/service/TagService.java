package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditTagRequest;
import com.kma.engfinity.DTO.request.SearchTagRequest;
import com.kma.engfinity.DTO.response.TagResponse;
import com.kma.engfinity.constants.Constant.*;
import com.kma.engfinity.entity.Tag;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public Response<Object> create (EditTagRequest request) {
        try {
            Account account = authService.getCurrentAccount();
            if (tagRepository.existsByNameAndLanguage(request.getName(), request.getLanguage())) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Từ khóa đã tồn tại, hãy kiểm tra lại!");
            }
            Tag tag = modelMapper.map(request, Tag.class);
            tag.setCreatedBy(account.getId());
            tagRepository.save(tag);
            return Response.getResponse(ErrorCode.OK, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when TagService.create: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    public Tag get (String id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Response<Object> search (SearchTagRequest request) {
        try {
            List<Tag> tags = tagRepository.findByLanguageAndName(request.getLanguage(), request.getName());
            List<TagResponse> tagResponses = tags.stream().map(this::tagToTagResponse).toList();
            return Response.getResponse(ErrorCode.OK, tagResponses, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when TagService.search: {}", e.getMessage());
            return Response.getResponse(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    public TagResponse tagToTagResponse (Tag tag) {
        return modelMapper.map(tag, TagResponse.class);
    }
}
