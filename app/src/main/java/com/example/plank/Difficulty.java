package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Difficulty extends AppCompatActivity {
    public SharedPreferences preferences;
    int year, month, day;
    private String date;
    private SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        LinearLayout button1 = findViewById(R.id.Beginner);
        LinearLayout button2 = findViewById(R.id.Intermediate);
        LinearLayout button3 = findViewById(R.id.Advanced);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                SharedPreferences pref = getSharedPreferences("PrefTest", 0);
                SharedPreferences.Editor edit = pref.edit();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                edit.putInt("년", year);
                edit.putInt("월", month);
                edit.putInt("일", day);
                edit.putString("난이도", "초급");
                //edit.putInt("플랭크",1);
                sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
                date = sdf.format(new Date());

                Beginner beginner = new Beginner();
                edit.commit();
                Diffi diffi= new Diffi(beginner.args);
                diff(diffi);
                dbCreate();
                preferences = getSharedPreferences("Pref", MODE_PRIVATE);
                preferences.edit().putBoolean("stop", false).apply();
                preferences.edit().putBoolean("isFirstRun", false).apply();
                preferences.edit().putBoolean("isSecondRun",false).apply();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                finish();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Calendar calendar = Calendar.getInstance();
//
//                SharedPreferences pref = getSharedPreferences("PrefTest", 0);
//                SharedPreferences.Editor edit = pref.edit();
//
//                year = calendar.get(Calendar.YEAR);
//                month = calendar.get(Calendar.MONTH) + 1;
//                day = calendar.get(Calendar.DAY_OF_MONTH);
//                edit.putInt("년", year);
//                edit.putInt("월", month);
//                edit.putInt("일", day);
//                edit.putString("난이도", "중급");
//                //edit.putInt("플랭크",1);
//                sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
//                date = sdf.format(new Date());
//
//                edit.commit();
//                Intermediate intermediate = new Intermediate();
//                Diffi diffi= new Diffi(intermediate.args);
//                diff(diffi);
//                dbCreate();
//                preferences = getSharedPreferences("Pref", MODE_PRIVATE);
//                preferences.edit().putBoolean("stop", false).apply();
//                preferences.edit().putBoolean("isFirstRun", false).apply();
//                preferences.edit().putBoolean("isSecondRun",false).apply();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//
//                finish();
                Toast toast = Toast.makeText(Difficulty.this, "추후 업데이트 될 예정입니다!", Toast.LENGTH_SHORT);
                View view2 = toast.getView();
                view2.setBackgroundResource(R.drawable.blackbtn);
                TextView text = (TextView) view2.findViewById(android.R.id.message);
                text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                toast.show();

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Calendar calendar = Calendar.getInstance();
//
//                SharedPreferences pref = getSharedPreferences("PrefTest", 0);
//                SharedPreferences.Editor edit = pref.edit();
//
//                year = calendar.get(Calendar.YEAR);
//                month = calendar.get(Calendar.MONTH) + 1;
//                day = calendar.get(Calendar.DAY_OF_MONTH);
//                edit.putInt("년", year);
//                edit.putInt("월", month);
//                edit.putInt("일", day);
//                edit.putString("난이도", "고급");
//                //edit.putInt("플랭크",1);
//                sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
//                date = sdf.format(new Date());
//
//                edit.commit();
//                Advanced advanced = new Advanced();
//                Diffi diffi= new Diffi(advanced.args);
//                diff(diffi);
//                dbCreate();
//                preferences = getSharedPreferences("Pref", MODE_PRIVATE);
//                preferences.edit().putBoolean("isFirstRun", false).apply();
//                preferences.edit().putBoolean("stop", false).apply();
//                preferences.edit().putBoolean("isSecondRun",false).apply();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//
//                finish();
                Toast toast = Toast.makeText(Difficulty.this, "추후 업데이트 될 예정입니다!", Toast.LENGTH_SHORT);
                View view2 = toast.getView();
                view2.setBackgroundResource(R.drawable.blackbtn);
                TextView text = (TextView) view2.findViewById(android.R.id.message);
                text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                toast.show();

            }
        });

    }

    public void diff(Diffi obj) {

        DBHelper2 helper2 = new DBHelper2(this);
        SQLiteDatabase db2 = helper2.getWritableDatabase();

        String sql = "insert into DifficultTable (textData, textData2, timeData, timeData2) values(?,?,?,?)";
        String [][] table = obj.args;

        for(int i = 0; i < table.length; i++){
            db2.execSQL(sql,table[i]);
        }
        db2.close();
    }
    public void dbCreate(){

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "insert into HistoryTable (memoData, intData, intData2, dateData) values(?,?,?,?)";
        String[] arg1 = {null,"1","1", date};
        db.execSQL(sql, arg1);
        db.close();
    }


}