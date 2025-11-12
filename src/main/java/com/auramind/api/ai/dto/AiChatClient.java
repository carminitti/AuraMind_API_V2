package com.auramind.api.ai;

import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AiChatClient {

    private final WebClient aiWebClient;
    private final String chatPath;

    public AiChatClient(WebClient aiWebClient, @Value("${app.ai.chat-path}") String chatPath) {
        this.aiWebClient = aiWebClient;
        this.chatPath = chatPath; // "chat"
    }

    public ChatResponse chat(ChatRequest req) {
        return aiWebClient.post()
                .uri(chatPath)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .onErrorResume(ex ->
                        Mono.error(new RuntimeException("Falha ao chamar IA: " + ex.getMessage(), ex)))
                .block();
    }
}
