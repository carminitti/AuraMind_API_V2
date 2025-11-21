package com.auramind.api.diary;

import com.auramind.api.ai.AiChatClient;
import com.auramind.api.ai.dto.ChatDtos;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.auramind.api.ai.dto.ChatDtos;


@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final AiChatClient aiChatClient;

    @PostMapping("/send")
    public DiaryDTOs.DiaryRes sendMessage(
            @RequestBody DiaryDTOs.DiaryReq req,
            Authentication auth
    ) {
        // pega o e-mail do usuário autenticado
        String userEmail = auth.getName();

        // Cria o ChatRequest baseado no DTO final
        ChatDtos.ChatRequest creq = new ChatDtos.ChatRequest(
                userEmail,
                req.getMessage(),
                req.getHistory(),
                null // profile não obrigatório
        );

        // chama a IA
        ChatDtos.ChatResponse aiResp = aiChatClient.chat(creq);

        // retorna resposta final para o Android
        return new DiaryDTOs.DiaryRes(aiResp.botReply());
    }
}
