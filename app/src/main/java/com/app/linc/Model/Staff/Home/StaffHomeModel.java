
package com.app.linc.Model.Staff.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffHomeModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("information")
    @Expose
    private InformationStaffModel information;

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

    public InformationStaffModel getInformation() {
        return information;
    }

    public void setInformation(InformationStaffModel information) {
        this.information = information;
    }

}
