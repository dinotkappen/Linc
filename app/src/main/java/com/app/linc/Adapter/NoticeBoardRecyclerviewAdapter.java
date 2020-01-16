package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.linc.Fragment.NoticeBoardFragment;
import com.app.linc.Fragment.NoticreBoardDetailsFragment;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoticeBoardRecyclerviewAdapter extends RecyclerView.Adapter<NoticeBoardRecyclerviewAdapter.TasksViewHolder> {

    private Context mContext;
    private List<Noticeboard> noticeBoardList;

    public NoticeBoardRecyclerviewAdapter(Context mContext, List<Noticeboard> noticeBoardList) {
        this.mContext = mContext;
        this.noticeBoardList = noticeBoardList;
    }

    @Override
    public NoticeBoardRecyclerviewAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.noticeboard_detail_adapter_layout, parent, false);
        return new NoticeBoardRecyclerviewAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoticeBoardRecyclerviewAdapter.TasksViewHolder holder, final int position) {
        final Noticeboard noticeboard = noticeBoardList.get(position);

        if ((position % 2) == 0) {

            holder.linearBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
        }
        // even
        else {


            holder.linearBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrange));
        }


        String content = noticeboard.getTitle();
        String date = noticeboard.getDate();
        if (content != null && !content.isEmpty() && !content.equals("null")) {

            holder.txtTitle.setText(content);
            if (date != null && !date.isEmpty() && !date.equals("null")) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = null;
                try {
                    dt = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat your_format = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = your_format.format(dt);
                Log.v("date", date);
                holder.txtDate.setText("" + formatedDate);
            }
        }

        holder.linearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = noticeboard.getId();
                Hawk.put("selectedNoticeID", id);
                Fragment fragment = new NoticreBoardDetailsFragment();
            FragmentTransaction transaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeBoardList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtDate, txtTitle;
        LinearLayout linearBackground, linearClick;

        public TasksViewHolder(View itemView) {
            super(itemView);


            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            linearBackground = itemView.findViewById(R.id.linearBackground);
            linearClick = itemView.findViewById(R.id.linearClick);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Fragment fragment = new NoticeBoardFragment();
//            FragmentTransaction transaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, fragment);
//            transaction.addToBackStack(null).commit();
        }
    }

}