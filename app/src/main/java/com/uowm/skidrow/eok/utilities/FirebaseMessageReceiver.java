package com.uowm.skidrow.eok.utilities;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uowm.skidrow.eok.MainActivity;
import com.uowm.skidrow.eok.R;
import com.uowm.skidrow.eok.activities.MessageActivityActivity;
import com.uowm.skidrow.eok.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(isAppIsInBackground(getApplicationContext())) {
            //handle when receive notification via data event
            if (remoteMessage.getData().size() > 0) {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),false);
                EventBus.getDefault().postSticky(new MessageEvent(remoteMessage.getData().get("title"), remoteMessage.getData().get("message")));
            }

            //handle when receive notification
            if (remoteMessage.getNotification() != null) {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),false);
                EventBus.getDefault().postSticky(new MessageEvent(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()));
            }
        }else {
            if (remoteMessage.getData().size() > 0) {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),true);
                EventBus.getDefault().postSticky(new MessageEvent(remoteMessage.getData().get("title"), remoteMessage.getData().get("message")));
            }

            //handle when receive notification
            if (remoteMessage.getNotification() != null) {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),true);
                EventBus.getDefault().postSticky(new MessageEvent(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()));
            }
        }

    }


    public void showNotification(String title,String message,Boolean setOnlyAlertOnce){
        String[] splitInTwo = title.split("/");
        Intent intent;
        if(splitInTwo[1].equals("0")) {
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, MessageActivityActivity.class);
            intent.putExtra("id", Integer.parseInt(splitInTwo[1]));
            intent.putExtra("name_surname", splitInTwo[0]);
        }
        String channel_id="web_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        System.out.println(title);
        System.out.println(message);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.eiko)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000})
                .setOnlyAlertOnce(setOnlyAlertOnce)
                .setContentIntent(pendingIntent);

        builder = builder.setContentTitle(splitInTwo[0])
                        .setContentText(message);


        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(Integer.parseInt(splitInTwo[1]),builder.build());
    }

    //app part ready now let see how to send differnet users
    //like send to specific device
    //like specifi topic

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;

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
        }
        else
        {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
