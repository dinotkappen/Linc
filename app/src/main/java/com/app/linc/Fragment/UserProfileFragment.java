package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Model.LogInModel.Student.StudentLogInModel;
import com.app.linc.Model.LogInModel.Student.StudentUser;
import com.app.linc.Model.Other.ProfileUpdateModel;
import com.app.linc.Model.Parent.LogIn.LogInParentModel;
import com.app.linc.Model.Parent.LogIn.UserParent;
import com.app.linc.Model.Staff.LogIn.LogInModelStaff;
import com.app.linc.Model.Staff.LogIn.UserStaff;
import com.app.linc.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    ImageView profile_image;
    EditText edtName, edtPhone, edtEmail;
    CardView btnCardLogIn;
    String userType;
    LogInModelStaff logInModelStaff = new LogInModelStaff();
    LogInParentModel logInParentModel = new LogInParentModel();
    StudentLogInModel studentLogInModel = new StudentLogInModel();

    UserStaff userStaffModel = new UserStaff();
    UserParent userParentModel=new UserParent();
    StudentUser studentUser=new StudentUser();
    String name, phone, email;
    private AVLoadingIndicatorView aviProfileLoader;
    API apiInterface;
    String userToken;
    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Hawk.put("currentFragment",false);
        apiInterface = APIClient.getClient().create(API.class);

        profile_image = rootView.findViewById(R.id.profile_image);
        edtName = rootView.findViewById(R.id.edtName);
        edtPhone = rootView.findViewById(R.id.edtPhone);
        edtEmail = rootView.findViewById(R.id.edtEmail);
        btnCardLogIn = rootView.findViewById(R.id.btnCardLogIn);
        aviProfileLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviProfileLoader.hide();


        userType = Hawk.get("userType", userType);
        userToken = Hawk.get("userToken", userToken);

        if (userType.equals("staff")) {
            logInModelStaff = Hawk.get("logInModelStaff", logInModelStaff);
            userStaffModel = logInModelStaff.getUser();
            name = userStaffModel.getName();
            phone = userStaffModel.getPhone();
            email = userStaffModel.getEmail();

        } else if (userType.equals("parent")) {
            studentLogInModel=Hawk.get("studentLogInModel",studentLogInModel);
            userParentModel=logInParentModel.getUser();
            name = userParentModel.getName();
            phone = userParentModel.getPhone();
            email = userParentModel.getEmail();

        }
        else if (userType.equals("student"))
        {
            studentLogInModel=Hawk.get("studentLogInModel",studentLogInModel);
            studentUser=studentLogInModel.getUser();
            name = studentUser.getName();
            phone = studentUser.getPhone();
            email = studentUser.getEmail();
        }
        if (name != null && !name.isEmpty() && !name.equals("null")) {

            edtName.setText(name);
        }
        if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
            edtPhone.setText(phone);
        }

        if (email != null && !email.isEmpty() && !email.equals("null")) {
            edtEmail.setText(email);
        }

        btnCardLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentProfile();
            }
        });

        return rootView;
    }



    public void updateStudentProfile() {
        aviProfileLoader.show();
        Call<ProfileUpdateModel> call=null;
        name=edtName.getText().toString();
        phone=edtPhone.getText().toString();
        if (userType.equals("student"))
        {
            call = apiInterface.studentProfileUpdate(name,phone, userToken);
        }
        else if( userType.equals("parent"))
        {
            call = apiInterface.parentProfileUpdate(name,phone, userToken);
        }
        else if(userType.equals("staff"))
        {
            call = apiInterface.staffProfileUpdate(name,phone, userToken);
        }

        call.enqueue(new Callback<ProfileUpdateModel>() {
            @Override
            public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {
                try {
                    ProfileUpdateModel profileUpdateModelObj = new ProfileUpdateModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    profileUpdateModelObj = gson.fromJson(jsonElement, ProfileUpdateModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = profileUpdateModelObj.getStatus();

                    if (status.equals("200")) {
                        int success = profileUpdateModelObj.getSuccess();
                        String msg  = profileUpdateModelObj.getMsg();
                        if (success == 1) {

                            if(userType.equals("parent"))
                            {
                                userParentModel.setName(name);
                                userParentModel.setPhone(phone);
                                logInParentModel.setUser(userParentModel);
                                Hawk.put("logInParentModel",logInParentModel);
                            }
                            else if(userType.equals("staff"))
                            {
                               userStaffModel.setName(name);
                               userStaffModel.setPhone(phone);
                               logInModelStaff.setUser(userStaffModel);
                                Hawk.put("logInModelStaff",logInModelStaff);
                            }
                            else if(userType.equals("student"))
                            {
                                studentUser.setName(name);
                                studentUser.setPhone(phone);
                                studentLogInModel.setUser(studentUser);
                                Hawk.put("studentLogInModel",studentLogInModel);

                            }

                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        // logOut();

                    }
                    aviProfileLoader.hide();
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());

                    aviProfileLoader.hide();
                  }


            }

            @Override
            public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviProfileLoader.hide();

            }
        });
    }
}
