package com.example.plank;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    Intent intent1;
    Notification.Builder builder;
    NotificationManager notificationManager;
    boolean[] week;
    int minute;

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context);
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        //intent = new Intent(context.getApplicationContext(), MainActivity.class);

        week = intent.getBooleanArrayExtra("weekday");
        minute = intent.getIntExtra("30",0);
        Calendar calendar = Calendar.getInstance();

        if (week != null && !week[calendar.get(Calendar.DAY_OF_WEEK)]) {
            Toast.makeText(context, "asdf", Toast.LENGTH_SHORT).show();
            return;
        } else {
            alarm(context);
        }
        if(minute == 30){
            alarm(context);
        }
    }

    public void alarm(Context context) {
        Toast.makeText(context, "알람~!!", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        intent1 = new Intent(context, Splash.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //앞서 생성한 작업 내용을 Notification 객체에 담기 위한 PendingIntent 객체 생성
        //PendingIntent pendnoti = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        //푸시 알림에 대한 각종 설정
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setNumber(1)
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true);


        //NotificationManager를 이용하여 푸시 알림 보내기
        notificationManager.notify(1, notificationBuilder.build());
    }

    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);

        } else {
            builder = new Notification.Builder(context);
        }
    }


}
