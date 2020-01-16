package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Adapter.AllStudentAdapter;
import com.app.linc.Adapter.NoticBoardHomeRecyclerViewAdapter;
import com.app.linc.Adapter.AllParentAdapter;
import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.Chat.AddGroupChat;
import com.app.linc.Model.Chat.AllParents.AllParentsModel;
import com.app.linc.Model.Chat.AllParents.Information;
import com.app.linc.Model.Chat.AllParents.Parent;
import com.app.linc.Model.Chat.AllStudents.AllStudentChatModel;
import com.app.linc.Model.Chat.AllStudents.InformationStudentChat;
import com.app.linc.Model.Chat.AllStudents.Studen;
import com.app.linc.Model.Chat.GroupMemberModel;
import com.app.linc.Model.Other.ProfileUpdateModel;
import com.app.linc.Model.Parent.Home.HomeParentModel;
import com.app.linc.Model.Parent.Home.InformationParent;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.linc.Activities.MainActivity.logOut;
import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMembersFragment extends Fragment {

    API apiInterface;
    String groupName;
    FloatingActionButton fab;
    String userToken, userType;
    TextView txtParent, txtStudent;
    AllParentAdapter allParentAdapter;
    AllStudentAdapter allStudentAdapter;
    private AVLoadingIndicatorView aviLoader;
    LinearLayout linearParent, linearStudent;
    List<Parent> parentList = new ArrayList<>();
    List<Studen> studentList = new ArrayList<>();
    RecyclerView recyclerViewAllStudent, recyclerViewAllParent;
   // List<GroupMemberModel> groupMembersList = new ArrayList();
   HashMap<Integer, GroupMemberModel> groupMembersList = new HashMap<Integer, GroupMemberModel>();
    public AddMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_members, container, false);
        apiInterface = APIClient.getClient().create(API.class);
        groupName = getArguments().getString("groupName");
        titlHide(groupName);
        aviLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviLoader.show();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        recyclerViewAllStudent = rootView.findViewById(R.id.recyclerViewAllStudent);
        recyclerViewAllStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAllStudent.setVisibility(View.GONE);

        recyclerViewAllParent = rootView.findViewById(R.id.recyclerViewAllParent);
        recyclerViewAllParent.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAllParent.setVisibility(View.VISIBLE);

        userType = Hawk.get("userType", userType);
        userToken = Hawk.get("userToken", userToken);
        Hawk.put("currentFragment", false);


        txtParent = rootView.findViewById(R.id.txtParent);
        txtStudent = rootView.findViewById(R.id.txtStudent);
        linearParent = rootView.findViewById(R.id.linearParent);
        linearStudent = rootView.findViewById(R.id.linearStudent);

        linearParent.setBackgroundResource(R.color.colorgray);
        linearStudent.setBackgroundResource(R.color.colorWhite);
        txtParent.setTextColor(getResources().getColor(R.color.colorWhite));
        txtStudent.setTextColor(getResources().getColor(R.color.colorBlack));

        getAllParent();

        Hawk.put("groupMembersList","");


        linearParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aviLoader.show();
                linearParent.setBackgroundResource(R.color.colorgray);
                linearStudent.setBackgroundResource(R.color.colorWhite);
                txtParent.setTextColor(getResources().getColor(R.color.colorWhite));
                txtStudent.setTextColor(getResources().getColor(R.color.colorBlack));
                recyclerViewAllStudent.setVisibility(View.GONE);
                recyclerViewAllParent.setVisibility(View.VISIBLE);
                studentList.clear();
                parentList.clear();
                getAllParent();

            }
        });

        linearStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aviLoader.show();
                linearParent.setBackgroundResource(R.color.colorWhite);
                linearStudent.setBackgroundResource(R.color.colorgray);
                txtParent.setTextColor(getResources().getColor(R.color.colorBlack));
                txtStudent.setTextColor(getResources().getColor(R.color.colorWhite));
                recyclerViewAllStudent.setVisibility(View.VISIBLE);
                recyclerViewAllParent.setVisibility(View.GONE);
                studentList.clear();
                parentList.clear();
                getAllStudent();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aviLoader.show();
                groupMembersList = Hawk.get("groupMembersList", groupMembersList);
                if (groupMembersList.size() > 0) {
                    addGroupMembers();
                }

            }
        });
        return rootView;
    }

    public void addGroupMembers() {
        aviLoader.show();
        String groupID=null;
        ArrayList<String> groupMembers = new ArrayList<>();
        for (HashMap.Entry<Integer, GroupMemberModel> entry : groupMembersList.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            GroupMemberModel groupMemberModel=entry.getValue();
            String user=groupMemberModel.getMemberType();
            String id=groupMemberModel.getMemberID();
            groupID=groupMemberModel.getGroupID();
            String format=id+"$"+user;
            groupMembers.add(format);
        }



      //  groupMembers.add("2$parent");
        Call<AddGroupChat> call = apiInterface.addChatGroupMembers(groupID, groupMembers, userToken);
        Log.v("call", "" + call);

        String finalGroupID = groupID;
        call.enqueue(new Callback<AddGroupChat>() {
            @Override
            public void onResponse(Call<AddGroupChat> call, Response<AddGroupChat> response) {
                try {
                    AddGroupChat addGroupChat = new AddGroupChat();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    addGroupChat = gson.fromJson(jsonElement, AddGroupChat.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = addGroupChat.getStatus();

                    if (status.equals("200")) {
                        int success = addGroupChat.getSuccess();
                        String msg = addGroupChat.getMag();
                        if (success == 1) {

                            Fragment fragment = new SendMessageFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null).commit();
                            Hawk.put("chatGroupId",Integer.parseInt(finalGroupID));
                            Hawk.put("chatGroupName",groupName);
                            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        // logOut();

                    }
                    aviLoader.hide();
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());

                    aviLoader.hide();
                }


            }

            @Override
            public void onFailure(Call<AddGroupChat> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviLoader.hide();

            }
        });
    }

    public void getAllStudent() {
        aviLoader.show();
        Call<AllStudentChatModel> call = apiInterface.getAllStudents(userToken);
        Log.v("url", "" + apiInterface.getParentHome(userToken));
        Log.v("call", "" + call);
        call.enqueue(new Callback<AllStudentChatModel>() {
            @Override
            public void onResponse(Call<AllStudentChatModel> call, Response<AllStudentChatModel> response) {
                try {
                    AllStudentChatModel allStudentChatModel = new AllStudentChatModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    allStudentChatModel = gson.fromJson(jsonElement, AllStudentChatModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = allStudentChatModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = allStudentChatModel.getSuccess();
                            if (success == 1) {
                                InformationStudentChat informationStudentChat = new InformationStudentChat();
                                informationStudentChat = allStudentChatModel.getInformation();
                                studentList = informationStudentChat.getStudens();


                                if (studentList.size() > 0) {

                                    Log.v("studentList", "" + studentList.size());
                                    allStudentAdapter = new AllStudentAdapter(getActivity(), studentList);
                                    recyclerViewAllStudent.setAdapter(allStudentAdapter);


                                }


                            } else if (success == 0) {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                            }

                        } else if (status.equals("401")) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                            logOut();
                            aviLoader.hide();

                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviLoader.hide();
                    }

                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    aviLoader.hide();
                }

                aviLoader.hide();
            }

            @Override
            public void onFailure(Call<AllStudentChatModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }

    public void getAllParent() {
        aviLoader.show();
        Call<AllParentsModel> call = apiInterface.getAllParents(userToken);
        Log.v("url", "" + apiInterface.getParentHome(userToken));
        Log.v("call", "" + call);
        call.enqueue(new Callback<AllParentsModel>() {
            @Override
            public void onResponse(Call<AllParentsModel> call, Response<AllParentsModel> response) {
                try {
                    AllParentsModel allParentsModel = new AllParentsModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    allParentsModel = gson.fromJson(jsonElement, AllParentsModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = allParentsModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = allParentsModel.getSuccess();
                            if (success == 1) {
                                Information informationParent = new Information();
                                informationParent = allParentsModel.getInformation();
                                parentList = informationParent.getParents();


                                if (parentList.size() > 0) {

                                    Log.v("parentList", "" + parentList.size());

                                    allParentAdapter = new AllParentAdapter(getActivity(), parentList);
                                    recyclerViewAllParent.setAdapter(allParentAdapter);


                                }


                            } else if (success == 0) {
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                            }

                        } else if (status.equals("401")) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                            logOut();
                            aviLoader.hide();

                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviLoader.hide();
                    }

                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    aviLoader.hide();
                }

                aviLoader.hide();
            }

            @Override
            public void onFailure(Call<AllParentsModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }
}
