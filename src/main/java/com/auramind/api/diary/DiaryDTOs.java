package com.auramind.api.diary;

import com.auramind.api.ai.dto.ChatDtos;
import java.util.List;
import lombok.Data;

public class DiaryDTOs {

   public static class DiaryRes {
    private String aiReply;
    public DiaryRes() {}
    public DiaryRes(String aiReply) { this.aiReply = aiReply; }
    public String getAiReply() { return aiReply; }
    public void setAiReply(String aiReply) { this.aiReply = aiReply; }
}

}
