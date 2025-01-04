package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OpenAiRequest {

    private List<Content> contents = new ArrayList<>();

    @Data
    public static class Content {
        private List<Part> parts = new ArrayList<>();
    }

    @Data
    public static class Part {
        private String text;
    }
}
