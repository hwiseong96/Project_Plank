package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
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
    TextView textView;
    long counter = 10000;
    Button start;
    long milliLeft, sec;
    int count = 0;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
//
//        final Toolbar mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.count);
        circularProgressBar = findViewById(R.id.circle);
        circularProgressBar.CircularProgressBar(10);

        start = findViewById(R.id.start);
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (spr == 1) {
//                    timerStart(counter);
//                    spr = 2;
//                    start.setBackgroundResource(R.drawable.group);
//                } else if(spr == 2){
//                    timerPause();
//                }else if(spr == 3){
//                    timerResume();;
//                }
//
//            }
//        });

    }
    public void btnMethod(View view){

        switch (view.getId()){
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
    public void timerStart(long timeLengthMilli){
        counter = timeLengthMilli;
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long l) {

                if(count == 10){
                    off();
                }

                milliLeft = l;
                sec = (l/1000);
                milliLeft = l;
                int a = 10;
                milliLeft = l;
                a -= count;
                textView.setText(a + "초");
                milliLeft = l;
                count++;
                circularProgressBar.setProgress(count);
                sec--;
                milliLeft = l;

            }

            @Override
            public void onFinish() {
                setResult(RESULT_OK);
                finish();
            }
        }.start();
    }


    public void timerPause(){
        countDownTimer.cancel();
        start.setBackgroundResource(R.drawable.pause_3x);
    }
    private void timerResume(){
        timerStart(milliLeft);
        start.setBackgroundResource(R.drawable.group);
    }
    public void off(){
        timerPause();
        setResult(RESULT_OK);
        finish();

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

}