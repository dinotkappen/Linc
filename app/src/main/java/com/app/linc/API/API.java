package com.app.linc.API;




import com.app.linc.Model.Attendance.AttendanceMain;
import com.app.linc.Model.Chat.AddGroupChat;
import com.app.linc.Model.Chat.AllParents.AllParentsModel;
import com.app.linc.Model.Chat.AllStudents.AllStudentChatModel;
import com.app.linc.Model.Chat.ChatGroups.ListChatGroupsModel;
import com.app.linc.Model.Chat.ChatHistory.ChatHistoryModel;
import com.app.linc.Model.Chat.CreateGroup.CreateGroupModel;
import com.app.linc.Model.DeviceRegistration.DeviceRegModel;
import com.app.linc.Model.LogInModel.Student.StudentLogInModel;
import com.app.linc.Model.Other.ProfileUpdateModel;
import com.app.linc.Model.Parent.Attendance.ParentAttendance;
import com.app.linc.Model.Parent.Home.HomeParentModel;
import com.app.linc.Model.Parent.LogIn.LogInParentModel;
import com.app.linc.Model.PrivacyPolicy.PrivacyPolicyModel;
import com.app.linc.Model.SchoolCodeValidatorModel.SchoolCodeValidatorModel;
import com.app.linc.Model.Staff.Home.StaffHomeModel;
import com.app.linc.Model.Staff.LogIn.LogInModelStaff;
import com.app.linc.Model.StudentHomeModel.StudentHome;


