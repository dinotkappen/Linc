package com.app.linc.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.API.API;
import com.app.linc.API.APIClient;
import com.app.linc.Model.DeviceRegistration.DeviceRegModel;
import com.app.linc.Model.LogInModel.Student.StudentLogInModel;
import com.app.linc.Model.LogInModel.Student.StudentUser;
import com.app.linc.Model.Parent.LogIn.LogInParentModel;
import com.app.linc.Model.Parent.LogIn.UserParent;
import com.app.linc.Model.Staff.LogIn.LogInModelStaff;
import com.app.linc.Model.Staff.LogIn.UserStaff;
import com.app.linc.Other.Config;
import com.app.linc.Other.NetworkChangeReceiver;
import com.app.linc.Other.NotificationUtils;
import com.app.linc.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    String userType;
    String device_id;
    API apiInterface;
    String email, pwd;
    ImageView imgBack;
    String school_code;
    Switch logInSwitch;
    String profileImgUrl;
    CardView btnCardLogIn;
    TextView action_bar_title;
    Boolean logInSwitchState;
    EditText edtUserName, edtPwd;
    private NetworkChangeReceiver receiver;
    static Boolean flagNoInternetActivity = false;
    private AVLoadingIndicatorView aviSchoolSelectionActivity;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static String firebasseRegID;
    private static final String TAG = LogInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        apiInterface = APIClient.getClient().create(API.class);
        userType = Hawk.get("userType", "");
        school_code= Hawk.get("school_code", "");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_activity);
        Hawk.put("currentFragment",false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));


        imgBack = findViewById(R.id.imgBack);
        logInSwitch = findViewById(R.id.logInSwitch);
        logInSwitchState = logInSwitch.isChecked();
        aviSchoolSelectionActivity = (AVLoadingIndicatorView) findViewById(R.id.avi);
        aviSchoolSelectionActivity.hide();


        action_bar_title = findViewById(R.id.action_bar_title);
        btnCardLogIn = findViewById(R.id.btnCardLogIn);
        edtPwd = findViewById(R.id.edtPwd);
        edtUserName = findViewById(R.id.edtUserName);

        action_bar_title.setText(getString(R.string.log_in));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        logInSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    logInSwitchState = true;
                } else {
                    logInSwitchState = false;

                }
            }
        });

        btnCardLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                submitForm();

            }
        });
        displayFirebaseRegId();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }
        if (userType.equals("student")) {
            LogInStudent();
        } else if (userType.equals("staff")) {
            LogInStaff();
        } else if (userType.equals("parent")) {
            LogInParent();
        }

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail() {
        email = edtUserName.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edtUserName.setError(getString(R.string.validEmail));
            requestFocus(edtUserName);
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        pwd = edtPwd.getText().toString().trim();
        if (pwd.isEmpty()) {
            edtPwd.setError(getString(R.string.validPassword));
            requestFocus(edtPwd);
            return false;
        }

        return true;
    }

    public void LogInStudent() {
        aviSchoolSelectionActivity.show();
        Call<StudentLogInModel> call = apiInterface.Login(email, pwd,school_code);
        call.enqueue(new Callback<StudentLogInModel>() {
            @Override
            public void onResponse(Call<StudentLogInModel> call, Response<StudentLogInModel> response) {

                StudentLogInModel logInModel = new StudentLogInModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                logInModel = gson.fromJson(jsonElement, StudentLogInModel.class);
                Log.e("resultLogIn", jsonElement.toString());
                StudentUser userModel = logInModel.getUser();
                String status = logInModel.getStatus();
                int success = logInModel.getSuccess();
                if (status.equals("200")) {
                    if (success == 1) {
                        String userToken = userModel.getLoginToken();
                        String user_id = "" + userModel.getId();
                        Hawk.put("logInSwitchState", logInSwitchState);
                        Log.v("logInSwitchState", "" + logInSwitchState);
                        Hawk.put("userToken", userToken);
                        Log.v("userTokenLogIn", userToken);
                        Hawk.put("user_id", user_id);
                        profileImgUrl=getString(R.string.imgBasrUrl)+userModel.getPhoto();
                        Hawk.put("profileImgUrl",profileImgUrl);
                        Hawk.put("Name",userModel.getName());
                        Hawk.put("studentLogInModel",logInModel);
                        DeviceRegister(userToken);



                    } else if (success == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.LogInFailed), Toast.LENGTH_SHORT).show();
                    }
                } else if (status.equals("401")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }

                aviSchoolSelectionActivity.hide();
            }

            @Override
            public void onFailure(Call<StudentLogInModel> call, Throwable t) {
                Log.e("BagResponse", call.toString());
                aviSchoolSelectionActivity.hide();
            }
        });
    }

    public void LogInStaff() {
        aviSchoolSelectionActivity.show();
        Call<LogInModelStaff> call = apiInterface.LoginStaff(email, pwd,school_code);
        call.enqueue(new Callback<LogInModelStaff>() {
            @Override
            public void onResponse(Call<LogInModelStaff> call, Response<LogInModelStaff> response) {

                LogInModelStaff logInModel = new LogInModelStaff();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                logInModel = gson.fromJson(jsonElement, LogInModelStaff.class);
                Log.e("resultLogIn", jsonElement.toString());
                UserStaff userModel = logInModel.getUser();
                String status = logInModel.getStatus();
                int success = logInModel.getSuccess();
                if (status.equals("200")) {
                    if (success == 1) {
                        String userToken = userModel.getLoginToken();
                        String user_id = "" + userModel.getId();
                        Hawk.put("logInSwitchState", logInSwitchState);
                        Log.v("logInSwitchState", "" + logInSwitchState);
                        Hawk.put("userToken", userToken);
                        Log.v("userTokenLogIn", userToken);
                        Hawk.put("user_id", user_id);
                        Hawk.put("logInModelStaff",logInModel);
                        profileImgUrl=getString(R.string.imgBasrUrl)+userModel.getPhoto();
                        Hawk.put("profileImgUrl",profileImgUrl);
                        Hawk.put("Name",userModel.getName());


                        DeviceRegister(userToken);
                    } else if (success == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.LogInFailed), Toast.LENGTH_SHORT).show();
                    }
                } else if (status.equals("401")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }

                aviSchoolSelectionActivity.hide();
            }

            @Override
            public void onFailure(Call<LogInModelStaff> call, Throwable t) {
                Log.e("BagResponse", call.toString());
                aviSchoolSelectionActivity.hide();
            }
        });
    }

    public void LogInParent() {
        aviSchoolSelectionActivity.show();
        Call<LogInParentModel> call = apiInterface.logInParent(email, pwd,school_code);
        call.enqueue(new Callback<LogInParentModel>() {
            @Override
            public void onResponse(Call<LogInParentModel> call, Response<LogInParentModel> response) {

                LogInParentModel logInModel = new LogInParentModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                logInModel = gson.fromJson(jsonElement, LogInParentModel.class);
                Log.e("resultLogIn", jsonElement.toString());
                UserParent userModel = logInModel.getUser();
                String status = logInModel.getStatus();
                int success = logInModel.getSuccess();
                if (status.equals("200")) {

                    if (success == 1) {
                        String userToken = userModel.getLoginToken();
                        String user_id = "" + userModel.getId();
                        Hawk.put("logInSwitchState", logInSwitchState);
                        Log.v("logInSwitchState", "" + logInSwitchState);
                        Hawk.put("userToken", userToken);
                        Log.v("userTokenLogIn", userToken);
                        Hawk.put("user_id", user_id);
                        Hawk.put("logInParentModel",logInModel);
                        profileImgUrl=getString(R.string.imgBasrUrl)+userModel.getPhoto();
                        Hawk.put("profileImgUrl",profileImgUrl);
                        Hawk.put("Name",userModel.getName());
                        DeviceRegister(userToken);
                    } else if (success == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.LogInFailed), Toast.LENGTH_SHORT).show();
                    }
                } else if (status.equals("401")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }
                aviSchoolSelectionActivity.hide();
            }
            @Override
            public void onFailure(Call<LogInParentModel> call, Throwable t) {
                Log.e("BagResponse", call.toString());
                aviSchoolSelectionActivity.hide();

            }
        });
    }

    public void DeviceRegister(String userToken) {
        aviSchoolSelectionActivity.show();
        Call<DeviceRegModel> call = null;
        if(userType.equals("student"))
        {
            call = apiInterface.studentDeviceRegister(device_id, "Android","Android",userToken);
        }
        else if(userType.equals("parent"))
        {
            call = apiInterface.parentDeviceRegister(device_id, "Android","Android",userToken);
        }
        else if(userType.equals("staff"))
        {
            call = apiInterface.stffDeviceRegister(device_id, "Android","Android",userToken);
        }

        call.enqueue(new Callback<DeviceRegModel>() {
            @Override
            public void onResponse(Call<DeviceRegModel> call, Response<DeviceRegModel> response) {

                DeviceRegModel deviceRegModel = new DeviceRegModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                deviceRegModel = gson.fromJson(jsonElement, DeviceRegModel.class);
                Log.e("resultLogIn", jsonElement.toString());


                int success = deviceRegModel.getSuccess();
                String message=deviceRegModel.getMsg();
                String status = deviceRegModel.getStatus();
                if (status.equals("200")) {
                    if (success == 1) {

                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        finish();
                    } else if (success == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.LogInFailed), Toast.LENGTH_SHORT).show();
                    }
                } else if (status.equals("401")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }

                aviSchoolSelectionActivity.hide();
            }

            @Override
            public void onFailure(Call<DeviceRegModel> call, Throwable t) {
                Log.e("BagResponse", call.toString());
                aviSchoolSelectionActivity.hide();
            }
        });
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        String RegID;
        device_id=regId;
        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            RegID = "Firebase Reg Id: " + regId;
            Log.v("RegID", RegID);
        }

        else {
            RegID = ("Firebase Reg Id is not received yet!");
            Log.v("RegID", RegID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
