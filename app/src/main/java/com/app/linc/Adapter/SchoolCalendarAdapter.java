package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.linc.Fragment.SchoolCalendarFragment;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.app.linc.Fragment.SchoolCalendarFragment.fragmentset;

public class SchoolCalendarAdapter extends RecyclerView.Adapter<SchoolCalendarAdapter.TasksViewHolder> {

    private Context mContext;
    private List<SchoolCalander> schoolCalanderList;

    public SchoolCalendarAdapter(Context mContext, List<SchoolCalander> schoolCalanderList) {
        this.mContext = mContext;
        this.schoolCalanderList = schoolCalanderList;
    }

    @Override
    public SchoolCalendarAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_school_calendar, parent, false);
        return new SchoolCalendarAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SchoolCalendarAdapter.TasksViewHolder holder, final int position) {
        SchoolCalander schoolCalander = schoolCalanderList.get(position);
        final ArrayList<Date> dateArrayList =new ArrayList<>();

            String content = schoolCalander.getTitle();


            String startDate_str = schoolCalander.getStartTime();
            String endDate_str=schoolCalander.getEndTime();
            if (content != null && !content.isEmpty() && !content.equals("null")) {
                holder.txtNoSchoolInfo.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtDate.setVisibility(View.VISIBLE);
                holder.txtContent.setText(content);

                if (startDate_str != null && !startDate_str.isEmpty() && !startDate_str.equals("null")) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    Date dt = null;
                    try {
                        dt = format.parse(startDate_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat your_format = new SimpleDateFormat("dd-MM-yyyy");

                     holder.formatedStartDate = your_format.format(dt);
                    Log.v("date", startDate_str);

                    holder.txtDate.setText("" + holder.formatedStartDate);
                }

            } else {
                holder.txtNoSchoolInfo.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.GONE);
                holder.txtDate.setVisibility(View.GONE);
            }


            if (endDate_str != null && !endDate_str.isEmpty() && !endDate_str.equals("null")) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                Date dt = null;
                try {
                    dt = format.parse(endDate_str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat your_format = new SimpleDateFormat("dd-MM-yyyy");

                 holder.formatedEndDate = your_format.format(dt);
                Log.v("date", endDate_str);





                List<Date> dates = new ArrayList<Date>();


                DateFormat formatter;

                formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date startDate = null;
                try {
                    startDate = (Date) formatter.parse(holder.formatedStartDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Date endDate = null;
                try {
                    endDate = (Date) formatter.parse(holder.formatedEndDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
                long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
                long curTime = startDate.getTime();
                while (curTime <= endTime) {
                    dates.add(new Date(curTime));
                    curTime += interval;
                }
                for (int i = 0; i < dates.size(); i++) {
                    Date lDate = (Date) dates.get(i);
                    String ds = formatter.format(lDate);
                    dateArrayList.add(lDate);

                }
                Hawk.put("dateArrayList",dateArrayList);
                if(dateArrayList.size()>0)
                {
                    if(fragmentset==true) {
                        //Dates();
                    }
                }


            }


        holder.linearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Date> dates = new ArrayList<Date>();
                DateFormat formatter;

                formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date startDate = null;
                try {
                    startDate = (Date) formatter.parse(holder.formatedStartDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Date endDate = null;
                try {
                    endDate = (Date) formatter.parse(holder.formatedEndDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
                long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
                long curTime = startDate.getTime();
                while (curTime <= endTime) {
                    dates.add(new Date(curTime));
                    curTime += interval;
                }
                for (int i = 0; i < dates.size(); i++) {
                    Date lDate = (Date) dates.get(i);
                    String ds = formatter.format(lDate);
                    dateArrayList.add(lDate);

                }
                Hawk.put("dateArrayList",dateArrayList);
                if(dateArrayList.size()>0)
                {
                    //Dates();
                }
            }
        });
        }



    @Override
    public int getItemCount() {
        return schoolCalanderList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder  {


        TextView txtContent, txtDate, txtNoSchoolInfo;
        String formatedStartDate,formatedEndDate;
        LinearLayout linearClick;

        public TasksViewHolder(View itemView) {
            super(itemView);


            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNoSchoolInfo = itemView.findViewById(R.id.txtNoSchoolInfo);
            linearClick = itemView.findViewById(R.id.linearClick);


        }

//        @Override
//        public void onClick(View view) {
//            Fragment fragment = new SchoolCalendarFragment();
//            FragmentTransaction transaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, fragment);
//            transaction.addToBackStack(null).commit();
       //}
    }
}
