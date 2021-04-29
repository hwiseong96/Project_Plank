package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

public class secondTimer extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView textView, textView2;
    long counter;
    Button start;
    long milliLeft, sec;
    int count = 0;
    int time, second, val;
    String plank;
    private MainActivity activity;
    CountDownTimer countDownTimer;
    private SQLiteDatabase db;
    private int intData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

//        final Toolbar mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper helper = new DBHelper(getApplicationContext());
        db = helper.getReadableDatabase();

        String sql = "select * from HistoryTable";

        Cursor c = db.rawQuery(sql, null);
        c.moveToLast();

        int intData2_pos = c.getColumnIndex("intData2");
        intData2 = c.getInt(intData2_pos);

        time = getIntent().getIntExtra("time", 999);
        plank = getIntent().getStringExtra("name");
        val = getIntent().getIntExtra("val", 0);

        textView2 = findViewById(R.id.name);
        textView2.setText(plank);

        textView = findViewById(R.id.count);
        textView.setText(time + "초");

        circularProgressBar = findViewById(R.id.circle);
        circularProgressBar.mMaxProgress = time;
        circularProgressBar.CircularProgressBar(time);
        counter = time * 1000;
        second = time;
        start = findViewById(R.id.start);

    }

    public void btnMethod(View view) {

        switch (view.getId()) {
            case R.id.start:
                timerStart(counter);
                start.setId(R.id.pause);
                break;
            case R.id.pause:
                timerPause();
                start.setId(R.id.resume);
                break;
            case R.id.resume:
                timerResume();
                start.setId(R.id.pause);
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
                if(second < 0){
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
        start.setBackgroundResource(R.drawable.pause_3x);
    }

    private void timerResume() {
        timerStart(milliLeft);
        start.setBackgroundResource(R.drawable.group);
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
                finish();
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
                String sql2 = "update HistoryTable set boolData = 0 where intData2 = " + intData2;
                db.execSQL(sql2);
                Intent intent = getIntent();
                intent.putExtra("p2",getIntent().getStringExtra("name3"));
                intent.putExtra("t2",getIntent().getIntExtra("time4",0));
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(0,intent);
                break;

            case 2:
                //now.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.planksuccess));
                //now.setTag("succ");
                //now.setText(pro + "일차 운동 성공! 내일 또 봐요");
                //now.setEnabled(false);
                String sql3 = "update HistoryTable set boolData2 = 0, ClearData = 0 where intData2 = " + intData2;
                db.execSQL(sql3);
                setResult(1);
                finish();
                break;

            default:
                Log.d("asdf","3");
                break;

        }
    }

}