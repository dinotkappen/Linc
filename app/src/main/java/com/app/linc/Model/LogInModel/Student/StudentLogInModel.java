
package com.app.linc.Model.LogInModel.Student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentLogInModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user")
    @Expose
    private StudentUser user;

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

    public StudentUser getUser() {
        return user;
    }

    public void setUser(StudentUser user) {
        this.user = user;
    }

}
