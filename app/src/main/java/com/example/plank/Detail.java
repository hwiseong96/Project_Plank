package com.example.plank;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    private Button b[] = new Button[30];
    private Button button, yes, no, ok;
    private TextView textView, textView2,textView3, tv1, tv2, tv3, tv4, tv5;
    private Calendar calendar, dCalendar, dDaycalendar;
    private int resultNumber = 0;
    int resetY, resetM, resetD;
    private long diffDay;
    int pro;
    private Dialog dialog;
    private String date;
    private SimpleDateFormat sdf;
    private SharedPreferences pref;
    private ArrayList<String> dataList;
    private String [][]table;
    private Diffi diffi;
    private float per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.appbar).bringToFront();

        ProgressBar progressBar = findViewById(R.id.probar2);
        progressBar.setIndeterminate(false);

        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        pref = getSharedPreferences("PrefTest", 0);
        String diff = pref.getString("난이도","");
        pro = dDay();
        String today = sdf.format(new Date());

        textView = findViewById(R.id.nday);
        textView2 = findViewById(R.id.achiev);
        textView3 = findViewById(R.id.detailDiff);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "select * from HistoryTable";

        Cursor c = db.rawQuery(sql, null);
        c.moveToLast();

        int intData2_pos = c.getColumnIndex("intData2");
        int intData2 = c.getInt(intData2_pos);

        int Clear_pos = c.getColumnIndex("ClearData");
        Boolean ClearData = (c.getInt(Clear_pos) == 0);

        textView.setText((diffDay + 1) + "일차 도전 중");
        progressBar.setProgress(intData2);
        db.close();

        if(ClearData == true){
            per = (((float)(intData2) / 30) * 100);
        }else{
            per= (((float)(intData2 - 1) / 30) * 100);
            Log.d("adsf",""+per);
        }

        textView2.setText(String.format("%.1f",per) + "%달성");

        textView3.setText(diff + " 30일 챌린지");

        createButton();
        //달성버튼
        for (int i = 0; i < intData2-1; i++) {

            b[i].setBackgroundResource(R.drawable.greencheck);
            b[i].setTextColor(ContextCompat.getColor(this, R.color.White));

        }
        b[intData2-1].setBackgroundResource(R.drawable.lightgreen);
        b[intData2-1].setTextColor(ContextCompat.getColor(this, R.color.Green));

        if (ClearData == true) {
            b[intData2-1].setBackgroundResource(R.drawable.greencheck);
            b[intData2-1].setTextColor(ContextCompat.getColor(this, R.color.White));
        }

        DBHelper2 helper3 = new DBHelper2(this);
        SQLiteDatabase db3 = helper3.getReadableDatabase();

        String sql3 = "select * from DifficultTable";

        Cursor c3 = db3.rawQuery(sql3,null);

        table = new String[30][];
        int re = 0;
        while(c3.moveToNext()){

            int textData_pos = c3.getColumnIndex("textData");
            int textData2_pos = c3.getColumnIndex("textData2");
            int timeData_pos = c3.getColumnIndex("timeData");
            int timeData2_pos = c3.getColumnIndex("timeData2");

            String textData = c3.getString(textData_pos);
            String textData2 = c3.getString(textData2_pos);
            int timeData = c3.getInt(timeData_pos);
            int timeData2 = c3.getInt(timeData2_pos);

            String[] aa = {textData, textData2, timeData+"", timeData2+""};
            table[re] = aa;

            re++;
        }
        db3.close();

        button = findViewById(R.id.reset);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog = new Dialog(Detail.this, R.style.Dialog);

                dialog.setContentView(R.layout.modal_reset);
                yes = dialog.findViewById(R.id.btnReset);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dCalendar.add(Calendar.DAY_OF_MONTH, 30);
                        Calendar calendar = Calendar.getInstance();

                        SharedPreferences pref = getSharedPreferences("PrefTest", 0);
                        SharedPreferences.Editor edit = pref.edit();

                        resetY = calendar.get(Calendar.YEAR);
                        resetM = calendar.get(Calendar.MONTH) + 1;
                        resetD = calendar.get(Calendar.DAY_OF_MONTH);

                        edit.putInt("년", resetY);
                        edit.putInt("월", resetM);
                        edit.putInt("일", resetD);
                        edit.commit();

                        delete();

                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(intent);

                        Intent refresh = new Intent(getApplicationContext(), Detail.class);

                        startActivity(refresh);
                        finish();

                        Toast toast = Toast.makeText(getApplicationContext(), "챌린지가 리셋되어 1일차로 변경되었습니다.", Toast.LENGTH_SHORT);
                        View view2 = toast.getView();
                        view2.setBackgroundResource(R.drawable.blackbtn);
                        TextView text = (TextView) view2.findViewById(android.R.id.message);
                        text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                        toast.show();

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

    public void createButton() {

        for (int i = 0; i < 30; i++) {

            b[i] = findViewById(getResources().getIdentifier("btn" + (i + 1), "id", getPackageName()));
            //b[i].setText((i + 1) + "일차");
            b[i].setTag(i);
            b[i].setOnClickListener(this);

        }

    }

    public int dDay() {
        //오늘
        calendar = Calendar.getInstance();
        String today = sdf.format(new Date());

        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);
        int preMonth = calendar.get(calendar.MONTH) + 1;

        //date = year+"-"+month+"-"+day;
        //시작날
        String start = year + "-" + month + "-" + day;

        // 오늘 - 시작날 차이구하기
        try {
            Date lastDate = sdf.parse(start);
            Date todayDate = sdf.parse(today);
            diffDay = (todayDate.getTime() - lastDate.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ((int) diffDay + 1);
    }

    public void delete() {

        DBHelper helper2 = new DBHelper(this);
        SQLiteDatabase db2 = helper2.getReadableDatabase();
        date = sdf.format(new Date());
        String sql = "delete from HistoryTable where 1 < idx";
        db2.execSQL(sql);

        String sql2 = "update HistoryTable set dateData = ? , ClearData = 1, boolData = 1, boolData2 = 1, memoData = null where idx = 1";
        String[] arg = {date};
        db2.execSQL(sql2, arg);
        db2.close();

    }

    @Override
    public void onClick(View view) {

        Button newButton = (Button)view;

        for(Button tempButton : b){
            if(tempButton == newButton){
                int position = (Integer)view.getTag();

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Detail.this);
                bottomSheetDialog.setContentView(R.layout.modal_bottom);

                tv1 = bottomSheetDialog.findViewById(R.id.tv1);
                tv2 = bottomSheetDialog.findViewById(R.id.tv2);
                tv3 = bottomSheetDialog.findViewById(R.id.tv3);
                tv4 = bottomSheetDialog.findViewById(R.id.tv4);
                tv5 = bottomSheetDialog.findViewById(R.id.tv5);
                ok = bottomSheetDialog.findViewById(R.id.BDok);
                bottomSheetDialog.show();

                tv1.setText((position+1)+"일차");
                tv2.setText(table[position][2] + "초");
                tv3.setText(table[position][0]);
                tv4.setText(table[position][3]+"초");
                tv5.setText(table[position][1]);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });


            }
        }

    }
}
