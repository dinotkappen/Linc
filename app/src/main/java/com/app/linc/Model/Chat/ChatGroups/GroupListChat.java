
package com.app.linc.Model.Chat.ChatGroups;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupListChat {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("close")
    @Expose
    private Integer close;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
