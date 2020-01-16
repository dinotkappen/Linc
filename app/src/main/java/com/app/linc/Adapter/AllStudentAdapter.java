package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.app.linc.Model.Chat.AllStudents.Studen;
import com.app.linc.Model.Chat.AllStudents.Studen;
import com.app.linc.Model.Chat.GroupMemberModel;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllStudentAdapter extends RecyclerView.Adapter<AllStudentAdapter.TasksViewHolder> {

    private Context mContext;
    private List<Studen> studentList;
   // List<GroupMemberModel> groupMembersList = new ArrayList();
   HashMap<Integer, GroupMemberModel> groupMembersList = new HashMap<Integer, GroupMemberModel>();


    public AllStudentAdapter(Context mContext, List<Studen> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;

    }

    @Override
    public AllStudentAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_student_adapter_layout, parent, false);

        return new AllStudentAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllStudentAdapter.TasksViewHolder holder, final int position) {
        Log.v("studentList", "" + studentList.size());
        final Studen parentMode = studentList.get(position);
        try {
            groupMembersList = Hawk.get("groupMembersList", groupMembersList);
        }
        catch (Exception ex)
        {

        }

        String imgUrl = parentMode.getPhoto();
        String name = parentMode.getName();
        String email = parentMode.getEmail();
        String memberID = "" + parentMode.getId();


        if (name != null && !name.isEmpty() && !name.equals("null")) {

            holder.txtName.setText(name);
        }

        if (email != null && !email.isEmpty() && !email.equals("null")) {

            holder.txtEmail.setText(email);
        }


        holder.lineaerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int f = holder.imgChecked.getVisibility();
                Log.v("f", "" + f);

                int chatGroupID = 0;
                chatGroupID = Hawk.get("chatGroupID", chatGroupID);
                if (f == View.VISIBLE) {
                    if (groupMembersList.size() > 0) {

                        groupMembersList.remove(position);
                        printMap(groupMembersList);


                    }
                    holder.imgChecked.setVisibility(View.INVISIBLE);
                } else {
                    GroupMemberModel groupMembeModel = new GroupMemberModel();

                        groupMembeModel.setMemberType("student");
                        groupMembeModel.setMemberID(memberID);
                        groupMembeModel.setGroupID("" + chatGroupID);
                        groupMembersList.put(position,groupMembeModel);

                    holder.imgChecked.setVisibility(View.VISIBLE);
                }
                Hawk.put("groupMembersList", groupMembersList);
                //notifyDataSetChanged();
            }
        });


    }
    private static void printMap(Map < Integer, GroupMemberModel > courseMap) {
        for (Integer s: courseMap.keySet()) {
            Log.v("Res",""+s);
        }
    }
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtName, txtEmail;
        ImageView imgChecked;
        LinearLayout lineaerClick;


        public TasksViewHolder(View itemView) {
            super(itemView);


            txtName = itemView.findViewById(R.id.txtName);
            imgChecked = itemView.findViewById(R.id.imgChecked);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            lineaerClick = itemView.findViewById(R.id.lineaerClick);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
