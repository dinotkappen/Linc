
package com.app.linc.Model.StudentHomeModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("student")
    @Expose
    private StudentModel student;
    @SerializedName("calander")
    @Expose
    private List<SchoolCalander> schoolCalander = null;
    @SerializedName("attandance")
    @Expose
    private Attandance attandance;
    @SerializedName("noticeboard")
    @Expose
    private List<Noticeboard> noticeboard = null;
    @SerializedName("school_information")
    @Expose
    private List<SchoolInformation> schoolInformation = null;

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public List<SchoolCalander> getSchoolCalander() {
        return schoolCalander;
    }

    public void setSchoolCalander(List<SchoolCalander> schoolCalander) {
        this.schoolCalander = schoolCalander;
    }

    public Attandance getAttandance() {
        return attandance;
    }

    public void setAttandance(Attandance attandance) {
        this.attandance = attandance;
    }

    public List<Noticeboard> getNoticeboard() {
        return noticeboard;
    }

    public void setNoticeboard(List<Noticeboard> noticeboard) {
        this.noticeboard = noticeboard;
    }

    public List<SchoolInformation> getSchoolInformation() {
        return schoolInformation;
    }

    public void setSchoolInformation(List<SchoolInformation> schoolInformation) {
        this.schoolInformation = schoolInformation;
    }

}
