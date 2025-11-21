package com.auramind.api.ai.dto;

import java.util.List;

public class ChatDtos {

    public record Msg(String role, String content) {}

    public record ChatRequest(
            String userId,
            String message,
            List<Msg> history,
            String profile
    ) {}

    public record ChatResponse(
            String userId,
            String message,
            String botReply
    ) {}
}
