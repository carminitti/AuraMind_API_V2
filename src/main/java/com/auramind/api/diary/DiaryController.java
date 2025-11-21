package com.auramind.api.diary;

import com.auramind.api.ai.AiChatClient;
import com.auramind.api.ai.dto.ChatDtos;
import com.auramind.api.ai.dto.ChatDtos.ChatRequest;
import com.auramind.api.ai.dto.ChatDtos.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final AiChatClient ai;
    private final UserRepository userRepository;

    public DiaryController(AiChatClient ai, UserRepository userRepository) {
        this.ai = ai;
        this.userRepository = userRepository;
    }

    @PostMapping("/message")
    public DiaryDTOs.DiaryRes message(
            @RequestBody DiaryDTOs.DiaryReq req,
            Authentication authentication
    ) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        var principal = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Long userId = user.getId();

        // monta o histórico
        List<DiaryDTOs.Message> history = req.getHistory();

        // monta requisição para IA
        ChatRequest chatReq = new ChatRequest(
                String.valueOf(userId),
                req.getMessage(),
                history,
                ""
        );

        // chama IA
        ChatResponse aiResp = ai.chat(chatReq);

        // devolve resposta da IA
        return new DiaryDTOs.DiaryRes(aiResp.botReply());
    }
}
