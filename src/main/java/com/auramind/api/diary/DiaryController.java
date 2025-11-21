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
public DiaryDTOs.DiaryRes message(@RequestBody DiaryDTOs.DiaryReq req, Authentication authentication) {
    String email = null;
    Long userId = null;
    if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails ud) {
        email = ud.getUsername();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userId = user.getId();
    } else {
        throw new RuntimeException("Usuário não autenticado");
    }

    // montar ChatRequest (use ChatDtos.ChatRequest)
    var history = req.history(); // adaptar ao DTO
    var chatReq = new com.auramind.api.ai.dto.ChatDtos.ChatRequest(String.valueOf(userId), req.getMessage(), history, "");
    var aiResp = aiChatClient.chat(chatReq); // ajustado ao seu client
    // opcional: salvar no DB...
    return new DiaryDTOs.DiaryRes(aiResp.botReply());
}

