package com.app.linc.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.linc.R;

import java.util.Timer;

public class NoInternetActivity extends AppCompatActivity {
    Button ReTry;
    ProgressBar loading;
    ImageView img_refesh;
    LinearLayout lean_internet;
    static NoInternetActivity NoInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_no_internet);
        ReTry =findViewById(R.id.bt_retry);
        loading = findViewById(R.id.loading);
        img_refesh = findViewById(R.id.img_refesh);
        lean_internet =findViewById(R.id.lean_internet);
        lean_internet.setVisibility(View.VISIBLE);
        NoInternet=this;
        img_refesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);

                final Timer timer = new Timer();
                timer.scheduleAtFixedRate(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
//
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        // Stuff that updates the UI
                                        loading.setVisibility(View.GONE);


                                    }
                                });
//                                        loading.setVisibility(View.GONE);
                                timer.cancel();
                            }
                        },
                        2000, 5000);
            }
        });
    }
    public static void closeActivity()
    {
        NoInternet.finish();

    }
}