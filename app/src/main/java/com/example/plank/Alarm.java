package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Alarm extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private TimePicker timePicker;
    private AlarmManager alarmManager;
    private int hour, minute;
    private Dialog dialog;
    private CheckBox cbSun, cbMon, cbTue, cbWed, cbThu, cbFri, cbSat;
    private Button complete, back, picker, ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.appbar).bringToFront();

        //timePicker = findViewById(R.id.picker);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        cbSun = findViewById(R.id.sun);
        cbMon = findViewById(R.id.mon);
        cbTue = findViewById(R.id.tue);
        cbWed = findViewById(R.id.wed);
        cbThu = findViewById(R.id.thu);
        cbFri = findViewById(R.id.fri);
        cbSat = findViewById(R.id.sat);

        cbSun.setOnCheckedChangeListener(this);
        cbMon.setOnCheckedChangeListener(this);
        cbTue.setOnCheckedChangeListener(this);
        cbWed.setOnCheckedChangeListener(this);
        cbThu.setOnCheckedChangeListener(this);
        cbFri.setOnCheckedChangeListener(this);
        cbSat.setOnCheckedChangeListener(this);

        picker = findViewById(R.id.picker);
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("hh : mm a", new Locale("en", "US"));
        String getTime = simpleDate.format(mDate);

        picker.setText(getTime);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(Alarm.this, R.style.Dialog);
                dialog.setContentView(R.layout.modal_timepicker);
                timePicker = dialog.findViewById(R.id.picker);
                ok = dialog.findViewById(R.id.b1);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            Toast.makeText(getApplicationContext(), "버전을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (hour > 12) {
                            hour -= 12;
                            state = "PM";
                        }
                        picker.setText(hour +" : "+minute+" "+state);
                        dialog.dismiss();
                    }
                });

            }
        });

        complete = findViewById(R.id.completion);
        back = findViewById(R.id.itsok);
        final float scale = getResources().getDisplayMetrics().density;

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist(view);
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
        boolean[] week = {false, cbSun.isChecked(), cbMon.isChecked(), cbTue.isChecked(), cbWed.isChecked(),
                cbThu.isChecked(), cbFri.isChecked(), cbSat.isChecked()};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hour = timePicker.getHour();
//            minute = timePicker.getMinute();
//        } else {
//            Toast.makeText(this, "버전을 확인해 주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (!cbSun.isChecked() && !cbMon.isChecked() && !cbTue.isChecked() && !cbWed.isChecked() && !cbThu.isChecked() && !cbFri.isChecked() && !cbSat.isChecked()) {
            Toast.makeText(this, "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("weekday", week);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); //PendingIntent.FLAG_UPDATE_CURRENT

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Log.d("asdf2", hour + "" + minute);

            // 지정한 시간에 매일 알림
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pIntent);
            finish();
        }
    }

    public void unregist(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (cbSun.isChecked()) {
            cbSun.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbSun.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbMon.isChecked()) {
            cbMon.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbMon.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbTue.isChecked()) {
            cbTue.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbTue.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbWed.isChecked()) {
            cbWed.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbWed.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbThu.isChecked()) {
            cbThu.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbThu.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbFri.isChecked()) {
            cbFri.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbFri.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
        if (cbSat.isChecked()) {
            cbSat.setTextColor(ContextCompat.getColor(this, R.color.White));
        } else {
            cbSat.setTextColor(ContextCompat.getColor(this, R.color.Black));
        }
    }
}