package com.app.linc.Fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    LinearLayout linearProfile, linearChangePwd;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Hawk.put("currentFragment",false);
        titlHide(getResources().getString(R.string.settings));

        linearChangePwd = rootView.findViewById(R.id.linearChangePwd);
        linearChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // linearProfile=rootView.findViewById(R.id.linearProfile);
//        linearProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new ProfileFragment();
//                FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(null).commit();
//            }
//        });


        return rootView;
    }


}
