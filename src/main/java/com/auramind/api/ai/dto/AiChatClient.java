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
    private final boolean enabled;

    public AiChatClient(
            RestTemplate restTemplate,
            // se não tiver valor configurado, fica string vazia
            @Value("${app.ai.base-url:}") String baseUrl,
            @Value("${app.ai.chat-path:chat}") String chatPath
    ) {
        this.rest = restTemplate;

        if (baseUrl == null || baseUrl.isBlank()) {
            // IA desativada (sem URL configurada)
            this.enabled = false;
            this.chatUrl = null;
        } else {
            this.enabled = true;
            this.chatUrl = baseUrl + chatPath;  // ex.: https://sua-ia.onrender.com/chat
        }
    }

    public ChatResponse chat(ChatRequest request) {
        // Se IA não estiver configurada, responde algo padrão e NÃO chama serviço externo
        if (!enabled) {
            String fallback =
                    "No momento a inteligência artificial do diário não está disponível. " +
                    "Mas sua mensagem foi recebida pelo sistema. " +
                    "Tente novamente mais tarde.";

            return new ChatResponse(
                    request.userId(),
                    request.message(),
                    fallback
            );
        }

        // Quando você configurar a IA de verdade (base-url), essa parte passa a funcionar
        return rest.postForObject(chatUrl, request, ChatResponse.class);
    }
}
