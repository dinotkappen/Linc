package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.app.linc.Fragment.ChatFragment;
import com.app.linc.Fragment.NoticreBoardDetailsFragment;
import com.app.linc.Fragment.SendMessageFragment;
import com.app.linc.Model.Chat.ChatGroups.GroupListChat;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class ListChatGroupsAdapter extends RecyclerView.Adapter<ListChatGroupsAdapter.TasksViewHolder> {

    private Context mContext;
    private List<GroupListChat> groupListChat;



    public ListChatGroupsAdapter(Context mContext, List<GroupListChat> groupListChat) {
        this.mContext = mContext;
        this.groupListChat = groupListChat;

    }

    @Override
    public ListChatGroupsAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_chat_groups_layout, parent, false);

        return new ListChatGroupsAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListChatGroupsAdapter.TasksViewHolder holder, final int position) {
        Log.v("groupListChat", "" + groupListChat.size());
        final GroupListChat parentMode = groupListChat.get(position);


        String name = parentMode.getGroupName();



        if (name != null && !name.isEmpty() && !name.equals("null")) {

            holder.txtGroupName.setText(name);
        }

        holder.lineaerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        int chatGroupId=parentMode.getId();
        Log.v("chatGroupId",""+chatGroupId);

                Fragment fragment = new SendMessageFragment();
                FragmentTransaction transaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
                Hawk.put("chatGroupId",chatGroupId);
                Hawk.put("chatGroupName",name);

            }
        });


    }

    @Override
    public int getItemCount() {
        return groupListChat.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtGroupName;
        LinearLayout lineaerClick;


        public TasksViewHolder(View itemView) {
            super(itemView);


            txtGroupName = itemView.findViewById(R.id.txtGroupName);
            lineaerClick = itemView.findViewById(R.id.lineaerClick);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}