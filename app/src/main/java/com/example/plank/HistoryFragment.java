package com.example.plank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class HistoryFragment extends Fragment {

    private View view, view2;
    HAdapter adapter;
    private int pro, check1, check2;
    private long diffDay;
    private Calendar calendar, dCalendar, dDaycalendar;
    private SimpleDateFormat sdf;
    private NestedScrollView nestedScrollView;
    private SQLiteDatabase db;
    final int NOW = 1;
    final int CONTI = 2;
    Button button;
    RecyclerView recyclerView;
    MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);
        view2 = inflater.inflate(R.layout.item_history, container, false);

        view.setId(R.id.frag);
        recyclerView = view.findViewById(R.id.dayRecyc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HAdapter(new HAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), secondTimer.class);
                if(button.getId() == R.id.conti){
                    startActivityForResult(intent, CONTI);
                } else {
                    startActivityForResult(intent, NOW);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

        DBHelper helper = new DBHelper(getContext());
        db = helper.getReadableDatabase();
        activity = (MainActivity) getActivity();

        nestedScrollView = view.findViewById(R.id.hisNS);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null)
                {
                    if (scrollY > oldScrollY)
                    {
                        activity.setFragment("down");
                    }else if(scrollY < oldScrollY){
                        activity.setFragment("up");

                    }
                }
            }
        });

        pro = dDay();

        for (int i = 1; i <= pro; i++) {

            getData(Integer.toString(i));
        }

        button = view2.findViewById(R.id.hisBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), secondTimer.class);
                if (button.getId() == R.id.conti) {
                    startActivityForResult(intent, CONTI);
                } else {
                    startActivityForResult(intent, NOW);
                }
            }
        });

        return view;
    }

    private void getData(String dDay) {

        boolean ClearData = false;

        DBHelper2 helper2 = new DBHelper2(getContext());
        SQLiteDatabase db2 = helper2.getReadableDatabase();

        Log.d("aaaa",dDay+"elepdl");
        String sql = "select * from HistoryTable where intData = " + dDay + "";
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();

        int dateData_pos = c.getColumnIndex("dateData");
        int memoData_pos = c.getColumnIndex("memoData");
        int intData_pos = c.getColumnIndex("intData");
        int intData2_pos = c.getColumnIndex("intData2");
        int boolData_pos = c.getColumnIndex("boolData");
        int boolData2_pos = c.getColumnIndex("boolData2");

        String dateData = c.getString(dateData_pos);
        String memo = c.getString(memoData_pos);
        int intData = c.getInt(intData_pos);
        int intData2 = c.getInt(intData2_pos);
        Boolean boolData = (c.getInt(boolData_pos) == 0);
        Boolean boolData2 = (c.getInt(boolData2_pos) == 0);

        String[] date = dateData.split("-");
        String month = date[1];
        String day = date[2];

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        String plank = Integer.toString(intData2);

        String sql2 = "select * from DifficultTable where idx = ?";
        String[] arg = {plank};

        Cursor c2 = db2.rawQuery(sql2, arg);

        c2.moveToFirst();

        int plank1_pos = c2.getColumnIndex("textData");
        int plank2_pos = c2.getColumnIndex("textData2");

        String plank1 = c2.getString(plank1_pos);
        String plank2 = c2.getString(plank2_pos);

        String today = sdf.format(new Date());

        if (today.equals(dateData)) {
            int ClearData_pos = c.getColumnIndex("ClearData");
            ClearData = true;
//            ClearData = (c.getInt(ClearData_pos) == 0);
            if ((c.getInt(ClearData_pos) == 0)) {
                ClearData = false;
            }
        }

        if(boolData == true){
            check1 = R.drawable.ic_launcher_background;
        }else if(boolData == false){
            check1 = R.drawable.ic_launcher_foreground;
            if (today.equals(dateData)) {
                check1 = R.drawable.circle;
            }
        }
        if(boolData2 == true){
            check2 = R.drawable.ic_launcher_background;
        }else if(boolData2 == false){
            check2 = R.drawable.ic_launcher_foreground;
            if (today.equals(dateData)) {
                check2 = R.drawable.circle;
            }
        }

        DataHistory data = new DataHistory(month + "월", day + "일", intData + "일차", plank1, plank2, memo, check1, check2, ClearData);

        adapter.addItem(data);
        //db.close();
    }

    public int dDay() {
        //오늘
        String today = sdf.format(new Date());

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);

        //시작날
        String start = year + "-" + month + "-" + day;


        // 오늘 - 시작날 차이구하기
        try {
            Date lastDate = sdf.parse(start);
            Date todayDate = sdf.parse(today);
            diffDay = (todayDate.getTime() - lastDate.getTime() ) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if (diffDay >= 70000) {
//            diffDay = 0;
//        }


        return ((int) diffDay + 1);
    }

    public void up() {

        nestedScrollView.fullScroll(View.FOCUS_UP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOW:
                switch (resultCode) {
                    case RESULT_OK:
                        button.setId(R.id.conti);
                        String sql2 = "select * from HistoryTable";

                        Cursor c = db.rawQuery(sql2,null);
                        c.moveToLast();

                        int intData2_pos = c.getColumnIndex("intData2");
                        int intData2 = c.getInt(intData2_pos);

                        String sql = "update HistoryTable set boolData = 0 where intData2 = " +intData2;
                        db.execSQL(sql);
                        activity.setFragment("refresh");
                        db.close();
                        break;
                    default:
                        break;

                }
                break;
            case CONTI:
                switch (resultCode) {
                    case RESULT_OK:
                        button.setId(R.id.succ);
                        String sql2 = "select * from HistoryTable";

                        Cursor c = db.rawQuery(sql2,null);
                        c.moveToLast();

                        int intData2_pos = c.getColumnIndex("intData2");
                        int intData2 = c.getInt(intData2_pos);

                        String sql = "update HistoryTable set boolData2 = 0 where intData2 = " +intData2;
                        db.execSQL(sql);
                        activity.setFragment("refresh");
                        db.close();
                        break;
                    default:
                        break;

                }
                break;
        }
    }
}