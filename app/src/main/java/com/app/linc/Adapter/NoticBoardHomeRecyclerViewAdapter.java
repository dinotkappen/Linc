package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.linc.Fragment.NoticeBoardFragment;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoticBoardHomeRecyclerViewAdapter extends RecyclerView.Adapter<NoticBoardHomeRecyclerViewAdapter.TasksViewHolder> {

    private Context mContext;
    private List<Noticeboard> noticeBoardList;

    public NoticBoardHomeRecyclerViewAdapter(Context mContext, List<Noticeboard> noticeBoardList) {
        this.mContext = mContext;
        this.noticeBoardList = noticeBoardList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.noticeboard_list_layout, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, final int position) {
        Noticeboard noticeboard = noticeBoardList.get(position);
        if (position < 3) {
            String content = noticeboard.getTitle();
            String date = noticeboard.getDate();
            if (content != null && !content.isEmpty() && !content.equals("null")) {
                holder.txtNoNotice.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtDate.setVisibility(View.VISIBLE);
                holder.txtContent.setText(content);

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
            } else {
                holder.txtNoNotice.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.GONE);
                holder.txtDate.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return noticeBoardList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtContent, txtDate, txtNoNotice;

        public TasksViewHolder(View itemView) {
            super(itemView);


            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNoNotice = itemView.findViewById(R.id.txtNoNotice);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new NoticeBoardFragment();
            FragmentTransaction transaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null).commit();
        }
    }

}