package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Difficulty extends AppCompatActivity {

    int year, month, day;
    private String date;
    private SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        Button button = findViewById(R.id.Beginner);

        button.setOnClickListener(new View.OnClickListener() {
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

                edit.commit();
                beginner();
                dbCreate();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                finish();

            }
        });

    }

    public void beginner() {

        DBHelper2 helper2 = new DBHelper2(this);
        SQLiteDatabase db2 = helper2.getWritableDatabase();

        String sql = "insert into DifficultTable (textData, textData2) values(?,?)";

        Beginner beginner = new Beginner();

        String[][] table = {beginner.arg1,
                beginner.arg2,
                beginner.arg3,
                beginner.arg4,
                beginner.arg5,
                beginner.arg6,
                beginner.arg7,
                beginner.arg8,
                beginner.arg9,
                beginner.arg10,
                beginner.arg11,
                beginner.arg12,
                beginner.arg13,
                beginner.arg14,
                beginner.arg15,
                beginner.arg16,
                beginner.arg17,
                beginner.arg18,
                beginner.arg19,
                beginner.arg20,
                beginner.arg21,
                beginner.arg22,
                beginner.arg23,
                beginner.arg24,
                beginner.arg25,
                beginner.arg26,
                beginner.arg27,
                beginner.arg28,
                beginner.arg29,
                beginner.arg30,
        };
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