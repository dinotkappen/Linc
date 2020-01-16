
package com.app.linc.Model.Chat.ChatGroups;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListChatGroupsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("group")
    @Expose
    private List<GroupListChat> group = null;

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

    public List<GroupListChat> getGroup() {
        return group;
    }

    public void setGroup(List<GroupListChat> group) {
        this.group = group;
    }

}
