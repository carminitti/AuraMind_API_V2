package com.auramind.api.diary;

import lombok.Data;
import java.util.List;

public class DiaryDTOs {

    @Data
    public static class DiaryReq {
        private String message;        // Texto digitado pelo usuário no diário
        private List<String> history;  // Histórico anterior das mensagens
    }

    @Data
    public static class DiaryRes {
        private String aiReply;

        public DiaryRes() {}

        public DiaryRes(String aiReply) {
            this.aiReply = aiReply;
        }
    }
}
