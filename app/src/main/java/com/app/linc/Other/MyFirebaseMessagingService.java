package com.app.linc.Other;



import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.linc.Activities.MainActivity;

import com.app.linc.Fragment.ChatFragment;
import com.app.linc.Model.Chat.ChatHistory.ChatHistory;
import com.app.linc.Model.Chat.ChatHistory.Chat_;
import com.app.linc.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


import static com.app.linc.Activities.MainActivity.isAppIsInBackground;
import static com.app.linc.Activities.MainActivity.mainActivity;
import static com.app.linc.Fragment.SendMessageFragment.getNotificationMsg;
import static com.app.linc.Fragment.SendMessageFragment.grp_id;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private static final String TAG = "MyGcmListenerService";
    private NotificationManager notifManager;
    private NotificationChannel mChannel;
    private int numMessages = 0;
    int NOTIFICATION_ID = 1;
    String type = "";
    MainActivity yourActivity = new MainActivity();
    String ut;
    boolean acitivityStatus;
    boolean  currentFragment;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

            if (remoteMessage.getData().size() > 0) {
                //handle the data message here

                Map<String, String> data = remoteMessage.getData();

                String title = data.get("title");
                Hawk.put("chatGroupName",title);
                Log.v("titleNOT",title);

                String body = data.get("body");
                Log.v("bodyNOT",body);
                Log.v("dataaaaaaaaaaaaa",data.get("data").toString());
                Object dt = data.get("data");
                try {
                    JSONObject containerObject = new JSONObject(data.get("data").toString());
                    if (containerObject.has("chat")) {
                        type = "chat";
                        JSONObject objData = new JSONObject(containerObject.get("chat").toString());
                        Log.v("objData",""+objData);
                        String name =objData.getString("name");
                        String message=objData.getString("message");
                        String userType=objData.getString("user_type");
                        String groupId=objData.getString("group_id");

                        try {
                            currentFragment=Hawk.get("currentFragment",false);
                            acitivityStatus = Hawk.get("acitivityStatus", acitivityStatus);

                            Log.v("acitivityStatus", "" + acitivityStatus);
                        }catch (Exception ex)
                        {
                            acitivityStatus=false;
                        }
                        if (acitivityStatus == true) {
                                Log.v("acitivityStatus", "Yes" + acitivityStatus);
                                if (currentFragment == true) {
                                    Log.v("currentFragment", "Yes" + currentFragment);
                                    ChatHistory chatHistoryModel=new ChatHistory();
                                    chatHistoryModel.setName(name);
                                    chatHistoryModel.setUserType(userType);
                                    Chat_ chatModel=new Chat_();
                                    chatModel.setMessage(message);
                                    chatHistoryModel.setChat(chatModel);
                                    //getNotificationMsg(chatHistoryModel);

                                    yourActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try{
                                                //adapter.notifyDataSetChanged();
                                                if(grp_id == Integer.parseInt(groupId) ) {
                                                    getNotificationMsg(chatHistoryModel);
                                                }else{
                                                    String user_type = userType;
                                                    sendNotification(title, body, user_type,groupId, type);
                                                }
                                                Log.v("xxxxxxxxxxxxxxxxxxxxxx",""+grp_id);
                                            }
                                            catch(Exception e){

                                                Log.v("xxxxxxxxxxxxxxxxxxxxxx",e.getMessage());
                                            }
                                        }
                                    });
                                } else {
                                    Log.v("currentFragment", "No" + currentFragment);
                                    String user_type = userType;
                                    sendNotification(title, body, user_type,groupId, type);
                                }
                        } else {
                                Log.v("acitivityStatus", "No" + acitivityStatus);
                            String user_type = userType;
                            sendNotification(title, body, user_type,groupId, type);
                        }


                        /*new MainActivity().runOnUiThread(new Runnable() {

                            public void run() {

                                //ChatFragment.getNotificationMsg(getApplicationContext(),chatHistoryModel);
                                //System.out.println("yay");
                                getNotificationMsg(chatHistoryModel);

                            }
                        });*/

                        // sendNotification(title, body, id, type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* if (dt.has("chat")) {
                    Object objData = dt.get("chat");
                    Log.v("objData",""+objData);

                    type = "order";
                   // sendNotification(title, body, id, type);
                } else if (data.containsKey("product_id")) {
                    String id = data.get("product_id");
                    type = "product";
                    sendNotification(title, body, id, type);
                }
                else
                {
                    String id = "";
                    sendNotification(title, body, id, type);
                }*/
                //   Log.d("MyNotification", remoteMessage.getData().toString());
            }

    }


    private void sendNotification(String title, String body,String user_type,String id,String type) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationId", NOTIFICATION_ID);

        try {
            ut=Hawk.get("userType","");

        }catch (Exception ex)
        {
            ut="";
        }
        intent.setClass(this, MainActivity.class);
        if (type.equals("")) {
            intent.putExtra("user_type","");
            intent.putExtra("group_id","");

        } else if (type.equals("chat")) {
            intent.putExtra("user_type",ut);
            intent.putExtra("group_id",id);


        } else if (type.equals("product")) {
            intent.putExtra("orderDetailsFragment",type);
            intent.putExtra("orderDetailsFragmentID",id);


        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService

                    (Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                NotificationChannel mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setDescription(body);

                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            builder.setContentTitle(title)  // flare_icon_30

                    .setSmallIcon(R.drawable.launcher) // required

                    .setContentText(body)  // required
                    .setShowWhen(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLights(Color.RED, 1000, 300)
                    .setNumber(++numMessages)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.drawable.launcher))
                    .setBadgeIconType(R.drawable.ic_phone)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            builder.setDefaults(defaults);
        } else {

            builder = new NotificationCompat.Builder(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle(title)  // flare_icon_30

                    .setSmallIcon(R.drawable.launcher) // required
                    .setContentText(body)  // required
                    .setShowWhen(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLights(Color.RED, 1000, 300)
                    .setNumber(++numMessages)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.drawable.launcher))
                    .setBadgeIconType(R.drawable.launcher)
                    .setContentIntent(pendingIntent);

            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            builder.setDefaults(defaults);
            builder.setAutoCancel(true);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification notification = builder.build();
        notifManager.notify(0, notification);
    }
}