import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface API {


    //SchoolStaffModel code validator

    @FormUrlEncoded
    @POST("school/validate")
    Call<SchoolCodeValidatorModel> schoolCodeValidator(@Field("school_code") String school_code);


    //StudentModel LogIn
    @FormUrlEncoded
    @POST("student/login")
    Call<StudentLogInModel> Login(@Field("email") String email, @Field("password") String password,@Field("school_code") String school_code);


    //Staff LogIn
    @FormUrlEncoded
    @POST("staff/login")
    Call<LogInModelStaff> LoginStaff(@Field("email") String email, @Field("password") String password, @Field("school_code") String school_code);


    //Parent LogIn
    @FormUrlEncoded
    @POST("parent/login")
    Call<LogInParentModel> logInParent(@Field("email") String email, @Field("password") String password, @Field("school_code") String school_code);

    //Staff Home

    @GET("staff/home")
    Call<StaffHomeModel> getSaffHome(@Header("userToken")String userToken);


    @GET("student/fullinfo")
    Call<StudentHome> getStudentHome(@Header("userToken")String userToken);


    @GET("parent/home")
    Call<HomeParentModel> getParentHome(@Header("userToken")String userToken);

    //AttendanceMain
//    @GET("student/attendance?")
//    Call<AttendanceMain> getAttendance(@Query("student_id")int student_id, @Header("userToken")String userToken);

    @GET("student/attendance?")
    Call<AttendanceMain> getAttendanceStudent(@QueryMap Map<String, String> params,@Header("userToken")String userToken);

    @GET("parent/attendance?")
    Call<ParentAttendance> getAttendanceParent(@QueryMap Map<String, String> params, @Header("userToken")String userToken);

    //Privacy policy
    @GET("privacy_statement")
    Call<PrivacyPolicyModel> getPrivacyPolicy();


    //Device Registration Student
    @FormUrlEncoded
    @POST("student/register-device")
    Call<DeviceRegModel> studentDeviceRegister(@Field("device_id") String device_id, @Field("device_type") String device_type, @Field("device_os") String device_os, @Header("userToken")String userToken);

    //Device Registration Parent
    @FormUrlEncoded
    @POST("parent/register-device")
    Call<DeviceRegModel> parentDeviceRegister(@Field("device_id") String device_id, @Field("device_type") String device_type, @Field("device_os") String device_os, @Header("userToken")String userToken);


    //Device Registration Staff
    @FormUrlEncoded
    @POST("staff/register-device")
    Call<DeviceRegModel> stffDeviceRegister(@Field("device_id") String device_id, @Field("device_type") String device_type, @Field("device_os") String device_os, @Header("userToken")String userToken);


    //Profile Update Student
    @FormUrlEncoded
    @POST("student/profile/update")
    Call<ProfileUpdateModel> studentProfileUpdate(@Field("name") String name, @Field("phone") String phone, @Header("userToken")String userToken);


    //Profile Update Parent
    @FormUrlEncoded
    @POST("parent/profile/update")
    Call<ProfileUpdateModel> parentProfileUpdate(@Field("name") String name, @Field("phone") String phone, @Header("userToken")String userToken);

    //Profile Update Staff
    @FormUrlEncoded
    @POST("staff/profile/update")
    Call<ProfileUpdateModel> staffProfileUpdate(@Field("name") String name, @Field("phone") String phone, @Header("userToken")String userToken);


    //List ChatHistory GroupListChat STAFF
    @GET("staff/chat-groups")
    Call<ListChatGroupsModel> getChatGroupsStaff(@Header("userToken")String userToken);

    //List ChatHistory GroupListChat STUDENT
    @GET("student/chat-groups")
    Call<ListChatGroupsModel> getChatGroupsStudent(@Header("userToken")String userToken);

    //List ChatHistory GroupListChat PARENT
    @GET("parent/chat-groups")
    Call<ListChatGroupsModel> getChatGroupsParent(@Header("userToken")String userToken);

    //Create ChatHistory GroupListChat
    @FormUrlEncoded
    @POST("staff/create/group")
    Call<CreateGroupModel> createChatGroup(@Field("group_name") String group_name, @Header("userToken")String userToken);

    //All Parents
    @GET("staff/all-parents")
    Call<AllParentsModel> getAllParents(@Header("userToken")String userToken);

    //All Students
    @GET("staff/all-students")
    Call<AllStudentChatModel> getAllStudents(@Header("userToken")String userToken);

    //Add Members To GroupListChat
    @FormUrlEncoded
    @POST("staff/add/group-members/{groupID}")
    Call<AddGroupChat> addChatGroupMembers(@Path("groupID") String groupID, @Field("group_member[]") ArrayList<String> groupMembers, @Header("userToken")String userToken);

    //ChatHistory History STAFF
    @GET("staff/chathistory/{groupID}")
    Call<ChatHistoryModel> getChatHistoryStaff(@Path("groupID") String groupID,@Header("userToken")String userToken);

    //ChatHistory History STUDENT
    @GET("student/chathistory/{groupID}")
    Call<ChatHistoryModel> getChatHistoryStudent(@Path("groupID") String groupID,@Header("userToken")String userToken);

    //ChatHistory History PARENT
    @GET("parent/chathistory/{groupID}")
    Call<ChatHistoryModel> getChatHistoryParent(@Path("groupID") String groupID,@Header("userToken")String userToken);

    //Send Message STAFF
    @FormUrlEncoded
    @POST("staff/chat/send/{groupID}")
    Call<AddGroupChat> sendMessageStaff(@Path("groupID") String groupID,@Field("message") String message,@Header("userToken")String userToken);

    //Send Message STUDENT
    @FormUrlEncoded
    @POST("student/chat/send/{groupID}")
    Call<AddGroupChat> sendMessageStudent(@Path("groupID") String groupID,@Field("message") String message,@Header("userToken")String userToken);


    //Send Message PARENT
    @FormUrlEncoded
    @POST("parent/chat/send/{groupID}")
    Call<AddGroupChat> sendMessageParent(@Path("groupID") String groupID,@Field("message") String message,@Header("userToken")String userToken);

    //Close Group
    @GET("staff/group/close/{groupID}")
    Call<AddGroupChat> closeChatGroup(@Path("groupID") String groupID,@Header("userToken")String userToken);


}
