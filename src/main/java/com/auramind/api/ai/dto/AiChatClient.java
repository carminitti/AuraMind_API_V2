package com.auramind.api.ai;

import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiChatClient {

    private final RestTemplate rest;
    private final String chatUrl;

    public AiChatClient(
            RestTemplate restTemplate,
            @Value("${app.ai.base-url}") String baseUrl,
            @Value("${app.ai.chat-path}") String chatPath
    ) {
        this.rest = restTemplate;
        this.chatUrl = baseUrl + chatPath;   // Ex.: https://IA/chat
    }

    public ChatResponse chat(ChatRequest request) {
        return rest.postForObject(chatUrl, request, ChatResponse.class);
    }
}
