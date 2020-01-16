
package com.app.linc.Model.Parent.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeParentModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("information")
    @Expose
    private InformationParent information;

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

    public InformationParent getInformation() {
        return information;
    }

    public void setInformation(InformationParent information) {
        this.information = information;
    }

}
