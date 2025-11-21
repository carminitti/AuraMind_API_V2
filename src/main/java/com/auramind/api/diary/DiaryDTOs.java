package com.auramind.api.diary;

import com.auramind.api.ai.dto.ChatDtos;
import java.util.List;
import lombok.Data;

public class DiaryDTOs {

    @Data
    public static class DiaryReq {
        private String message;
        private List<ChatDtos.Msg> history;
    }

    @Data
    public static class DiaryRes {
        private String aiReply;

        public DiaryRes(String aiReply) {
            this.aiReply = aiReply;
        }
    }
}
