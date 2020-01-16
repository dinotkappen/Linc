
package com.app.linc.Model.Parent.Home;

import java.util.List;

import com.app.linc.Model.Staff.Home.StudentStaffModel;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.Model.StudentHomeModel.SchoolInformation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationParent {

    @SerializedName("students")
    @Expose
    private List<StudentStaffModel> students = null;
    @SerializedName("notice")
    @Expose
    private List<Noticeboard> notice = null;
    @SerializedName("calander")
    @Expose
    private List<SchoolCalander> calander = null;
    @SerializedName("school_information")
    @Expose
    private List<SchoolInformation> schoolInformation = null;

    public List<StudentStaffModel> getStudents() {
        return students;
    }

    public void setStudents(List<StudentStaffModel> students) {
        this.students = students;
    }

    public List<Noticeboard> getNotice() {
        return notice;
    }

    public void setNotice(List<Noticeboard> notice) {
        this.notice = notice;
    }

    public List<SchoolCalander> getCalander() {
        return calander;
    }

    public void setCalander(List<SchoolCalander> calander) {
        this.calander = calander;
    }

    public List<SchoolInformation> getSchoolInformation() {
        return schoolInformation;
    }

    public void setSchoolInformation(List<SchoolInformation> schoolInformation) {
        this.schoolInformation = schoolInformation;
    }

}
