package com.app.linc.Fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.linc.Adapter.NoticBoardHomeRecyclerViewAdapter;

import com.app.linc.Adapter.ProfileRecyclerviewAdapter;
import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.Parent.Home.HomeParentModel;
import com.app.linc.Model.Parent.Home.InformationParent;
import com.app.linc.Model.Parent.Home.SchoolInformationParent;
import com.app.linc.Model.Staff.Home.InformationStaffModel;
import com.app.linc.Model.Staff.Home.StaffHomeModel;
import com.app.linc.Model.Staff.Home.StudentStaffModel;
import com.app.linc.Model.StudentHomeModel.Attandance;
import com.app.linc.Model.StudentHomeModel.ClassModel;
import com.app.linc.Model.StudentHomeModel.Information;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.Model.StudentHomeModel.SchoolInformation;
import com.app.linc.Model.StudentHomeModel.StudentModel;
import com.app.linc.Model.StudentHomeModel.StudentHome;
import com.app.linc.R;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import static com.app.linc.Activities.MainActivity.logOut;
import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    String userType;
    API apiInterface;
    ImageView imgStudent;
    String school_code, userToken;
    PieChartView pieChartViewHome;
    ViewGroup.LayoutParams params;
    List pieData = new ArrayList<>();
    RecyclerView recyclerViewProfile;
    ClassModel classModelObj = new ClassModel();
    SchoolCalendarAdapter adapterSchoolCalendar;
    Attandance attandanceModel = new Attandance();
    StudentModel studentModelObj = new StudentModel();
    Information informationModelObj = new Information();
    List<Noticeboard> noticeboardList = new ArrayList<>();
    ProfileRecyclerviewAdapter profileRecyclerviewAdapter;
    private AVLoadingIndicatorView aviHomeFragmentLoader;
    List<Noticeboard> noticeParentList = new ArrayList<>();
    List<SchoolCalander> calanderStaffList = new ArrayList<>();
    List<SchoolCalander> calanderParentList = new ArrayList<>();
    List<Noticeboard> noticeStaffModelList = new ArrayList<>();
    List<SchoolCalander> schoolCalendarList = new ArrayList<>();
    NoticBoardHomeRecyclerViewAdapter adapterNoticBoardRecyclerView;
    List<SchoolInformation> schoolParentModelList = new ArrayList<>();
    List<SchoolInformation> schoolStaffModelList = new ArrayList<>();
    List<SchoolInformation> schoolInformationList = new ArrayList<>();
    List<StudentStaffModel> studentStaffModelList = new ArrayList<>();
    List<StudentStaffModel> studentParentModelList = new ArrayList<>();
    InformationStaffModel informationStaffModel = new InformationStaffModel();
    TextView txtStudentName, txtSchoolAdrz, txtSchoolName, txtClass, txtSection;
    LinearLayout linearSchoolCalendar, linearNoticeBoard, linearSchoolInformation;
    List<SchoolInformationParent> schoolInformationParentModelList = new ArrayList<>();
    LinearLayout linearAttendanceStudent, linearProfile, linearRecycleProfile, linearProfileCommon;
    private RecyclerView recyclerViewNotice, recyclerViewSchoolCalendar, recyclerViewSchoolInformation;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        apiInterface = APIClient.getClient().create(API.class);


        txtSchoolAdrz = rootView.findViewById(R.id.txtSchoolAdrz);
        linearProfile = rootView.findViewById(R.id.linearProfile);
        linearProfileCommon = rootView.findViewById(R.id.linearProfileCommon);
        params = linearProfileCommon.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 400;
        linearProfileCommon.setLayoutParams(params);
        linearRecycleProfile = rootView.findViewById(R.id.linearRecycleProfile);
        txtSchoolName = rootView.findViewById(R.id.txtSchoolName);
        pieChartViewHome = rootView.findViewById(R.id.pieChartViewHome);
        linearSchoolCalendar = rootView.findViewById(R.id.linearSchoolCalendar);
        linearNoticeBoard = rootView.findViewById(R.id.linearNoticeBoard);
        linearSchoolInformation = rootView.findViewById(R.id.linearSchoolInformation);
