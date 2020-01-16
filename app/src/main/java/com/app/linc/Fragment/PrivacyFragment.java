package com.app.linc.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Adapter.NoticBoardHomeRecyclerViewAdapter;
import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.PrivacyPolicy.PrivacyPolicyModel;
import com.app.linc.Model.Staff.LogIn.LogInModelStaff;
import com.app.linc.Model.StudentHomeModel.StudentHome;
import com.app.linc.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.linc.Activities.MainActivity.logOut;
import static com.app.linc.Activities.MainActivity.titlHide;


public class PrivacyFragment extends Fragment {

    API apiInterface;
    String userToken;
    TextView txtPrivacyPolicy;
    private AVLoadingIndicatorView aviPrivacyPolicyLoader;
    List<String> informationList=new ArrayList<>();


    public PrivacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_privacy, container, false);
        Hawk.put("currentFragment",false);
        aviPrivacyPolicyLoader = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        txtPrivacyPolicy = (TextView) rootView.findViewById(R.id.txtPrivacyPolicy);
        aviPrivacyPolicyLoader.show();
        titlHide(getResources().getString(R.string.privacy_statement));
        apiInterface = APIClient.getClient().create(API.class);

        getPrivacyPolicy();


        return rootView;
    }


    public void getPrivacyPolicy() {

        Call<PrivacyPolicyModel> call = apiInterface.getPrivacyPolicy();
        call.enqueue(new Callback<PrivacyPolicyModel>() {
            @Override
            public void onResponse(Call<PrivacyPolicyModel> call, Response<PrivacyPolicyModel> response) {
                try {
                    PrivacyPolicyModel privacyPolicyModel = new PrivacyPolicyModel();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    privacyPolicyModel = gson.fromJson(jsonElement, PrivacyPolicyModel.class);
                    Log.e("resultStudentHome", jsonElement.toString());
                    String status = privacyPolicyModel.getStatus();

                    if (status.equals("200")) {
                        int success = privacyPolicyModel.getSuccess();
                        if (success == 1) {

                            String information=privacyPolicyModel.getInformation();

                            if (information != null && !information.isEmpty() && !information.equals("null"))
                            {
                                txtPrivacyPolicy.setText(information);
                            }


                        } else if (success == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ErrorMsg), Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("401")) {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.SessionValidation), Toast.LENGTH_SHORT).show();
                        logOut();

                    }
                    aviPrivacyPolicyLoader.hide();
                } catch (Exception ex) {
                    String jk = ex.getMessage().toString();
                    Log.v("jd", ex.getMessage().toString());

                    aviPrivacyPolicyLoader.hide();
                }


            }

            @Override
            public void onFailure(Call<PrivacyPolicyModel> call, Throwable t) {

                Log.e("error", t.getMessage());

                aviPrivacyPolicyLoader.hide();

            }
        });
    }

}

