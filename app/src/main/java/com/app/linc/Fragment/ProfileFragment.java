package com.app.linc.Fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Model.Attendance.AttendanceMain;
import com.app.linc.Model.Attendance.InformationAttandance;
import com.app.linc.Model.LogInModel.Student.StudentLogInModel;
import com.app.linc.Model.LogInModel.Student.StudentUser;
import com.app.linc.Model.Other.ProfileUpdateModel;
import com.app.linc.Model.Parent.Attendance.InformationParentAttendance;
import com.app.linc.Model.Parent.Attendance.ParentAttendance;
import com.app.linc.Model.Staff.Home.ClassModelStaff;
import com.app.linc.Model.Staff.Home.StudentStaffModel;
import com.app.linc.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ImageView imgProfile;
    PieChartView pieChartView;
    TextView txtProfileName, txtProfileClass, txtProfileSection;
    TextView txtAbsent, txtPresent;
    int total_working_day, attendancePresents, attendanceAbsemts;
    String studentName, studentClass, studentSection, imgStudentUrl, userType;
    List<StudentStaffModel> studentStaffModelList = new ArrayList<>();
    ClassModelStaff classModelStaff = new ClassModelStaff();
    int selectedProfileId;
    API apiInterface;
    String userToken;
    String user_id;
    InformationAttandance informationAttandance = new InformationAttandance();
    List<InformationParentAttendance>informationParentAttendance = new ArrayList<>();
    private AVLoadingIndicatorView aviProfileLoader;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout linearCard;

    StudentLogInModel studentLogInModel = new StudentLogInModel();
    StudentUser studentUser=new StudentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Hawk.put("currentFragment",false);
        apiInterface = APIClient.getClient().create(API.class);

        txtPresent = (TextView) rootView.findViewById(R.id.txtPresent);
        txtAbsent = (TextView) rootView.findViewById(R.id.txtAbsent);
        txtProfileName = (TextView) rootView.findViewById(R.id.txtProfileName);
        txtProfileClass = (TextView) rootView.findViewById(R.id.txtProfileClass);
        txtProfileSection = (TextView) rootView.findViewById(R.id.txtProfileSection);
        imgProfile = (ImageView) rootView.findViewById(R.id.imgProfile);
        aviProfileLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviProfileLoader.hide();
        titlHide(getResources().getString(R.string.PROFILE));
        linearCard= rootView.findViewById(R.id.linearCard);
        pieChartView = rootView.findViewById(R.id.chart);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(userType.equals("staff"))
                {

                }
                else if(userType.equals("parent"))
                {
                    getAttendanceParent(selectedProfileId);

                    Log.v("userToken",userToken);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        userType = Hawk.get("userType", userType);
        userToken = Hawk.get("userToken", userToken);
        user_id=Hawk.get("user_id",user_id);
        if (userType.equals("student")) {

            studentLogInModel=Hawk.get("studentLogInModel",studentLogInModel);
            studentUser=studentLogInModel.getUser();
            studentName = studentUser.getName();

            studentClass = Hawk.get("studentClass", studentClass);
            studentSection = Hawk.get("studentSection", studentSection);
            imgStudentUrl = Hawk.get("imgStudentUrl", imgStudentUrl);
            total_working_day = Hawk.get("attendanceTotalWorkingDay", total_working_day);
            attendancePresents = Hawk.get("attendancePresents", attendancePresents);
            attendanceAbsemts = Hawk.get("attendanceAbsemts", attendanceAbsemts);
            getAttendanceStudent();
        } else {

            selectedProfileId = Hawk.get("selectedProfileId", selectedProfileId);
            studentStaffModelList = Hawk.get("studentStaffModelList", studentStaffModelList);
            if (studentStaffModelList.size() > 0) {
                for (int i = 0; i < studentStaffModelList.size(); i++) {
                    int id = studentStaffModelList.get(i).getId();
                    if (id == selectedProfileId) {
                        studentName = studentStaffModelList.get(i).getName();
                                        classModelStaff = studentStaffModelList.get(i).getClassModel();
                        studentClass = classModelStaff.getName();
                        studentSection = classModelStaff.getSection();
                        imgStudentUrl = getString(R.string.imgBasrUrl) + imgStudentUrl;

                    }
                }

            }
            if(userType.equals("staff"))
            {

            }
            else if(userType.equals("parent"))
            {
                getAttendanceParent(selectedProfileId);
                Log.v("userToken",userToken);

            }
        }


        if (studentName != null && !studentName.isEmpty() && !studentName.equals("null")) {
            txtProfileName.setText(studentName);
        }
        if (studentClass != null && !studentClass.isEmpty() && !studentClass.equals("null")) {
            txtProfileClass.setText(getString(R.string.YearLevel) + studentClass);
        } else {
            txtProfileClass.setText(getString(R.string.YearLevel));
        }
        if (studentSection != null && !studentSection.isEmpty() && !studentSection.equals("null")) {
            txtProfileSection.setText(getString(R.string.Section) + studentSection);
            if (imgStudentUrl != null && !imgStudentUrl.isEmpty() && !imgStudentUrl.equals("null")) {
            } else {
                txtProfileSection.setText(getString(R.string.Section));
            }
            Glide
                    .with(getActivity())
                    .load(imgStudentUrl)
                    .centerCrop()
                    .placeholder(R.drawable.man)
                    .into(imgProfile);
        }




        linearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals("student"))
                {
                    UserProfileFragment userProfileFragment=new UserProfileFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, userProfileFragment);
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        return rootView;
    }


    public void getAttendanceStudent() {
        aviProfileLoader.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("student_id",user_id);
        Call<AttendanceMain> call = apiInterface.getAttendanceStudent(params, userToken);
        call.enqueue(new Callback<AttendanceMain>() {
            @Override
            public void onResponse(Call<AttendanceMain> call, Response<AttendanceMain> response) {
                try {
                    AttendanceMain studentHomeModelObj = new AttendanceMain();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    studentHomeModelObj = gson.fromJson(jsonElement, AttendanceMain.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = studentHomeModelObj.getStatus();

                    if (status.equals("200")) {
                        int success = studentHomeModelObj.getSuccess();
                        if (success == 1) {
                            informationAttandance = studentHomeModelObj.getInformation();
                            attendancePresents = informationAttandance.getPresents();
                            attendanceAbsemts = informationAttandance.getAbsents();
                            total_working_day = informationAttandance.getTotalWorkingDay();


                            txtPresent.setText("" + attendancePresents + " " + getString(R.string.PresentDays));
                            txtAbsent.setText("" + attendanceAbsemts + " " + getString(R.string.AbsentDays));
                            //   pieData.add(new SliceValue(20, getResources().getColor(R.color.colorOrange)).setLabel(""));

                            List pieData = new ArrayList<>();
                            pieData.add(new SliceValue(attendancePresents, getResources().getColor(R.color.colorDarkGreen)).setLabel(""));
                            pieData.add(new SliceValue(attendanceAbsemts, getResources().getColor(R.color.colorRed)).setLabel(""));
                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                            pieChartData.setHasCenterCircle(true).setCenterText1("" + total_working_day).setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartData.setHasCenterCircle(true).setCenterText2("Days Total").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartView.setPieChartData(pieChartData);

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
            public void onFailure(Call<AttendanceMain> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviProfileLoader.hide();

            }
        });
    }

    public void getAttendanceParent(final int selectedProfileId) {
        aviProfileLoader.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("student_id",""+selectedProfileId);
        Call<ParentAttendance> call = apiInterface.getAttendanceParent(params, userToken);
        call.enqueue(new Callback<ParentAttendance>() {
            @Override
            public void onResponse(Call<ParentAttendance> call, Response<ParentAttendance> response) {
                try {
                    ParentAttendance parentAttendance = new ParentAttendance();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    parentAttendance = gson.fromJson(jsonElement, ParentAttendance.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = parentAttendance.getStatus();

                    if (status.equals("200")) {
                        int success = parentAttendance.getSuccess();
                        if (success == 1) {
                            informationParentAttendance =parentAttendance.getInformation();
                            for(int i=0;i<informationParentAttendance.size();i++)
                            {
                                int id=informationParentAttendance.get(i).getStudentId();
                                if(id==selectedProfileId)
                                {
                                    attendancePresents = informationParentAttendance.get(i).getPresents();
                                    attendanceAbsemts = informationParentAttendance.get(i).getAbsents();
                                    total_working_day = informationParentAttendance.get(i).getTotalWorkingDay();
                                }
                            }



                            txtPresent.setText("" + attendancePresents + " " + getString(R.string.PresentDays));
                            txtAbsent.setText("" + attendanceAbsemts + " " + getString(R.string.AbsentDays));
                            //   pieData.add(new SliceValue(20, getResources().getColor(R.color.colorOrange)).setLabel(""));

                            List pieData = new ArrayList<>();
                            pieData.add(new SliceValue(attendancePresents, getResources().getColor(R.color.colorDarkGreen)).setLabel(""));
                            pieData.add(new SliceValue(attendanceAbsemts, getResources().getColor(R.color.colorRed)).setLabel(""));
                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                            pieChartData.setHasCenterCircle(true).setCenterText1("" + total_working_day).setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartData.setHasCenterCircle(true).setCenterText2("Days Total").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartView.setPieChartData(pieChartData);

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
            public void onFailure(Call<ParentAttendance> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviProfileLoader.hide();

            }
        });
    }




    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
