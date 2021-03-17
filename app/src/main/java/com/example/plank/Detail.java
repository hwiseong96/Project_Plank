package com.example.plank;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Detail extends AppCompatActivity {

    private Button b[] = new Button[30];
    private Button button, yes, no;
    private TextView textView, textView2;
    private Calendar calendar, dCalendar, dDaycalendar;
    private int resultNumber = 0;
    private long diffDay;
    int resetY, resetM, resetD;
    int pro;
    private Dialog dialog, bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressBar progressBar = findViewById(R.id.probar2);
        progressBar.setIndeterminate(false);

        createButton();
        pro = dDay();

        //달성버튼
        for(int i = 0 ; i < pro; i++){

            b[i].setBackgroundColor(R.drawable.now);
            b[i].setBackgroundResource(R.drawable.ic_coolicon);

        }

        textView = findViewById(R.id.nday);
        textView2 = findViewById(R.id.achiev);

        textView.setText((diffDay + 1) + "일차 도전 중");
        progressBar.setProgress(pro);

        b[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Detail.this);
                bottomSheetDialog.setContentView(R.layout.bottom_modal);
                bottomSheetDialog.show();
            }
        });

        button = findViewById(R.id.reset);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog = new Dialog(Detail.this, R.style.Dialog);

                dialog.setContentView(R.layout.modal);
                yes = dialog.findViewById(R.id.btnReset);
                dialog.show();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dCalendar.add(Calendar.DAY_OF_MONTH, 30);
                        Calendar calendar = Calendar.getInstance();


                        SharedPreferences pref = getSharedPreferences("PrefTest",0);
                        SharedPreferences.Editor edit = pref.edit();

                        resetY = calendar.get(Calendar.YEAR);
                        resetM = calendar.get(Calendar.MONTH) + 1;
                        resetD = calendar.get(Calendar.DAY_OF_MONTH);

                        edit.putInt("년",resetY);
                        edit.putInt("월",resetM);
                        edit.putInt("일",resetD);
                        edit.commit();
                        dialog.dismiss();

                    }
                });

                no = dialog.findViewById(R.id.notReset);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });


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
    public void createButton(){

        for (int i = 0; i < 30; i++) {

            b[i] = findViewById(getResources().getIdentifier("btn" + (i + 1), "id", getPackageName()));
            b[i].setBackgroundResource(R.drawable.item);
            b[i].setText((i + 1) + "일차");

        }

    }
    public int dDay(){
        //오늘
        calendar = Calendar.getInstance();

        SharedPreferences pref = getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("년",0);
        int month = pref.getInt("월",0);
        int day = pref.getInt("일",0);
        int preMonth = calendar.get(calendar.MONTH)+1;

        //시작날
        dCalendar = new GregorianCalendar(year,month,day);

        //오늘
        dDaycalendar = new GregorianCalendar(calendar.get(calendar.YEAR),preMonth,calendar.get(calendar.DAY_OF_MONTH));
        // 오늘 - 시작날 차이구하기
        long diffSec = (dDaycalendar.getTimeInMillis()- dCalendar.getTimeInMillis())/1000;
        diffDay = diffSec /  (24 * 60 * 60);


        return ((int)diffDay+1);
    }




}
