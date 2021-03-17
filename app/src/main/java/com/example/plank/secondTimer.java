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
    int spr = 1;
    int count = 0;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.count);
        circularProgressBar = findViewById(R.id.circle);
        start = findViewById(R.id.start);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spr == 1) {
                    timerStart(counter);
                } else if(spr == 2){
                    timerPause();
                }else if(spr == 3){
                    timerResume();;
                }

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
    public void timerStart(long timeLengthMilli){
        counter = timeLengthMilli;
        spr = 2;
        start.setBackgroundResource(R.drawable.group);
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long l) {

                milliLeft = l;
                sec = (l/1000);
                textView.setText(sec + "초");
                count++;
                circularProgressBar.setProgress(count);
                sec--;

            }

            @Override
            public void onFinish() {
                textView.setText("0초");
            }
        }.start();
    }


    public void timerPause(){
        start.setBackgroundResource(R.drawable.pause_3x);
        spr = 3;
        countDownTimer.cancel();
    }
    private void timerResume(){
        start.setBackgroundResource(R.drawable.group);
        spr = 2;
        timerStart(milliLeft);
    }

}