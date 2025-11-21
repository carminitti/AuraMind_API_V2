package com.auramind.api.diary;

import com.auramind.api.ai.AiChatClient;
import com.auramind.api.ai.dto.ChatDtos;
import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final AiChatClient ai;

    public DiaryController(AiChatClient ai) {
        this.ai = ai;
    }

    @PostMapping("/message")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DiaryDTOs.DiaryRes> message(
            @RequestBody DiaryDTOs.DiaryReq req,
            @RequestHeader(value = "X-User-Id", required = false) String uid
    ) {
        String userId = (uid == null ? "anonymous" : uid);

        List<ChatDtos.Msg> history = req.getHistory();
        if (history == null) history = new ArrayList<>();

        boolean hasSystem = history.stream().anyMatch(m -> "system".equals(m.role()));
        if (!hasSystem) {
            history.add(new ChatDtos.Msg("system",
                    "Você é um psicólogo clínico, responda de forma humana e empática."));
        }

        ChatRequest aiReq = new ChatRequest(userId, req.getMessage(), history, "");

        ChatResponse aiRes = ai.chat(aiReq);

        DiaryDTOs.DiaryRes res = new DiaryDTOs.DiaryRes();
        res.setAiReply(aiRes.botReply());

        return ResponseEntity.ok(res);
    }
}
