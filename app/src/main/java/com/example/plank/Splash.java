package com.example.plank;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Splash extends Activity {
    public SharedPreferences preferences, pref;
    private String date;
    private SimpleDateFormat sdf;
    boolean danger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        pref = getSharedPreferences("PrefTest", 0);
        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        danger = false;

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable {
        public void run() {

            boolean stop = preferences.getBoolean("stop", true);
            if (stop) {
                checkFirstRun();
//                preferences.edit().putBoolean("stop", false).apply();
            } else {
                one();
                // dbUpdate();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("경고",danger);
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
//            preferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    public void one() {
        String today = sdf.format(new Date());
        danger = false;

        date = sdf.format(new Date());
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase dbRead = helper.getReadableDatabase();
        String sql = "select * from HistoryTable";
        Cursor c = dbRead.rawQuery(sql, null);

        c.moveToLast();

        int dateData_pos = c.getColumnIndex("dateData");
        int intData_pos = c.getColumnIndex("intData");
        int intData2_pos = c.getColumnIndex("intData2");
        int ClearData_pos = c.getColumnIndex("ClearData");

        String dateData = c.getString(dateData_pos);
        int intData = c.getInt(intData_pos);
        int intData2 = c.getInt(intData2_pos);
        Boolean Clear = (c.getInt(ClearData_pos) == 0);

        String yester = Integer.toString(intData2);

        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);
        String start = year + "-" + month + "-" + day;
        long diffiDay = 0;
        if (!dateData.equals(today)) {
            try {
                Date lastDate = sdf.parse(dateData);
                Date todayDate = sdf.parse(today);
                diffiDay = (todayDate.getTime() - lastDate.getTime() ) / (24 * 60 * 60 * 1000);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (Clear == true) {
            yester = Integer.toString(intData2 + 1);
        }

        SharedPreferences.Editor edit = pref.edit();
        if(diffiDay >= 3){
            danger = true;
        }
        String []last = date.split("-");

        int lastYear = Integer.parseInt(last[0]);
        int lastMonth = Integer.parseInt(last[1]) - 1;
        int lastDay = Integer.parseInt(last[2]);

        Calendar cal = Calendar.getInstance();

        if (!today.equals(dateData)) {
            int dif = (int)diffiDay;
            int j = 0;
            for (int i = 0; i < (int)diffiDay; i++) {
                j++;
                dif--;
                cal.set(lastYear, lastMonth, lastDay);

                cal.add(Calendar.DATE,dif * -1);

                date = sdf.format(cal.getTime());
                String dDay = Integer.toString(intData+j);
                String sql2 = "insert into HistoryTable (memoData, intData, intData2, dateData) values(?,?,?,?)";
                String[] arg1 = {null, dDay, yester, date};
                dbRead.execSQL(sql2, arg1);

            }
            dbRead.close();
        } else if (start.equals(dateData)) {
            dbRead.close();
        }
    }

}