//        linearProfile2 = rootView.findViewById(R.id.linearProfile2);
        recyclerViewProfile = rootView.findViewById(R.id.recyclerViewProfile);
        txtClass = rootView.findViewById(R.id.txtClass);
        txtSection = rootView.findViewById(R.id.txtSection);
        imgStudent = rootView.findViewById(R.id.imgStudent);
        txtStudentName = rootView.findViewById(R.id.txtStudentName);
        aviHomeFragmentLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviHomeFragmentLoader.show();

        //  linearProfile2.setVisibility(View.GONE);

        linearAttendanceStudent = rootView.findViewById(R.id.linearAttendanceStudent);
        //      params = linearAttendanceStudent.getLayoutParams();
        userType = Hawk.get("userType", "");
        userToken = Hawk.get("userToken", userToken);
        school_code = "0005";
        Log.v("userType", userType);
        titlHide(getResources().getString(R.string.menu_home));
        Hawk.put("currentFragment",false);

        recyclerViewNotice = rootView.findViewById(R.id.recyclerViewNotice);
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewProfile = rootView.findViewById(R.id.recyclerViewProfile);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewSchoolCalendar = rootView.findViewById(R.id.recyclerViewSchoolCalendar);
        recyclerViewSchoolCalendar.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (userType.equals("staff")) {
            linearRecycleProfile.setVisibility(View.VISIBLE);
            linearProfile.setVisibility(View.GONE);
            //linearProfile2.setVisibility(View.VISIBLE);
            linearAttendanceStudent.setVisibility(View.GONE);
            getStaffHome();
        } else if (userType.equals("parent")) {
            linearRecycleProfile.setVisibility(View.VISIBLE);
            linearProfile.setVisibility(View.GONE);
            // linearProfile2.setVisibility(View.GONE);
            linearAttendanceStudent.setVisibility(View.GONE);
            getParentHome();
        } else if (userType.equals("student")) {
            linearRecycleProfile.setVisibility(View.GONE);
            linearProfile.setVisibility(View.VISIBLE);
            // linearProfile2.setVisibility(View.GONE);
            linearAttendanceStudent.setVisibility(View.VISIBLE);


            getStudentHome();
        }

        linearAttendanceStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();

            }
        });

        linearProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        linearSchoolCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SchoolCalendarFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });


        linearNoticeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NoticeBoardFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        linearSchoolInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SchoolInformationFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });
        return rootView;


    }


    public void getStudentHome() {
        aviHomeFragmentLoader.show();
        Call<StudentHome> call = apiInterface.getStudentHome(userToken);
        Log.v("url", "" + apiInterface.getStudentHome(userToken));
        call.enqueue(new Callback<StudentHome>() {
            @Override
            public void onResponse(Call<StudentHome> call, Response<StudentHome> response) {
                try {
                    StudentHome studentHomeModelObj = new StudentHome();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    studentHomeModelObj = gson.fromJson(jsonElement, StudentHome.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = studentHomeModelObj.getStatus();

                    if (status.equals("200")) {
                        int success = studentHomeModelObj.getSuccess();
                        if (success == 1) {
                            informationModelObj = studentHomeModelObj.getInformation();
                            studentModelObj = informationModelObj.getStudent();
                            noticeboardList = informationModelObj.getNoticeboard();
                            attandanceModel = informationModelObj.getAttandance();
                            schoolCalendarList = informationModelObj.getSchoolCalander();
                            schoolInformationList = informationModelObj.getSchoolInformation();
                            classModelObj = informationModelObj.getStudent().getClassModel();

                            // StudentModel

                            String name = studentModelObj.getName();
                            Log.v("name", name);
                            if (name != null && !name.isEmpty() && !name.equals("null")) {
                                txtStudentName.setText(name);
                                Hawk.put("studentName", name);
                            }
                            String studentClass = classModelObj.getName();
                            String section = classModelObj.getSection();
                            if (studentClass != null && !studentClass.isEmpty() && !studentClass.equals("null")) {
                                txtClass.setText(getString(R.string.YearLevel) + studentClass);
                                Hawk.put("studentClass", studentClass);
                            } else {
                                txtClass.setText(getString(R.string.YearLevel));
                            }
                            if (section != null && !section.isEmpty() && !section.equals("null")) {
                                txtSection.setText(getString(R.string.Section) + section);
                                Hawk.put("studentSection", section);
                            } else {
                                txtSection.setText(getString(R.string.Section));
                            }
                            String imgStudentUrl = getString(R.string.imgBasrUrl) + studentModelObj.getPhoto();
                            Log.v("imgStudentUrl", imgStudentUrl);
                            if (imgStudentUrl != null && !imgStudentUrl.isEmpty() && !imgStudentUrl.equals("null")) {
                                Glide
                                        .with(getActivity())
                                        .load(imgStudentUrl)
                                        .centerCrop()
                                        .placeholder(R.drawable.man)
                                        .into(imgStudent);
                                Hawk.put("imgStudentUrl", imgStudentUrl);
                            }

                            // AttendanceMain

                            int total_working_day = attandanceModel.getTotalWorkingDay();
                            int presents = attandanceModel.getPresents();
                            int absents = attandanceModel.getAbsents();
                            Hawk.put("attendanceTotalWorkingDay", total_working_day);
                            Hawk.put("attendancePresents", presents);
                            Hawk.put("attendanceAbsemts", absents);


                            pieData.add(new SliceValue(presents, getResources().getColor(R.color.colorDarkGreen)).setLabel(""));
                            pieData.add(new SliceValue(absents, getResources().getColor(R.color.colorRed)).setLabel(""));
                            //   pieData.add(new SliceValue(20, getResources().getColor(R.color.colorOrange)).setLabel(""));


                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartData.setHasLabels(true).setValueLabelTextSize(6);
                            pieChartData.setHasCenterCircle(true).setCenterText1("").setCenterText1FontSize(6).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartData.setHasCenterCircle(true).setCenterText2("").setCenterText1FontSize(4).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartViewHome.setPieChartData(pieChartData);


                            //NoticeBoard

                            if (noticeboardList.size() > 0) {
                                for (int i = 0; i < noticeboardList.size(); i++) {
//                                    String content = noticeboardList.get(i).getContent();
//                                    String noticeDate = noticeboardList.get(i).getDate();
//                                    Log.v("content", content);
//                                    Log.v("noticeDate", noticeDate);
                                    Hawk.put("noticeboardList", noticeboardList);
                                    adapterNoticBoardRecyclerView = new NoticBoardHomeRecyclerViewAdapter(getActivity(), noticeboardList);
                                    recyclerViewNotice.setAdapter(adapterNoticBoardRecyclerView);
                                }
                            }

                            //SchoolStaffModel Calendar
                            if (schoolCalendarList.size() > 0) {
                                for (int i = 0; i < schoolCalendarList.size(); i++) {
//                                    String content = schoolCalendarList.get(i).getContent();
//                                    String noticeDate = schoolCalendarList.get(i).getDate();
//                                    Log.v("content", content);
//                                    Log.v("noticeDate", noticeDate);
                                    adapterSchoolCalendar = new SchoolCalendarAdapter(getActivity(), schoolCalendarList);
                                    recyclerViewSchoolCalendar.setAdapter(adapterSchoolCalendar);
                                    Hawk.put("schoolCalendarList", schoolCalendarList);
                                }
                            }

                            //SchoolStaffModel InformationStaffModel

                            if (schoolInformationList.size() > 0) {
                                Hawk.put("schoolInformationList", schoolInformationList);
                                for (int i = 0; i < schoolInformationList.size(); i++) {
                                    String schoolName = schoolInformationList.get(i).getName();
                                    String schoolAdrz = schoolInformationList.get(i).getAddress();
                                    Log.v("schoolName", schoolName);
                                    Log.v("schoolAdrz", schoolAdrz);

                                    if (schoolName != null && !schoolName.isEmpty() && !schoolName.equals("null")) {
                                        txtSchoolName.setText(schoolName);

                                    }
                                    if (schoolAdrz != null && !schoolAdrz.isEmpty() && !schoolAdrz.equals("null")) {
                                        txtSchoolAdrz.setText(schoolAdrz);
                                    }


                                }
                            }


                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();

                    }
                    aviHomeFragmentLoader.hide();
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    logOut();
                    aviHomeFragmentLoader.hide();
                }


            }

            @Override
            public void onFailure(Call<StudentHome> call, Throwable t) {

                Log.e("error", t.getMessage());
                logOut();
                aviHomeFragmentLoader.hide();

            }
        });
    }

    public void getStaffHome() {
        aviHomeFragmentLoader.show();
        Call<StaffHomeModel> call = apiInterface.getSaffHome(userToken);
        Log.v("url", "" + apiInterface.getStudentHome(userToken));
        call.enqueue(new Callback<StaffHomeModel>() {
            @Override
            public void onResponse(Call<StaffHomeModel> call, Response<StaffHomeModel> response) {
                try {
                    StaffHomeModel staffHomeModel = new StaffHomeModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    staffHomeModel = gson.fromJson(jsonElement, StaffHomeModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = staffHomeModel.getStatus();

                    if (status.equals("200")) {
                        int success = staffHomeModel.getSuccess();
                        if (success == 1) {
                            informationStaffModel = staffHomeModel.getInformation();
                            studentStaffModelList = informationStaffModel.getStudent();
                            noticeStaffModelList = informationStaffModel.getNotifications();
                            calanderStaffList = informationStaffModel.getCalander();
                            schoolStaffModelList = informationStaffModel.getSchool();

                            // StudentModel
                            if (studentStaffModelList.size() > 0) {
                                if (studentStaffModelList.size() > 1) {
                                    params.height = 800;
                                    linearProfileCommon.setLayoutParams(params);
                                }
                                Hawk.put("studentStaffModelList", studentStaffModelList);
                                profileRecyclerviewAdapter = new ProfileRecyclerviewAdapter(getActivity(), studentStaffModelList);
                                recyclerViewProfile.setAdapter(profileRecyclerviewAdapter);


                            }


                            //NoticeBoard

                            if (noticeStaffModelList.size() > 0) {
                                for (int i = 0; i < noticeStaffModelList.size(); i++) {

                                    Hawk.put("noticeboardList", noticeStaffModelList);
                                    adapterNoticBoardRecyclerView = new NoticBoardHomeRecyclerViewAdapter(getActivity(), noticeStaffModelList);
                                    recyclerViewNotice.setAdapter(adapterNoticBoardRecyclerView);
                                }
                            }

                            //SchoolStaffModel Calendar
                            if (calanderStaffList.size() > 0) {
                                for (int i = 0; i < calanderStaffList.size(); i++) {
//                                    String content = schoolCalendarList.get(i).getContent();
//                                    String noticeDate = schoolCalendarList.get(i).getDate();
//                                    Log.v("content", content);
//                                    Log.v("noticeDate", noticeDate);
                                    adapterSchoolCalendar = new SchoolCalendarAdapter(getActivity(), calanderStaffList);
                                    recyclerViewSchoolCalendar.setAdapter(adapterSchoolCalendar);
                                    Hawk.put("schoolCalendarList", calanderStaffList);
                                }
                            }

                            //SchoolStaffModel InformationStaffModel

                            if (schoolStaffModelList.size() > 0) {
                                Hawk.put("schoolInformationList", schoolStaffModelList);
                                for (int i = 0; i < schoolStaffModelList.size(); i++) {
                                    String schoolName = schoolStaffModelList.get(i).getName();
                                    String schoolAdrz = schoolStaffModelList.get(i).getAddress();
                                    Log.v("schoolName", schoolName);
                                    Log.v("schoolAdrz", schoolAdrz);

                                    if (schoolName != null && !schoolName.isEmpty() && !schoolName.equals("null")) {
                                        txtSchoolName.setText(schoolName);

                                    }
                                    if (schoolAdrz != null && !schoolAdrz.isEmpty() && !schoolAdrz.equals("null")) {
                                        txtSchoolAdrz.setText(schoolAdrz);
                                    }


                                }
                            }


                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviHomeFragmentLoader.hide();

                    }
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    logOut();
                    aviHomeFragmentLoader.hide();
                }

                aviHomeFragmentLoader.hide();
            }

            @Override
            public void onFailure(Call<StaffHomeModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                logOut();
                aviHomeFragmentLoader.hide();


            }
        });
    }

    public void getParentHome() {
        aviHomeFragmentLoader.show();
        Call<HomeParentModel> call = apiInterface.getParentHome(userToken);
        Log.v("url", "" + apiInterface.getParentHome(userToken));
        call.enqueue(new Callback<HomeParentModel>() {
            @Override
            public void onResponse(Call<HomeParentModel> call, Response<HomeParentModel> response) {
                try {
                    HomeParentModel homeParentModel = new HomeParentModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    homeParentModel = gson.fromJson(jsonElement, HomeParentModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = homeParentModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = homeParentModel.getSuccess();
                            if (success == 1) {
                                InformationParent informationParent = new InformationParent();
                                informationParent = homeParentModel.getInformation();
                                studentParentModelList = informationParent.getStudents();
                                noticeParentList = informationParent.getNotice();
                                schoolParentModelList = informationParent.getSchoolInformation();

                                calanderParentList = informationParent.getCalander();
                                schoolParentModelList = informationParent.getSchoolInformation();

//                            // StudentModel
                                if (studentParentModelList.size() > 0) {

                                    if (studentParentModelList.size() > 1) {
                                        params.height = 800;
                                        linearProfileCommon.setLayoutParams(params);
                                    }
                                    Log.v("studentParentModelList", "" + studentParentModelList.size());
                                    Hawk.put("studentStaffModelList", studentParentModelList);
                                    profileRecyclerviewAdapter = new ProfileRecyclerviewAdapter(getActivity(), studentParentModelList);
                                    recyclerViewProfile.setAdapter(profileRecyclerviewAdapter);


                                }


                                //NoticeBoard

                                if (noticeParentList.size() > 0) {
                                    Hawk.put("noticeboardList", noticeParentList);
                                    adapterNoticBoardRecyclerView = new NoticBoardHomeRecyclerViewAdapter(getActivity(), noticeParentList);
                                    recyclerViewNotice.setAdapter(adapterNoticBoardRecyclerView);

                                }
//
//
//
//                            //SchoolStaffModel InformationStaffModel
//
                                if (schoolParentModelList.size() > 0) {
                                    Hawk.put("schoolInformationList", schoolParentModelList);
                                    for (int i = 0; i < schoolParentModelList.size(); i++) {
                                        String schoolName = schoolParentModelList.get(i).getName();
                                        String schoolAdrz = schoolParentModelList.get(i).getAddress();
                                        Log.v("schoolName", schoolName);
                                        Log.v("schoolAdrz", schoolAdrz);

                                        if (schoolName != null && !schoolName.isEmpty() && !schoolName.equals("null")) {
                                            txtSchoolName.setText(schoolName);

                                        }
                                        if (schoolAdrz != null && !schoolAdrz.isEmpty() && !schoolAdrz.equals("null")) {
                                            txtSchoolAdrz.setText(schoolAdrz);
                                        }


                                    }
                                }

                                //SchoolStaffModel Calendar
                                if (calanderParentList.size() > 0) {

                                    adapterSchoolCalendar = new SchoolCalendarAdapter(getActivity(), calanderParentList);
                                    recyclerViewSchoolCalendar.setAdapter(adapterSchoolCalendar);
                                    Hawk.put("schoolCalendarList", calanderParentList);

                                }


                            } else if (success == 0) {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                            }

                        } else if (status.equals("401")) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                            logOut();
                            aviHomeFragmentLoader.hide();

                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviHomeFragmentLoader.hide();
                    }

                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    aviHomeFragmentLoader.hide();
                }

                aviHomeFragmentLoader.hide();
            }

            @Override
            public void onFailure(Call<HomeParentModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviHomeFragmentLoader.hide();

            }
        });
    }


}
