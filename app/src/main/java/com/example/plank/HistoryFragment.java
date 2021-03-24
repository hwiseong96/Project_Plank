package com.example.plank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    private static final String TAG2 = "2";
    private View view, view2;
    HAdapter adapter;
    private int pro;
    private long diffDay;
    private Calendar calendar, dCalendar, dDaycalendar;
    private TextView memoText;
    private ConstraintLayout constraintLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.dayRecyc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HAdapter();
        recyclerView.setAdapter(adapter);
        memoText = view.findViewById(R.id.memo);

        view2 = inflater.inflate(R.layout.item_history, container, false);
        constraintLayout = view2.findViewById(R.id.memoLayout);

        pro = dDay();

        for (int i = 1; i <= pro; i++) {

            getData(Integer.toString(i));

        }

        return view;
    }

    private void getData(String dDay) {
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        DBHelper helper2 = new DBHelper(getContext());
        SQLiteDatabase db2 = helper2.getReadableDatabase();

        String sql = "select * from HistoryTable where intData = " + dDay + "";
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();

        int dateData_pos = c.getColumnIndex("dateData");
        int memoData_pos = c.getColumnIndex("memoData");
        int intData_pos = c.getColumnIndex("intData");

        String dateData = c.getString(dateData_pos);
        String memo = c.getString(memoData_pos);
        int a = c.getInt(intData_pos);


        if (memo == null || memo.length() == 0) {
            Log.d("dhodho","asdf");
            constraintLayout.setVisibility(view2.GONE);

        } else {
            constraintLayout.setVisibility(View.VISIBLE);

        }

        String[] date = dateData.split("-");
        String month = date[1];
        String day = date[2];

        DataHistory data = new DataHistory(month + "월", day + "일", a + "일차", "플랭크1", "플랭크2", memo, R.drawable.ic_launcher_background);
        adapter.addItem(data);
        db.close();


    }

    public int dDay() {
        //오늘
        calendar = Calendar.getInstance();

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);
        int preMonth = calendar.get(calendar.MONTH) + 1;

        //시작날
        dCalendar = new GregorianCalendar(year, month, day);

        //오늘
        dDaycalendar = new GregorianCalendar(calendar.get(calendar.YEAR), preMonth, calendar.get(calendar.DAY_OF_MONTH));
        // 오늘 - 시작날 차이구하기
        long diffSec = (dDaycalendar.getTimeInMillis() - dCalendar.getTimeInMillis()) / 1000;
        diffDay = diffSec / (24 * 60 * 60);
        if (diffDay >= 70000) {
            diffDay = 0;
        }
        return ((int) diffDay + 1);
    }


}