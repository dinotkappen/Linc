
package com.app.linc.Model.Chat.ChatHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatHistory {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("chat")
    @Expose
    private Chat_ chat;
    @SerializedName("profile-img")
    @Expose
    private String profileImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Chat_ getChat() {
        return chat;
    }

    public void setChat(Chat_ chat) {
        this.chat = chat;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

}
