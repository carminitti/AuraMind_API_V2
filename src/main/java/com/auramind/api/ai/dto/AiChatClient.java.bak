package com.auramind.api.ai;

import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Objects;

@Component
public class AiChatClient {

    private final RestTemplate rest;
    private final boolean enabled;
    private final String chatUrl;
    private final String authHeaderValue; // opcional, caso queira passar token p/ IA

    public AiChatClient(RestTemplate restTemplate,
                        @Value("${app.ai.enabled:false}") boolean enabled,
                        @Value("${app.ai.url:}") String chatUrl,
                        @Value("${app.ai.auth:}") String authHeaderValue) {
        this.rest = restTemplate;
        this.enabled = enabled;
        this.chatUrl = chatUrl;
        this.authHeaderValue = authHeaderValue;
    }

    /**
     * Chama o endpoint da IA (chatUrl) enviando o ChatRequest.
     * Se app.ai.enabled=false ou chatUrl vazio -> retorna resposta fallback amigável.
     */
    public ChatResponse chat(ChatRequest request) {
        if (!enabled || chatUrl == null || chatUrl.trim().isEmpty()) {
            String fallback = "No momento a inteligência artificial do diário não está disponível. " +
                    "Mas sua mensagem foi recebida pelo sistema. Tente novamente mais tarde.";
            return new ChatResponse(request.userId(), request.message(), fallback);
        }

        try {
            // monta o header (json)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // se você configurar algum token para a IA via env, adiciona aqui:
            if (authHeaderValue != null && !authHeaderValue.isBlank()) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeaderValue);
            }

            HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<ChatResponse> resp = rest.postForEntity(chatUrl, entity, ChatResponse.class);
            if (resp != null && resp.getBody() != null) {
                return resp.getBody();
            } else {
                String fallback = "A IA retornou resposta vazia. Tente novamente mais tarde.";
                return new ChatResponse(request.userId(), request.message(), fallback);
            }
        } catch (Exception e) {
            // log no console para depuração (substitua por logger se quiser)
            System.err.println("Erro ao chamar IA em " + chatUrl + ": " + e.getMessage());
            String fallback = "No momento a inteligência artificial do diário não está indisponível. " +
                    "Mas sua mensagem foi recebida. Tente novamente mais tarde.";
            return new ChatResponse(request.userId(), request.message(), fallback);
        }
    }
}
