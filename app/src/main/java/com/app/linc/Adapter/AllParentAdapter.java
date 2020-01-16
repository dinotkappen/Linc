package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.linc.Fragment.ProfileFragment;
import com.app.linc.Model.Chat.AllParents.Parent;
import com.app.linc.Model.Chat.AllParents.Information;
import com.app.linc.Model.Chat.AllParents.Parent;
import com.app.linc.Model.Chat.GroupMemberModel;
import com.app.linc.Model.Staff.Home.ClassModelStaff;
import com.app.linc.R;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllParentAdapter extends RecyclerView.Adapter<AllParentAdapter.TasksViewHolder> {

    private Context mContext;
    private List<Parent> parentModelList;
    HashMap<Integer, GroupMemberModel> groupMembersList = new HashMap<Integer, GroupMemberModel>();

    public AllParentAdapter(Context mContext, List<Parent> parentModelList) {
        this.mContext = mContext;
        this.parentModelList = parentModelList;
    }

    @Override
    public AllParentAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_parent_adapter_layout, parent, false);

        return new AllParentAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllParentAdapter.TasksViewHolder holder, final int position) {
        Log.v("parentModelList", "" + parentModelList.size());
        final Parent parentMode = parentModelList.get(position);

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
                int f = holder.imgChecked.getVisibility() ;
                Log.v("f",""+f);
                int chatGroupID = 0;
                chatGroupID = Hawk.get("chatGroupID", chatGroupID);
                if (f==View.VISIBLE) {

                    if (groupMembersList.size() > 0) {

                        groupMembersList.remove(position);
                        printMap(groupMembersList);


                    }
                    holder.imgChecked.setVisibility(View.INVISIBLE);
                } else {
                    GroupMemberModel groupMembeModel = new GroupMemberModel();

                    groupMembeModel.setMemberType("parent");
                    groupMembeModel.setMemberID(memberID);
                    groupMembeModel.setGroupID("" + chatGroupID);
                    groupMembersList.put(position,groupMembeModel);
                    holder.imgChecked.setVisibility(View.VISIBLE);
                }
                Hawk.put("groupMembersList", groupMembersList);
                notifyDataSetChanged();
            }
        });
    }

    private static void printMap(Map< Integer, GroupMemberModel > courseMap) {
        for (Integer s: courseMap.keySet()) {
            Log.v("Res",""+s);
        }
    }
    @Override
    public int getItemCount() {
        return parentModelList.size();
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
