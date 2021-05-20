package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class secondTimer extends AppCompatActivity {
    private CircularProgressBar circularProgressBar;
    private TextView textView, textView2;
    private long counter;
    private Button start, btnStop, notStop, comeback;
    private long milliLeft, sec;
    private int count = 0;
    private int time, second, val;
    private String plank;
    private MainActivity activity;
    private CountDownTimer countDownTimer;
    private SQLiteDatabase db;
    private int intData;
    private boolean stop;
    private Dialog dialog;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        DBHelper helper = new DBHelper(getApplicationContext());
        db = helper.getReadableDatabase();

        String sql = "select * from HistoryTable";

        Cursor c = db.rawQuery(sql, null);
        c.moveToLast();

        int intData_pos = c.getColumnIndex("intData");
        intData = c.getInt(intData_pos);

        time = getIntent().getIntExtra("time", 999);
        plank = getIntent().getStringExtra("name");
        val = getIntent().getIntExtra("val", 0);

        textView2 = findViewById(R.id.name);
        textView2.setText(plank);

        textView = findViewById(R.id.count);
        textView.setText(time + "초");

        circularProgressBar = findViewById(R.id.circle);
        circularProgressBar.mMaxProgress = time;
        //circularProgressBar.CircularProgressBar(time);
        counter = time * 1000;
        second = time;
        start = findViewById(R.id.start);
        stop = true;

        timerStart(counter);
        start.setTag("pause");

    }

    public void btnMethod(View view) {

        switch (view.getId()) {
            case R.id.start:
                if(start.getTag().equals("start")){
                    timerStart(counter);
                    start.setTag("pause");
                }else if(start.getTag().equals("pause")){
                    timerPause();
                    stop = false;
                    start.setTag("resume");
                }else if(start.getTag().equals("resume")){
                    timerResume();
                    stop = true;
                    start.setTag("pause");
                }
                break;
//            case R.id.pause:
//                timerPause();
//                stop = false;
//                start.setId(R.id.resume);
//                break;
//            case R.id.resume:
//                timerResume();
//                stop = true;
//                start.setId(R.id.pause);
//                break;
            case R.id.back:
                if (countDownTimer == null) {

                } else {
                    countDownTimer.cancel();
                    milliLeft += 10000;
                    second += 10;
                    count -= 10;
                    if (milliLeft >= (time * 1000)) {
                        milliLeft = time * 1000;
                        second = time;
                        count = 0;
                    }
                    back();
                }
                break;
            case R.id.forward:

                if (countDownTimer == null) {

                } else {
                    countDownTimer.cancel();
                    milliLeft -= 10000;
                    second -= 10;
                    count += 10;
                    if (milliLeft <= 0) {
                        milliLeft = 0;
                        second = 0;
                        count = time;
                    }
                    back();
                }
                break;

        }
    }

    public void timerStart(long timeLengthMilli) {
        counter = timeLengthMilli;
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long l) {

                if (count == time) {
                    off();
                }
                milliLeft = l;
                sec = (l / 1000);
                milliLeft = l;
                second -= 1;
                if (second < 0) {
                    second = 0;
                }
                milliLeft = l;
                textView.setText(second + "초");
                milliLeft = l;
                count++;
                milliLeft = l;
                circularProgressBar.setProgress(count);
                milliLeft = l;
                sec--;
                milliLeft = l;

            }

            @Override
            public void onFinish() {
                //setResult(RESULT_OK);
                result();
                finish();
            }
        }.start();
    }


    public void timerPause() {
        countDownTimer.cancel();
        start.setBackgroundResource(R.drawable.play_3x);
    }

    private void timerResume() {
        timerStart(milliLeft);
        start.setBackgroundResource(R.drawable.pause_3x);
    }

    private void back() {

        textView.setText(second + "초");
        circularProgressBar.setProgress(count);
        if (stop) {
            timerStart(milliLeft);
        }
    }

    public void off() {
        timerPause();
        //setResult(RESULT_OK);
        result();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                backbtn();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void result() {
        switch (val) {
            case 1:
                //now.setTag("conti");
                //now.setText(pro + "일차 운동 계속하기");
                String sql2 = "update HistoryTable set boolData = 0 where intData = " + intData;
                db.execSQL(sql2);
                Intent intent = getIntent();
                intent.putExtra("p2", getIntent().getStringExtra("name3"));
                intent.putExtra("t2", getIntent().getIntExtra("time4", 0));
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(0, intent);
                break;

            case 2:
                //now.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.planksuccess));
                //now.setTag("succ");
                //now.setText(pro + "일차 운동 성공! 내일 또 봐요");
                //now.setEnabled(false);
                String sql3 = "update HistoryTable set boolData2 = 0, ClearData = 0 where intData = " + intData;
                db.execSQL(sql3);
                setResult(1);
                finish();
                break;

            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        backbtn();

        return;
    }
    public void backbtn(){
        if (countDownTimer != null) {
            timerPause();
            start.setTag("resume");
        }
        dialog = new Dialog(secondTimer.this, R.style.Dialog);
        dialog.setContentView(R.layout.modal_stop);
        btnStop = dialog.findViewById(R.id.btnStop);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3);
                finish();
                dialog.dismiss();
            }
        });

        notStop = dialog.findViewById(R.id.notStop);
        notStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        comeback = dialog.findViewById(R.id.comeback);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast = Toast.makeText(getApplicationContext(), "30분 후에 알림을 보내드릴게요!", Toast.LENGTH_SHORT);
                View view2 = toast.getView();
                view2.setBackgroundResource(R.drawable.blackbtn);
                TextView text = (TextView) view2.findViewById(android.R.id.message);
                text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                toast.show();

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("30",30);
                PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); //PendingIntent.FLAG_UPDATE_CURRENT
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 30);
                setResult(3);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
                dialog.dismiss();
                finish();
            }
        });
    }


}