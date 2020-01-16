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

import com.app.linc.Fragment.ProfileFragment;
import com.app.linc.Model.Staff.Home.ClassModelStaff;
import com.app.linc.Model.Staff.Home.StudentStaffModel;
import com.app.linc.Other.NetworkChangeReceiver;
import com.app.linc.R;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class ProfileRecyclerviewAdapter extends RecyclerView.Adapter<ProfileRecyclerviewAdapter.TasksViewHolder> {

    private Context mContext;
    private List<StudentStaffModel> profileList;

    public ProfileRecyclerviewAdapter(Context mContext, List<StudentStaffModel> profileList) {
        this.mContext = mContext;
        this.profileList = profileList;
    }

    @Override
    public ProfileRecyclerviewAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_adapter_layout, parent, false);

        return new ProfileRecyclerviewAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, final int position) {
        Log.v("profileList",""+profileList.size());
        final StudentStaffModel profileModel = profileList.get(position);
        ClassModelStaff classModel = profileModel.getClassModel();
        String imgUrl = profileModel.getPhoto();
        String name = profileModel.getName();
         String year = classModel.getName();
        String section = classModel.getSection();

        if (name != null && !name.isEmpty() && !name.equals("null")) {

            holder.txtStudentName.setText(name);
        }

        if (year != null && !year.isEmpty() && !year.equals("null")) {

            holder.txtClass.setText(mContext.getString(R.string.YearLevel) + year);
        }
        if (section != null && !section.isEmpty() && !section.equals("null")) {

            holder.txtSection.setText(mContext.getString(R.string.Section) + section);
        }
        if (imgUrl != null && !imgUrl.isEmpty() && !imgUrl.equals("null")) {
            imgUrl = mContext.getString(R.string.imgBasrUrl) + imgUrl;
            Glide
                    .with((mContext))
                    .load(imgUrl)
                    .centerCrop()
                    .placeholder(R.drawable.man)
                    .into(holder.imgStudent);
        }

        holder.lineaerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=profileModel.getId();
                Fragment fragment = new ProfileFragment();
                Hawk.put("selectedProfileId",id);
                FragmentTransaction transaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtStudentName, txtClass, txtSection;
        ImageView imgStudent;
        LinearLayout lineaerClick;

        public TasksViewHolder(View itemView) {
            super(itemView);


            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtClass = itemView.findViewById(R.id.txtClass);
            txtSection = itemView.findViewById(R.id.txtSection);
            imgStudent = itemView.findViewById(R.id.imgStudent);
            lineaerClick = itemView.findViewById(R.id.lineaerClick);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
