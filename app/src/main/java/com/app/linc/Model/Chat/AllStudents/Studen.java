
package com.app.linc.Model.Chat.AllStudents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Studen {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("id_number")
    @Expose
    private String idNumber;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("rest_password_time")
    @Expose
    private Object restPasswordTime;
    @SerializedName("reset_password_token")
    @Expose
    private Object resetPasswordToken;
    @SerializedName("login_token")
    @Expose
    private Object loginToken;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("parents_id")
    @Expose
    private Integer parentsId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getRestPasswordTime() {
        return restPasswordTime;
    }

    public void setRestPasswordTime(Object restPasswordTime) {
        this.restPasswordTime = restPasswordTime;
    }

    public Object getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(Object resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Object getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(Object loginToken) {
        this.loginToken = loginToken;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getParentsId() {
        return parentsId;
    }

    public void setParentsId(Integer parentsId) {
        this.parentsId = parentsId;
    }

}
