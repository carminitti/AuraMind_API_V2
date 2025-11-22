package com.auramind.api.ai;
import com.auramind.api.ai.dto.ChatDtos;
import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * AiChatClient - robust RestTemplate-based client to call external AI service.
 * Replaces incomplete version that caused startup / runtime errors.
 */
@Component
public class AiChatClient {

    private final RestTemplate rest;
    private final boolean enabled;
    private final String chatUrl;
    private final String authHeaderValue;

    public AiChatClient(RestTemplate restTemplate,
                        @Value("${app.ai.enabled:false}") boolean enabled,
                        @Value("${app.ai.url:}") String chatUrl,
                        @Value("${app.ai.auth:}") String authHeaderValue) {
        this.rest = restTemplate;
        this.enabled = enabled;
        this.chatUrl = chatUrl;
        this.authHeaderValue = authHeaderValue;
    }


    

    public ChatResponse chat(ChatRequest request) {
        if (!enabled || chatUrl == null || chatUrl.isBlank()) {
            String fallback = "Serviço de IA indisponível. Tente novamente mais tarde.";
            return new ChatResponse(request.userId(), request.message(), fallback);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (authHeaderValue != null && !authHeaderValue.isBlank()) {
                headers.set("Authorization", authHeaderValue);
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
            System.err.println("Erro ao chamar IA em " + chatUrl + ": " + e.getMessage());
            String fallback = "No momento a inteligência artificial do diário não está indisponível. " +
                              "Mas sua mensagem foi recebida. Tente novamente mais tarde.";
            return new ChatResponse(request.userId(), request.message(), fallback);
        }
    }
}
