package com.auramind.api.diary;

import com.auramind.api.ai.AiChatClient;
import com.auramind.api.ai.dto.ChatDtos;
import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final AiChatClient ai;

    @Data
    public static class DiaryReq {
        @NotBlank private String message;
        private List<ChatDtos.Msg> history; // opcional, para manter contexto
    }

    @Data
    public static class DiaryRes {
        private String aiReply;
    }

    @PostMapping("/message")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DiaryRes> message(
            @RequestBody DiaryReq req,
            @RequestHeader(value = "X-User-Id", required = false) String userIdFromHeader
    ) {
        final String userId = (userIdFromHeader == null || userIdFromHeader.isBlank())
                ? "anonymous" : userIdFromHeader;

        // Se não veio histórico, cria um com system default igual ao Python
        List<ChatDtos.Msg> history = (req.getHistory() != null) ? req.getHistory() : new ArrayList<>();
        boolean hasSystem = history.stream().anyMatch(m -> "system".equalsIgnoreCase(m.role()));
        if (!hasSystem) {
            history.add(new ChatDtos.Msg(
                    "system",
                    "Você é um psicólogo clínico. Responda de forma empática e profissional ao cliente."
            ));
        }

        ChatRequest aiReq = new ChatRequest(userId, req.getMessage(), history);
        ChatResponse aiRes = ai.chat(aiReq);

        DiaryRes res = new DiaryRes();
        res.setAiReply(aiRes.botReply());
        return ResponseEntity.ok(res);
    }
}
