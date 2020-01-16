
package com.app.linc.Model.Staff.Home;

import java.util.List;

import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.Model.StudentHomeModel.SchoolInformation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationStaffModel {

    @SerializedName("notifications")
    @Expose
    private List<Noticeboard> notifications = null;
    @SerializedName("school")
    @Expose
    private List<SchoolInformation> school = null;
    @SerializedName("student")
    @Expose
    private List<StudentStaffModel> student = null;
    @SerializedName("calander")
    @Expose
    private List<SchoolCalander> calander = null;

    public List<Noticeboard> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Noticeboard> notifications) {
        this.notifications = notifications;
    }

    public List<SchoolInformation> getSchool() {
        return school;
    }

    public void setSchool(List<SchoolInformation> school) {
        this.school = school;
    }

    public List<StudentStaffModel> getStudent() {
        return student;
    }

    public void setStudent(List<StudentStaffModel> student) {
        this.student = student;
    }

    public List<SchoolCalander> getCalander() {
        return calander;
    }

    public void setCalander(List<SchoolCalander> calander) {
        this.calander = calander;
    }

}
