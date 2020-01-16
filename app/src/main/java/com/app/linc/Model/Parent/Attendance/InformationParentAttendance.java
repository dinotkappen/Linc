
package com.app.linc.Model.Parent.Attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationParentAttendance {

    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("total_working_day")
    @Expose
    private Integer totalWorkingDay;
    @SerializedName("presents")
    @Expose
    private Integer presents;
    @SerializedName("absents")
    @Expose
    private Integer absents;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTotalWorkingDay() {
        return totalWorkingDay;
    }

    public void setTotalWorkingDay(Integer totalWorkingDay) {
        this.totalWorkingDay = totalWorkingDay;
    }

    public Integer getPresents() {
        return presents;
    }

    public void setPresents(Integer presents) {
        this.presents = presents;
    }

    public Integer getAbsents() {
        return absents;
    }

    public void setAbsents(Integer absents) {
        this.absents = absents;
    }

}
