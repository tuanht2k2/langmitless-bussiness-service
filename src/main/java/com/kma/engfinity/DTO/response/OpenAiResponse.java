package com.kma.engfinity.DTO.response;

import lombok.Data;
import java.util.List;

@Data
public class OpenAiResponse {

    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    @Data
    public static class Candidate {
        private Content content;
        private String role;
        private String finishReason;
        private double avgLogprobs;
    }

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;
    }
}

