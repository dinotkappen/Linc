package com.app.linc.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.linc.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

public class MenuActivity extends AppCompatActivity {

    TextView action_bar_title;
    ImageView imgStaff,imgStudent,imgParent,imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_activity);
        Hawk.put("currentFragment",false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        imgBack             =findViewById(R.id.imgBack);
        imgStaff            =findViewById(R.id.imgStaff);
        imgStudent          =findViewById(R.id.imgStudent);
        imgParent           =findViewById(R.id.imgParent);
        action_bar_title    =findViewById(R.id.action_bar_title);

        action_bar_title.setText(getString(R.string.SCHOOL));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("userType","staff");
                Intent i = new Intent(MenuActivity.this, LogInActivity.class);
                startActivity(i);


            }
        });
        imgStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("userType","student");
                Intent i = new Intent(MenuActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
        imgParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("userType","parent");
                Intent i = new Intent(MenuActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }
}
