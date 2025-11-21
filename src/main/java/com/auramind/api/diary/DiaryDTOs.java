package com.auramind.api.diary;

import lombok.Data;

public class DiaryDTOs {

    // ---------- REQUEST ----------
    @Data
    public static class DiaryReq {
        private String userId;
        private String message;
    }

    // ---------- RESPONSE ----------
    @Data
    public static class DiaryRes {
        private String aiReply;

        public DiaryRes() {}

        public DiaryRes(String aiReply) {
            this.aiReply = aiReply;
        }
    }
}
