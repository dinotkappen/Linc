package com.app.linc.Activities;

import androidx.appcompat.app.AppCompatActivity;




import com.app.linc.R;
import retrofit2.Call;

import android.net.NetworkInfo;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.Window;
import android.widget.Toast;
import com.app.linc.API.API;
import com.google.gson.Gson;
import android.text.Editable;
import android.content.Intent;
import android.content.Context;
import android.widget.EditText;
import com.orhanobut.hawk.Hawk;
import android.text.TextWatcher;
import android.view.WindowManager;
import com.app.linc.API.APIClient;
import com.google.gson.JsonElement;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import com.wang.avi.AVLoadingIndicatorView;
import com.app.linc.Other.NetworkChangeReceiver;
import com.app.linc.Model.SchoolCodeValidatorModel.SchoolCodeValidatorModel;

import static com.app.linc.Activities.NoInternetActivity.closeActivity;

public class SchoolSelectionActivity extends AppCompatActivity {
    API apiInterface;
    String school_code;
    EditText edtSchoolCode;
    private NetworkChangeReceiver receiver;
    static Boolean flagNoInternetActivity = false;
    private AVLoadingIndicatorView aviSchoolSelectionActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_school_selection);
        Hawk.put("currentFragment",false);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        apiInterface = APIClient.getClient().create(API.class);
        edtSchoolCode = findViewById(R.id.edtSchoolCode);
        edtSchoolCode.setEnabled(true);
        aviSchoolSelectionActivity = (AVLoadingIndicatorView) findViewById(R.id.avi);
        aviSchoolSelectionActivity.hide();

        checkConnection();





    }


    public void SchoolCodeValidator() {
        aviSchoolSelectionActivity.show();
        edtSchoolCode.setEnabled(false);
        Call<SchoolCodeValidatorModel> call = apiInterface.schoolCodeValidator(school_code);
        call.enqueue(new Callback<SchoolCodeValidatorModel>() {
            @Override
            public void onResponse(Call<SchoolCodeValidatorModel> call, Response<SchoolCodeValidatorModel> response) {

                SchoolCodeValidatorModel schoolCodeValidatorModel = new SchoolCodeValidatorModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                schoolCodeValidatorModel = gson.fromJson(jsonElement, SchoolCodeValidatorModel.class);
                Log.e("resultLogIn", jsonElement.toString());
                String status = schoolCodeValidatorModel.getStatus();

                if (status.equals("200")) {
                    int success = schoolCodeValidatorModel.getSuccess();
                    if (success == 1) {
                        String msg = schoolCodeValidatorModel.getMsg();
                       // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Hawk.put("school_code", school_code);
                        Intent intent = new Intent(SchoolSelectionActivity.this, MenuActivity.class);
                        startActivity(intent);


                    } else if (success == 0) {
                        String msg = schoolCodeValidatorModel.getMsg();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        edtSchoolCode.setText("");
                    }
                } else if (status.equals("401")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SchoolSelectionActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }
                edtSchoolCode.setEnabled(true);
                aviSchoolSelectionActivity.hide();

            }

            @Override
            public void onFailure(Call<SchoolCodeValidatorModel> call, Throwable t) {
                Log.e("BagResponse", call.toString());
                edtSchoolCode.setEnabled(true);
                aviSchoolSelectionActivity.hide();

            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            edtSchoolCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    edtSchoolCode.setEnabled(true);


                }
            });


            edtSchoolCode.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (s.length() >= 4) {
                        school_code = edtSchoolCode.getText().toString();
                        SchoolCodeValidator();

                    }

                }
            });

        }else{
            Intent i = new Intent(SchoolSelectionActivity.this, NoInternetActivity.class);
            startActivity(i);

        }
    }

}
