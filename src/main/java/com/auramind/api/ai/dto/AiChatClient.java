package com.auramind.api.ai;

import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiChatClient {

    private final RestTemplate rest;
    private final String chatUrl;
    private final boolean enabled;

    public AiChatClient(RestTemplate restTemplate) {
        this.rest = restTemplate;

        // Lê variáveis de ambiente diretamente (opcional)
        String baseUrl = System.getenv("APP_AI_BASE_URL");   // ex.: https://minha-ia.onrender.com/
        String chatPath = System.getenv("APP_AI_CHAT_PATH"); // ex.: "chat"

        if (baseUrl == null || baseUrl.isBlank()) {
            this.enabled = false;
            this.chatUrl = null;
        } else {
            if (chatPath == null || chatPath.isBlank()) {
                chatPath = "chat";
            }
            this.enabled = true;
            this.chatUrl = baseUrl + chatPath;
        }
    }

    public ChatResponse chat(ChatRequest request) {
        // IA desabilitada → resposta padrão, sem chamar nada externo
        if (!enabled) {
            String fallback =
                "No momento a inteligência artificial do diário não está disponível. " +
                "Mas sua mensagem foi recebida. Tente novamente mais tarde.";

            return new ChatResponse(
                request.userId(),
                request.message(),
                fallback
            );
        }

        // Quando você configurar a IA de verdade e as env vars, isso passa a funcionar
        return rest.postForObject(chatUrl, request, ChatResponse.class);
    }
}
