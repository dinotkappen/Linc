package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticreBoardDetailsFragment extends Fragment {
    List<Noticeboard> noticeboardList = new ArrayList<>();
    int selectedNoticeID;
    TextView txtTitle, txtDate, txtContent, txtBy, txtAuthor;

    public NoticreBoardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_noticre_board_details, container, false);
        titlHide(getResources().getString(R.string.notice));
        txtTitle = rootView.findViewById(R.id.txtTitle);
        txtDate = rootView.findViewById(R.id.txtDate);
        txtContent = rootView.findViewById(R.id.txtContent);
        txtBy = rootView.findViewById(R.id.txtBy);
        txtAuthor = rootView.findViewById(R.id.txtAuthor);

        txtAuthor.setVisibility(View.GONE);
        txtBy.setVisibility(View.GONE);
        Hawk.put("currentFragment",false);

        noticeboardList = Hawk.get("noticeboardList", noticeboardList);
        selectedNoticeID = Hawk.get("selectedNoticeID", selectedNoticeID);
        if (noticeboardList.size() > 0) {
            for (int i = 0; i < noticeboardList.size(); i++) {
                int id = noticeboardList.get(i).getId();
                if (id == selectedNoticeID) {
                    String title = noticeboardList.get(i).getTitle();
                    String date = noticeboardList.get(i).getDate();
                    String content = noticeboardList.get(i).getContent();
                    String author = noticeboardList.get(i).getUserType();

                    if (title != null && !title.isEmpty() && !title.equals("null")) {
                        txtTitle.setText(title);
                    }
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

                        txtDate.setText(formatedDate);
                    }
                    if (content != null && !content.isEmpty() && !content.equals("null")) {
                        txtContent.setText(content);

                    }
                    if (author != null && !author.isEmpty() && !author.equals("null")) {
                        txtAuthor.setVisibility(View.VISIBLE);
                        txtBy.setVisibility(View.VISIBLE);
                        txtAuthor.setText(author);

                    }
                }
            }
        }


        return rootView;
    }


}
