
package com.app.linc.Model.Chat.ChatHistory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatHistoryModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("chat")
    @Expose
    private List<ChatHistory> chat = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<ChatHistory> getChat() {
        return chat;
    }

    public void setChat(List<ChatHistory> chat) {
        this.chat = chat;
    }

}
