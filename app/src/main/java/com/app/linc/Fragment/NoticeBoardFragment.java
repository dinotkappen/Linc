package com.app.linc.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.app.linc.Adapter.NoticBoardHomeRecyclerViewAdapter;
import com.app.linc.Adapter.NoticeBoardRecyclerviewAdapter;
import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.StudentHomeModel.Noticeboard;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.app.linc.Activities.MainActivity.titlHide;


public class NoticeBoardFragment extends Fragment {
    List<Noticeboard> noticeboardList = new ArrayList<>();
    RecyclerView recyclerViewNoticeBoard;
    NoticeBoardRecyclerviewAdapter adapterNoticBoardRecyclerView;
    public NoticeBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_notice_board, container, false);
        titlHide(getResources().getString(R.string.notice_board));

        recyclerViewNoticeBoard = rootView.findViewById(R.id.recyclerViewNoticeBoard);
        recyclerViewNoticeBoard.setLayoutManager(new LinearLayoutManager(getActivity()));
        Hawk.put("currentFragment",false);

        noticeboardList= Hawk.get("noticeboardList",noticeboardList);


        if (noticeboardList.size() > 0) {

                adapterNoticBoardRecyclerView = new NoticeBoardRecyclerviewAdapter(getActivity(), noticeboardList);
                recyclerViewNoticeBoard.setAdapter(adapterNoticBoardRecyclerView);

        }


        return rootView;
    }




}
