
package com.app.linc.Model.Parent.LogIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInParentModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user")
    @Expose
    private UserParent user;

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

    public UserParent getUser() {
        return user;
    }

    public void setUser(UserParent user) {
        this.user = user;
    }

}
