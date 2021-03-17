package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Difficulty extends AppCompatActivity {

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        Button button = findViewById(R.id.Beginner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                SharedPreferences pref = getSharedPreferences("PrefTest",0);
                SharedPreferences.Editor edit = pref.edit();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                edit.putInt("년",year);
                edit.putInt("월",month);
                edit.putInt("일",day);
                edit.commit();

                finish();

            }
        });

    }

}