package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Splash extends Activity {
    public SharedPreferences preferences, pref;
    private String date;
    private SimpleDateFormat sdf;
    SharedPreferences.Editor edit;
    boolean a = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        pref = getSharedPreferences("PrefTest", 0);
        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable {
        public void run() {

            boolean stop = preferences.getBoolean("stop", true);
            if(stop){
                checkFirstRun();
                preferences.edit().putBoolean("stop", false).apply();
            }else{
                one();
               // dbUpdate();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            Splash.this.finish(); // 로딩페이지 Activity stack에서 제거

        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

    public void checkFirstRun() {

        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Intent intent = new Intent(this, guide.class);
            startActivity(intent);
            preferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    public void one() {
        String today = sdf.format(new Date());
//
//        if (!date.equals(today)) {

        date = sdf.format(new Date());
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase dbRead = helper.getReadableDatabase();
        String sql = "select * from HistoryTable";
        Cursor c = dbRead.rawQuery(sql, null);

        c.moveToLast();

        int dateData_pos = c.getColumnIndex("dateData");
        String dateData = c.getString(dateData_pos);
        int intData_pos = c.getColumnIndex("intData");
        int intData = c.getInt(intData_pos);
        String dDay = Integer.toString(intData + 1);

        int year = pref.getInt("년",0);
        int month = pref.getInt("월",0);
        int day = pref.getInt("일",0);
        String start = year +"-" + month + "-" +day;
        if(!today.equals(dateData)){
            String sql2 = "insert into HistoryTable (memoData, intData, dateData) values(?,?,?)";
            String[] arg1 = {null, dDay, date};
            dbRead.execSQL(sql2, arg1);
            dbRead.close();
        }else if(start.equals(dateData)){
            Log.d("EJfk","wha");
            dbRead.close();
        }
    }

}