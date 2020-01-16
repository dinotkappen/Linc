
package com.app.linc.Model.Chat.AllStudents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllStudentChatModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("information")
    @Expose
    private InformationStudentChat information;

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

    public InformationStudentChat getInformation() {
        return information;
    }

    public void setInformation(InformationStudentChat information) {
        this.information = information;
    }

}
