package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class Alarm extends AppCompatActivity {
    private TimePicker timePicker;
    private AlarmManager alarmManager;
    private int hour, minute;
    CheckBox cbSun, cbMon, cbTue, cbWed, cbThu, cbFri, cbSat;
    Button complete, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = findViewById(R.id.picker);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        cbSun = findViewById(R.id.sun);
        cbMon = findViewById(R.id.mon);
        cbTue = findViewById(R.id.tue);
        cbWed = findViewById(R.id.wed);
        cbThu = findViewById(R.id.thu);
        cbFri = findViewById(R.id.fri);
        cbSat = findViewById(R.id.sat);

        complete = findViewById(R.id.completion);
        back = findViewById(R.id.itsok);


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regist(view);
                Toast.makeText(Alarm.this, "설정완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void regist(View view) {
        unregist(view);
        boolean[] week = { false, cbSun.isChecked(), cbMon.isChecked(), cbTue.isChecked(), cbWed.isChecked(),
                cbThu.isChecked(), cbFri.isChecked(), cbSat.isChecked() }; // cbSun을 1번부터 사용하기 위해 배열 0번은 false로 고정

        if(!cbSun.isChecked() &&  !cbMon.isChecked() &&  !cbTue.isChecked() && !cbWed.isChecked() &&  !cbThu.isChecked() && !cbFri.isChecked() && !cbSat.isChecked()){
            Toast.makeText(this, "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour=timePicker.getHour();
            minute=timePicker.getMinute();
        }else{
            Toast.makeText(this, "버전을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("weekday", week);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,intent, 0); //PendingIntent.FLAG_UPDATE_CURRENT

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        Date today = new Date();
        long intervalDay = 24 * 60 * 60 * 1000;// 24시간

        long selectTime=calendar.getTimeInMillis();
        long currenTime=System.currentTimeMillis();

        //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if(currenTime>selectTime){
            selectTime += intervalDay;
        }

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,  intervalDay, pIntent);

    }
    public void unregist(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }



}