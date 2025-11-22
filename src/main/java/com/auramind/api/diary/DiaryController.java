package com.auramind.api.diary;
import java.util.List;

import com.auramind.api.ai.AiChatClient;
import com.auramind.api.ai.dto.ChatDtos;
import com.auramind.api.repository.UserRepository;
import com.auramind.api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public DiaryDTOs.DiaryRes message(@RequestBody DiaryDTOs.DiaryReq req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email;
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            email = ud.getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }

        User u = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Long userId = u.getId();

        List<ChatDtos.Msg> convertedHistory = null;
if (req.getHistory() != null) {
    convertedHistory = req.getHistory().stream()
            .map(msg -> new ChatDtos.Msg("user", msg))
            .toList();
}

var chatReq = new ChatDtos.ChatRequest(
        String.valueOf(userId),
        req.getMessage(),
        convertedHistory,
        ""
);
        var aiResp = ai.chat(chatReq);
        return new DiaryDTOs.DiaryRes(aiResp.botReply());
    }
}
