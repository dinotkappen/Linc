package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarDetailFragment extends Fragment {

    TextView txtTo, txtFrom, txtTitle, txtContent;


    public CalendarDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar_detail, container, false);
        Hawk.put("currentFragment",false);

        txtTo       =   rootView.findViewById(R.id.txtTo);
        txtFrom     =   rootView.findViewById(R.id.txtFrom);
        txtTitle    =   rootView.findViewById(R.id.txtTitle);
        txtContent  =   rootView.findViewById(R.id.txtContent);

        return rootView;
    }

}
