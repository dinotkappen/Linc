package com.app.linc.Activities;



import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.linc.Other.NetworkChangeReceiver;
import com.app.linc.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

public class SplashActivity extends AppCompatActivity {
    private NetworkChangeReceiver receiver;
    private final int SPLASH_DISPLAY_LENGTH = 500;
   Boolean logInSwitchState=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

        logInSwitchState=Hawk.get("logInSwitchState", logInSwitchState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Start your app main activity
                if(logInSwitchState==true)
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, SchoolSelectionActivity.class);
                    startActivity(i);
                    finish();
                }

//
            }
        }, SPLASH_DISPLAY_LENGTH);




    }


}
