package com.app.linc.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Adapter.ChatHistoryAdapter;
import com.app.linc.Adapter.ListChatGroupsAdapter;



import com.app.linc.Model.Chat.ChatGroups.GroupListChat;
import com.app.linc.Model.Chat.ChatGroups.ListChatGroupsModel;

import com.app.linc.Model.Chat.CreateGroup.CreateGroupModel;
import com.app.linc.Model.Chat.CreateGroup.Group;
import com.app.linc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.linc.Activities.MainActivity.logOut;
import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    Dialog dialog;
    Button btnAdd;
    API apiInterface;
    String groupName;
    EditText edtGroupName;
    FloatingActionButton fab;
    String userToken, userType;
    RecyclerView recyclerViewChatGroup;
    private AVLoadingIndicatorView aviLoader;
    ListChatGroupsAdapter listChatGroupsAdapter;
    List<GroupListChat> groupListChat = new ArrayList<>();



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        apiInterface = APIClient.getClient().create(API.class);
        titlHide(getResources().getString(R.string.chat));
        aviLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviLoader.show();
        Hawk.put("currentFragment",false);


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.hide();
        userType = Hawk.get("userType", userType);
        userToken = Hawk.get("userToken", userToken);

        if(userType.equals("staff"))
        {
            fab.show();
        }
        else
        {
            fab.hide();
        }

        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dilg_chat_groupname);

        edtGroupName = dialog.findViewById(R.id.edtGroupName);
        btnAdd = dialog.findViewById(R.id.btnAdd);

        recyclerViewChatGroup = rootView.findViewById(R.id.recyclerViewChatGroup);
        recyclerViewChatGroup.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName = edtGroupName.getText().toString();

                if (groupName != null && !groupName.isEmpty() && !groupName.equals("null")) {

                    createGroup();


                } else {
                    edtGroupName.setError(getString(R.string.group_name_validation));
                }

            }
        });

        if(userType.equals("staff"))
        {
            listChatGroupsStaff();
        }
        else if(userType.equals("student"))
        {
            listChatGroupsStudent();
        }
        else if(userType.equals("parent"))
        {
            listChatGroupsParent();
        }






        return rootView;
    }


    public void listChatGroupsStaff() {
        aviLoader.show();
        Call<ListChatGroupsModel> call = apiInterface.getChatGroupsStaff(userToken);
        Log.v("url", "" + apiInterface.getStudentHome(userToken));
        call.enqueue(new Callback<ListChatGroupsModel>() {
            @Override
            public void onResponse(Call<ListChatGroupsModel> call, Response<ListChatGroupsModel> response) {
                try {
                    ListChatGroupsModel listChatGroupsModel = new ListChatGroupsModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    listChatGroupsModel = gson.fromJson(jsonElement, ListChatGroupsModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = listChatGroupsModel.getStatus();

                    if (status.equals("200")) {
                        int success = listChatGroupsModel.getSuccess();
                        groupListChat = listChatGroupsModel.getGroup();
                        if (success == 1) {

                            Log.v("parentList", "" + groupListChat.size());
                            if (groupListChat.size() > 0) {
                                listChatGroupsAdapter = new ListChatGroupsAdapter(getActivity(), groupListChat);
                                recyclerViewChatGroup.setAdapter(listChatGroupsAdapter);
                            }

                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviLoader.hide();

                    }
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    logOut();
                    aviLoader.hide();
                }

                aviLoader.hide();
            }

            @Override
            public void onFailure(Call<ListChatGroupsModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                logOut();
                aviLoader.hide();


            }
        });
    }

    public void listChatGroupsStudent() {
        aviLoader.show();
        Call<ListChatGroupsModel> call = apiInterface.getChatGroupsStudent(userToken);
        Log.v("url", "" + apiInterface.getStudentHome(userToken));
        call.enqueue(new Callback<ListChatGroupsModel>() {
            @Override
            public void onResponse(Call<ListChatGroupsModel> call, Response<ListChatGroupsModel> response) {
                try {
                    ListChatGroupsModel listChatGroupsModel = new ListChatGroupsModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    listChatGroupsModel = gson.fromJson(jsonElement, ListChatGroupsModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = listChatGroupsModel.getStatus();

                    if (status.equals("200")) {
                        int success = listChatGroupsModel.getSuccess();
                        groupListChat = listChatGroupsModel.getGroup();
                        if (success == 1) {

                            Log.v("parentList", "" + groupListChat.size());
                            if (groupListChat.size() > 0) {
                                listChatGroupsAdapter = new ListChatGroupsAdapter(getActivity(), groupListChat);
                                recyclerViewChatGroup.setAdapter(listChatGroupsAdapter);
                            }

                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviLoader.hide();

                    }
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    logOut();
                    aviLoader.hide();
                }

                aviLoader.hide();
            }

            @Override
            public void onFailure(Call<ListChatGroupsModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                logOut();
                aviLoader.hide();


            }
        });
    }

    public void listChatGroupsParent() {
        aviLoader.show();
        Call<ListChatGroupsModel> call = apiInterface.getChatGroupsParent(userToken);
        Log.v("url", "" + apiInterface.getStudentHome(userToken));
        call.enqueue(new Callback<ListChatGroupsModel>() {
            @Override
            public void onResponse(Call<ListChatGroupsModel> call, Response<ListChatGroupsModel> response) {
                try {
                    ListChatGroupsModel listChatGroupsModel = new ListChatGroupsModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    listChatGroupsModel = gson.fromJson(jsonElement, ListChatGroupsModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = listChatGroupsModel.getStatus();

                    if (status.equals("200")) {
                        int success = listChatGroupsModel.getSuccess();
                        groupListChat = listChatGroupsModel.getGroup();
                        if (success == 1) {

                            Log.v("parentList", "" + groupListChat.size());
                            if (groupListChat.size() > 0) {
                                listChatGroupsAdapter = new ListChatGroupsAdapter(getActivity(), groupListChat);
                                recyclerViewChatGroup.setAdapter(listChatGroupsAdapter);
                            }

                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();
                        aviLoader.hide();

                    }
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());
                    logOut();
                    aviLoader.hide();
                }

                aviLoader.hide();
            }

            @Override
            public void onFailure(Call<ListChatGroupsModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                logOut();
                aviLoader.hide();


            }
        });
    }



    public void createGroup() {
        aviLoader.show();

        Call<CreateGroupModel> call = apiInterface.createChatGroup(groupName, userToken);
        Log.v("call", "" + call);

        call.enqueue(new Callback<CreateGroupModel>() {
            @Override
            public void onResponse(Call<CreateGroupModel> call, Response<CreateGroupModel> response) {
                try {
                    CreateGroupModel createGroupModel = new CreateGroupModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    createGroupModel = gson.fromJson(jsonElement, CreateGroupModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = createGroupModel.getStatus();
                    Group groupModel = new Group();
                    if (status.equals("200")) {
                        int success = createGroupModel.getSuccess();

                        if (success == 1) {


                            groupModel = createGroupModel.getGroup();
                            int chatGroupID = groupModel.getId();
                            String group = groupModel.getGroupName();
                            Hawk.put("chatGroupID", chatGroupID);
                            AddMembersFragment addMembersFragment = new AddMembersFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("groupName", groupName);
                            addMembersFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, addMembersFragment);
                            transaction.addToBackStack(null).commit();
                            dialog.dismiss();


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
            public void onFailure(Call<CreateGroupModel> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviLoader.hide();

            }
        });
    }
}
