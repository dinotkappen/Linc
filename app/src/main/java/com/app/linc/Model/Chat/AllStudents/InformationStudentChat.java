
package com.app.linc.Model.Chat.AllStudents;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationStudentChat {

    @SerializedName("studens")
    @Expose
    private List<Studen> studens = null;

    public List<Studen> getStudens() {
        return studens;
    }

    public void setStudens(List<Studen> studens) {
        this.studens = studens;
    }

}
