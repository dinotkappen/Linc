package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Adapter.ChatHistoryAdapter;
import com.app.linc.Model.Chat.AddGroupChat;
import com.app.linc.Model.Chat.ChatHistory.ChatHistory;
import com.app.linc.Model.Chat.ChatHistory.ChatHistoryModel;
import com.app.linc.Model.Chat.ChatHistory.Chat_;
import com.app.linc.Model.Chat.CreateGroup.Group;
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

import static com.app.linc.Activities.MainActivity.logOut;
import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment {

    EditText edtMsg;
    int chatGroupId;
    API apiInterface;
    String chatMessage;
    String chatGroupName;
    public static int grp_id;
    String userToken, userType;
    LinearSmoothScroller smoothScroller;
    LinearLayout linearChatBox, linearSend;
    private AVLoadingIndicatorView aviLoader;
    public static RecyclerView recyclerViewChat;
    public static ChatHistoryAdapter chatHistoryAdapter;
    LinearLayout linearAddMember,linearExit,linearButtons;
    public static List<ChatHistory> chatHistoryList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_send_message, container, false);
        Hawk.put("currentFragment", true);
        apiInterface = APIClient.getClient().create(API.class);
        aviLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        aviLoader.show();

        chatGroupName=Hawk.get("chatGroupName",chatGroupName);

        titlHide(chatGroupName);

        edtMsg = (EditText) rootView.findViewById(R.id.edtMsg);
        linearChatBox = (LinearLayout) rootView.findViewById(R.id.linearChatBox);
        linearSend = (LinearLayout) rootView.findViewById(R.id.linearSend);
        linearAddMember = (LinearLayout) rootView.findViewById(R.id.linearAddMember);
        linearExit = (LinearLayout) rootView.findViewById(R.id.linearExit);
        linearButtons = (LinearLayout) rootView.findViewById(R.id.linearButtons);

        linearButtons.setVisibility(View.GONE);
        userType = Hawk.get("userType", userType);
        userToken = Hawk.get("userToken", userToken);
        chatGroupId = Hawk.get("chatGroupId", chatGroupId);

        recyclerViewChat = rootView.findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getActivity()));

         smoothScroller=new LinearSmoothScroller(getActivity()){
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        linearSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage = edtMsg.getText().toString();
                if (chatMessage != null && !chatMessage.isEmpty() && !chatMessage.equals("null")) {
                    if(userType.equals("staff"))
                    {
                        sendMessageStaff(chatMessage);
                    }
                    else if(userType.equals("student"))
                    {
                        sendMessageStudent(chatMessage);
                    }
                    else if(userType.equals("parent"))
                    {
                        sendMessageParent(chatMessage);
                    }
                }
                edtMsg.setText("");
            }
        });

        if (userType.equals("staff")) {
            getChatHistoryStaff(chatGroupId);
            linearButtons.setVisibility(View.VISIBLE);
        } else if (userType.equals("student")) {
            linearButtons.setVisibility(View.GONE);
            getChatHistoryStudent(chatGroupId);
        }
        else if(userType.equals("parent"))
        {
            linearButtons.setVisibility(View.GONE);
            getChatHistoryParent(chatGroupId);
        }

        linearAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddMembersFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupName", chatGroupName);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        linearExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeChatGroup(chatGroupId);
            }
        });
        return rootView;
    }

    public static void getNotificationMsg(ChatHistory chatHistoryNotificationModel) {
        try {
            chatHistoryList.add(chatHistoryNotificationModel);
            chatHistoryAdapter.notifyDataSetChanged();
            Log.e("NotificationModel", "" + chatHistoryList.size());
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());
        } catch (Exception ex) {
            Log.e("NotificationModel", "" + ex.getMessage());
        }
    }

    public void sendMessageStaff(String chatMessage) {


        if (chatHistoryList.size() > 0) {

            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("staff");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter.notifyDataSetChanged();
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


        }else{
            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("staff");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
            recyclerViewChat.setAdapter(chatHistoryAdapter);
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


            Log.e("Ohk", "Yes");
        }

        aviLoader.show();


        Call<AddGroupChat> call = apiInterface.sendMessageStaff(""+chatGroupId, chatMessage, userToken);
        Log.v("call", "" + call);

        call.enqueue(new Callback<AddGroupChat>() {
            @Override
            public void onResponse(Call<AddGroupChat> call, Response<AddGroupChat> response) {
                try {
                    AddGroupChat createGroupModel = new AddGroupChat();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    createGroupModel = gson.fromJson(jsonElement, AddGroupChat.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = createGroupModel.getStatus();
                    Group groupModel = new Group();
                    if (status.equals("200")) {
                        int success = createGroupModel.getSuccess();

                        if (success == 1) {


                            /*ChatHistory chatHistoryModel = new ChatHistory();
                            chatHistoryModel.setName("you");
                            chatHistoryModel.setUserType("staff");
                            Chat_ chatModel = new Chat_();
                            chatModel.setMessage(chatMessage);
                            chatHistoryModel.setChat(chatModel);
                            chatHistoryList.add(chatHistoryModel);
                            chatHistoryAdapter.notifyDataSetChanged();
                            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());*/


//

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

    public void sendMessageStudent(String chatMessage) {
        aviLoader.show();

        if (chatHistoryList.size() > 0) {

            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("student");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter.notifyDataSetChanged();
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


        }else{
            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("student");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
            recyclerViewChat.setAdapter(chatHistoryAdapter);
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


            Log.e("Ohk", "Yes");
        }

        Call<AddGroupChat> call = apiInterface.sendMessageStudent(""+chatGroupId, chatMessage, userToken);
        Log.v("call", "" + call);

        call.enqueue(new Callback<AddGroupChat>() {
            @Override
            public void onResponse(Call<AddGroupChat> call, Response<AddGroupChat> response) {
                try {
                    AddGroupChat createGroupModel = new AddGroupChat();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    createGroupModel = gson.fromJson(jsonElement, AddGroupChat.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = createGroupModel.getStatus();
                    Group groupModel = new Group();
                    if (status.equals("200")) {
                        int success = createGroupModel.getSuccess();

                        if (success == 1) {

//
//                            ChatHistory chatHistoryModel = new ChatHistory();
//                            chatHistoryModel.setName("you");
//                            chatHistoryModel.setUserType("staff");
//                            Chat_ chatModel = new Chat_();
//                            chatModel.setMessage(chatMessage);
//                            chatHistoryModel.setChat(chatModel);
//                            chatHistoryList.add(chatHistoryModel);
//                            chatHistoryAdapter.notifyDataSetChanged();
//                            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());
                            Log.v("sendMessageStudent","success");


//

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

    public void sendMessageParent(String chatMessage) {

        if (chatHistoryList.size() > 0) {

            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("parent");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter.notifyDataSetChanged();
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


        }else{
            ChatHistory chatHistoryModel = new ChatHistory();
            chatHistoryModel.setName("you");
            chatHistoryModel.setUserType("parent");
            Chat_ chatModel = new Chat_();
            chatModel.setMessage(chatMessage);
            chatHistoryModel.setChat(chatModel);
            chatHistoryList.add(chatHistoryModel);
            chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
            recyclerViewChat.setAdapter(chatHistoryAdapter);
            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());


            Log.e("Ohk", "Yes");
        }
        aviLoader.show();

        Call<AddGroupChat> call = apiInterface.sendMessageParent(""+chatGroupId, chatMessage, userToken);
        Log.v("call", "" + call);

        call.enqueue(new Callback<AddGroupChat>() {
            @Override
            public void onResponse(Call<AddGroupChat> call, Response<AddGroupChat> response) {
                try {
                    AddGroupChat createGroupModel = new AddGroupChat();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    createGroupModel = gson.fromJson(jsonElement, AddGroupChat.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = createGroupModel.getStatus();
                    Group groupModel = new Group();
                    if (status.equals("200")) {
                        int success = createGroupModel.getSuccess();

                        if (success == 1) {


//                            ChatHistory chatHistoryModel = new ChatHistory();
//                            chatHistoryModel.setName("you");
//                            chatHistoryModel.setUserType("staff");
//                            Chat_ chatModel = new Chat_();
//                            chatModel.setMessage(chatMessage);
//                            chatHistoryModel.setChat(chatModel);
//                            chatHistoryList.add(chatHistoryModel);
//                            chatHistoryAdapter.notifyDataSetChanged();
//                            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());
                            Log.v("sendMessageStudent","success");


//

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

    public void closeChatGroup(int chatGroupId) {
        grp_id = chatGroupId;
        aviLoader.show();
        Call<AddGroupChat> call = apiInterface.closeChatGroup("" + chatGroupId, userToken);
        Log.v("call", "" + call);
        call.enqueue(new Callback<AddGroupChat>() {
            @Override
            public void onResponse(Call<AddGroupChat> call, Response<AddGroupChat> response) {
                try {
                    AddGroupChat chatHistoryModel = new AddGroupChat();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    chatHistoryModel = gson.fromJson(jsonElement, AddGroupChat.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = chatHistoryModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = chatHistoryModel.getSuccess();
                            if (success == 1) {


                                ChatFragment chatFragment = new ChatFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, chatFragment);
                                transaction.addToBackStack(null).commit();


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
            public void onFailure(Call<AddGroupChat> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }



    public void getChatHistoryStaff(int chatGroupId) {
        grp_id = chatGroupId;
        aviLoader.show();
        Call<ChatHistoryModel> call = apiInterface.getChatHistoryStaff("" + chatGroupId, userToken);
        Log.v("call", "" + call);
        call.enqueue(new Callback<ChatHistoryModel>() {
            @Override
            public void onResponse(Call<ChatHistoryModel> call, Response<ChatHistoryModel> response) {
                try {
                    ChatHistoryModel chatHistoryModel = new ChatHistoryModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    chatHistoryModel = gson.fromJson(jsonElement, ChatHistoryModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = chatHistoryModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = chatHistoryModel.getSuccess();
                            if (success == 1) {


                                chatHistoryList = chatHistoryModel.getChat();

                                if (chatHistoryList.size() > 0) {

                                    chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
                                    recyclerViewChat.setAdapter(chatHistoryAdapter);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());
                                        }
                                    }, 200);



                                    Log.e("Ohk", "Yes");


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
            public void onFailure(Call<ChatHistoryModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }

    public void getChatHistoryStudent(int chatGroupId) {
        grp_id = chatGroupId;
        aviLoader.show();
        Call<ChatHistoryModel> call = apiInterface.getChatHistoryStudent("" + chatGroupId, userToken);
        Log.v("call", "" + call);
        call.enqueue(new Callback<ChatHistoryModel>() {
            @Override
            public void onResponse(Call<ChatHistoryModel> call, Response<ChatHistoryModel> response) {
                try {
                    ChatHistoryModel chatHistoryModel = new ChatHistoryModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    chatHistoryModel = gson.fromJson(jsonElement, ChatHistoryModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = chatHistoryModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = chatHistoryModel.getSuccess();
                            if (success == 1) {


                                chatHistoryList = chatHistoryModel.getChat();

                                if (chatHistoryList.size() > 0) {

                                    chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
                                    recyclerViewChat.setAdapter(chatHistoryAdapter);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerViewChat.smoothScrollToPosition(chatHistoryList.size());
                                        }
                                    }, 200);




                                    Log.e("Ohk", "Yes");


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
            public void onFailure(Call<ChatHistoryModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }

    public void getChatHistoryParent(int chatGroupId) {
        grp_id = chatGroupId;
        aviLoader.show();
        Call<ChatHistoryModel> call = apiInterface.getChatHistoryParent("" + chatGroupId, userToken);
        Log.v("call", "" + call);
        call.enqueue(new Callback<ChatHistoryModel>() {
            @Override
            public void onResponse(Call<ChatHistoryModel> call, Response<ChatHistoryModel> response) {
                try {
                    ChatHistoryModel chatHistoryModel = new ChatHistoryModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    chatHistoryModel = gson.fromJson(jsonElement, ChatHistoryModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = chatHistoryModel.getStatus();
                    if (status != null && !status.isEmpty() && !status.equals("null")) {
                        if (status.equals("200")) {
                            int success = chatHistoryModel.getSuccess();
                            if (success == 1) {


                                chatHistoryList = chatHistoryModel.getChat();

                                if (chatHistoryList.size() > 0) {

                                    chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), chatHistoryList);
                                    recyclerViewChat.setAdapter(chatHistoryAdapter);
                                    int newMsgPosition = chatHistoryList.size() - 1;
                                    // Notify recycler view insert one new data.
                                    chatHistoryAdapter.notifyItemInserted(newMsgPosition);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerViewChat.smoothScrollToPosition(newMsgPosition);
                                        }
                                    }, 200);

                                    Log.e("Ohk", "Yes");

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
            public void onFailure(Call<ChatHistoryModel> call, Throwable t) {

                Log.e("error", t.getMessage());
                aviLoader.hide();

            }
        });
    }


}
