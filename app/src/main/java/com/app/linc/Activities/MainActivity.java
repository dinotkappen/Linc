package com.app.linc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.Fragment.AddMembersFragment;
import com.app.linc.Fragment.ChangePasswordFragment;
import com.app.linc.Fragment.ChatFragment;
import com.app.linc.Fragment.HomeFragment;
import com.app.linc.Fragment.NoticeBoardFragment;
import com.app.linc.Fragment.NoticreBoardDetailsFragment;
import com.app.linc.Fragment.PrivacyFragment;
import com.app.linc.Fragment.ProfileFragment;
import com.app.linc.Fragment.SchoolCalendarFragment;
import com.app.linc.Fragment.SchoolInformationFragment;
import com.app.linc.Fragment.SendMessageFragment;
import com.app.linc.Fragment.SettingsFragment;
import com.app.linc.Fragment.UserProfileFragment;
import com.app.linc.Other.NetworkChangeReceiver;
import com.app.linc.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import static com.app.linc.Activities.NoInternetActivity.closeActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    String userType, profileImgUrl;
    ImageView profile_image_header;
    NavigationView navigationViewMenu;
    public static TextView nav_title;
    public static MainActivity mainActivity;
    public static ImageView nav_menu, nav_back;
    public static BottomNavigationView navigationBottom;
    static Boolean flagNoInternetActivity = false;
    boolean doubleBackToExitPressedOnce = false;
    String userName;
    String Name;
    private NetworkChangeReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);


        nav_menu = findViewById(R.id.nav_menu);
        nav_back = findViewById(R.id.nav_back);
        nav_title = findViewById(R.id.nav_title);
        drawer = findViewById(R.id.drawer_layout);
        navigationViewMenu = findViewById(R.id.navViewSideMenu);
        navigationBottom = findViewById(R.id.navigation_bottom);

        navigationBottom.setOnNavigationItemSelectedListener(this);
        navigationViewMenu.setNavigationItemSelectedListener(this);
        mainActivity = this;

        profile_image_header = (ImageView) navigationViewMenu.findViewById(R.id.profile_image_header);
        TextView txtSchoolName = (TextView) navigationViewMenu.findViewById(R.id.txtSchoolNameHeader);


//        View v = navigationBottom.getChildAt(2);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
//        View badge = LayoutInflater.from(this)
//                .inflate(R.layout.badge, itemView, true);

        userType = Hawk.get("userType", userType);

        profileImgUrl = Hawk.get("profileImgUrl", profileImgUrl);
        Name = Hawk.get("Name", Name);
        if (Name != null && !Name.isEmpty() && !Name.equals("null")) {
            try {
                txtSchoolName.setText(Name);
            } catch (Exception ex) {
                String msg = ex.getMessage().toString();
                Log.v("msgText", msg);
            }
        }
