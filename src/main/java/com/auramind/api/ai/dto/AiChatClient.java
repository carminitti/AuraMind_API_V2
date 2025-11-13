package com.auramind.api.ai;

import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiChatClient {

    private final RestTemplate rest;
    private final boolean enabled;
    private final String chatUrl;

    public AiChatClient(RestTemplate restTemplate) {
        this.rest = restTemplate;

        // PARA AGORA: IA DESATIVADA
        // Você não tem IA rodando no Render, então vamos deixar desativada.
        this.enabled = false;
        this.chatUrl = null;

        // 🔹 Quando quiser ligar a IA de verdade, você pode trocar esse construtor para ler
        // variáveis de ambiente, por exemplo:
        //
        // String baseUrl = System.getenv("APP_AI_BASE_URL");
        // String chatPath = System.getenv("APP_AI_CHAT_PATH");
        // ...
    }

    public ChatResponse chat(ChatRequest request) {
        // IA desativada → responde com texto padrão, não chama nada externo
        if (!enabled) {
            String fallback =
                "No momento a inteligência artificial do diário não está disponível. " +
                "Mas sua mensagem foi recebida pelo sistema. Tente novamente mais tarde.";

            return new ChatResponse(
                request.userId(),
                request.message(),
                fallback
            );
        }

        // (Código que chamaria a IA real quando você ativar no futuro)
        return rest.postForObject(chatUrl, request, ChatResponse.class);
    }
}
