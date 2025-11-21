package com.auramind.api.diary;

import java.util.List;
import com.auramind.api.ai.dto.ChatDtos;

public class DiaryDTOs {

    // ----------------------
    // REQUEST
    // ----------------------
    public static class DiaryReq {
        private String message;
        private List<ChatDtos.Msg> history;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public List<ChatDtos.Msg> getHistory() { return history; }
        public void setHistory(List<ChatDtos.Msg> history) { this.history = history; }
    }

    // ----------------------
    // RESPONSE
    // ----------------------
    public static class DiaryRes {
    private String aiReply;

    public DiaryRes(String aiReply) {
        this.aiReply = aiReply;
    }

    public String getAiReply() { return aiReply; }
    public void setAiReply(String aiReply) { this.aiReply = aiReply; }
}

}