//        if (profileImgUrl != null && !profileImgUrl.isEmpty() && !profileImgUrl.equals("null"))
//        {
//            try {
//
//                Glide.with(this)
//                        .load(profileImgUrl)
//                        .listener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                                return false;
//                            }
//                        })
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(profile_image_header);
//            }
//            catch (Exception ex)
//            {
//                String msg=ex.getMessage().toString();
//            }
//        }


        nav_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle();

            }
        });
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        //String user_type = i.getStringExtra("user_type");
        String group_id = i.getStringExtra("group_id");
        if (group_id != null && !group_id.isEmpty() && !group_id.equals("null")){
            Hawk.put("chatGroupId",Integer.parseInt(group_id));
            loadFragment(new SendMessageFragment());
        }else{
            loadFragment(new HomeFragment());
        }

       // loadFragment(new HomeFragment());


        Hawk.put("acitivityStatus", true);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof ChatFragment) {
            Log.v("FragmentVisibility", "your Fragment is Visible");
        } else {
            Log.v("FragmentVisibility", "Not Visible");
        }


    }

    public static boolean isAppIsInBackground(Context context) {
        Boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void titlHide(String title) {

        if (title.equals("null")) {
            nav_title.setVisibility(View.GONE);

        } else {
            nav_title.setText(title);
            nav_title.setVisibility(View.VISIBLE);

        }

    }

    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        try {
            switch (item.getItemId()) {

                //*******   SIDE MENU   ***************

                case R.id.navigation_home_side:
                    fragment = new HomeFragment();
                    Hawk.put("currentFragment",false);


                    break;

                case R.id.nav_profile:
                    Hawk.put("currentFragment",false);
                    if (userType.equals("student")) {
                        fragment = new ProfileFragment();
                    } else {
                        fragment = new UserProfileFragment();
                    }


                    break;
                case R.id.nav_Notice_Board:
                    Hawk.put("currentFragment",false);
                    fragment = new NoticeBoardFragment();


                    break;


                case R.id.nav_school_calendar:
                    Hawk.put("currentFragment",false);
                    fragment = new SchoolCalendarFragment();


                    break;
                case R.id.nav_School_Information:
                    Hawk.put("currentFragment",false);
                    fragment = new SchoolInformationFragment();


                    break;
                case R.id.nav_settings:
                    Hawk.put("currentFragment",false);
                    fragment = new SettingsFragment();

                    break;


                case R.id.nav_logout:

                    logOut();

                    break;
                case R.id.nav_Privacy:
                    Hawk.put("currentFragment",false);
                    fragment = new PrivacyFragment();

                    break;


                //******    BOTTOM MENU *****************


                case R.id.bottom_navigation_home:
                    fragment = new HomeFragment();
                    Hawk.put("currentFragment",false);

                    break;

                case R.id.bottom_navigation_profile:
                    Hawk.put("currentFragment",false);
                    if (userType.equals("student")) {
                        fragment = new ProfileFragment();
                    } else {
                        fragment = new UserProfileFragment();
                    }


                    break;
                case R.id.bottom_navigation_chat:
                    Hawk.put("currentFragment",false);
                    fragment = new ChatFragment();
                    break;

                case R.id.bottom_navigation_calendar:
                    Hawk.put("currentFragment",false);
                    fragment = new SchoolCalendarFragment();


                    break;


            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            String h = msg;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        try {
            //switching fragment
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_loadFragment", msg);
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        Fragment fragment_back = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (fragment_back instanceof ProfileFragment) {
                backMain();
                String hj = "lk";

            }
            if (fragment_back instanceof SchoolCalendarFragment) {
                backMain();
            }
            if (fragment_back instanceof ChatFragment) {
                backMain();
            }
            if (fragment_back instanceof AddMembersFragment) {
                getSupportFragmentManager().popBackStack();
            }
            if (fragment_back instanceof SendMessageFragment) {
                getSupportFragmentManager().popBackStack();
            }
            if (fragment_back instanceof NoticeBoardFragment) {
                backMain();
            }
            if (fragment_back instanceof NoticreBoardDetailsFragment) {
                getSupportFragmentManager().popBackStack();
            }
            if (fragment_back instanceof SchoolInformationFragment) {
                backMain();
            }
            if (fragment_back instanceof SettingsFragment) {
                backMain();
            }
            if (fragment_back instanceof PrivacyFragment) {
                backMain();
            }
            if (fragment_back instanceof ChangePasswordFragment) {
                getSupportFragmentManager().popBackStack();
            } else if (fragment_back instanceof HomeFragment) {

                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                            moveTaskToBack(true);
                        }
                    }, 2000);
                } else {
                    super.onBackPressed();
                    return;
                }
            }

        }

    }

    public void backMain() {
        Fragment fragment1 = new HomeFragment();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container, fragment1).
                addToBackStack(null).
                commit();
    }

    public static void no_internet(boolean status, Context context) {

        if (status) {
            if (flagNoInternetActivity == true) {
                flagNoInternetActivity = false;
                closeActivity();

            }

        } else {
            if (flagNoInternetActivity != true) {
                Intent intent = new Intent(context, NoInternetActivity.class);
                context.startActivity(intent);
                flagNoInternetActivity = true;
            }
        }


    }

    public static void logOut() {
        Intent intent = new Intent(mainActivity, SchoolSelectionActivity.class);
        Hawk.put("logInSwitchState", false);
        Hawk.put("userToken", "");
        Hawk.put("user_id", "");
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Hawk.put("acitivityStatus", true);
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Hawk.put("acitivityStatus", true);
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Hawk.put("acitivityStatus", false);
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Hawk.put("acitivityStatus", false);
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        Hawk.put("acitivityStatus", true);
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Hawk.put("acitivityStatus", false);
        Log.d("lifecycle","onDestroy invoked");
    }
}
