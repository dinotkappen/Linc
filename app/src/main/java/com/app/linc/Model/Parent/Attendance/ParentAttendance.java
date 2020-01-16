
package com.app.linc.Model.Parent.Attendance;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentAttendance {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("information")
    @Expose
    private List<InformationParentAttendance> information = null;

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

    public List<InformationParentAttendance> getInformation() {
        return information;
    }

    public void setInformation(List<InformationParentAttendance> information) {
        this.information = information;
    }

}
