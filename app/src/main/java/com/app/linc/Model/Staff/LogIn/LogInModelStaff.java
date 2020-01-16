
package com.app.linc.Model.Staff.LogIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInModelStaff {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user")
    @Expose
    private UserStaff user;

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

    public UserStaff getUser() {
        return user;
    }

    public void setUser(UserStaff user) {
        this.user = user;
    }

}
