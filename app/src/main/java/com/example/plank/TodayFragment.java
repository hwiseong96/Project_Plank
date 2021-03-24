package com.example.plank;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class TodayFragment extends Fragment {

    private static final String TAG1 = "1";
    HAdapter adapter;
    private View view, view2, view3;
    private TextView textView, textView2, plank1, plank2, memoText, mon, il, ilcha;
    private EditText editText, editText2;
    private NestedScrollView nestedScrollView;
    private SoftKeyboard softKeyboard;
    private ConstraintLayout ll;
    private BroadcastReceiver receiver;
    private Dialog dialog;
    private Button now, save, cancel;
    private SimpleDateFormat sdf;
    private String date, diffi;
    private Calendar calendar, dCalendar, dDaycalendar;
    private long diffDay;
    int pro;
    String textData, textData2;
    private ConstraintLayout constraintLayout, constraintLayout2;
    public SharedPreferences preferences, pref;



    public TodayFragment() {

    }

    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_today, container, false);
        view2 = inflater.inflate(R.layout.item_history, container, false);
        view3 = inflater.inflate(R.layout.fragment_history, container, false);
        ProgressBar progressBar = view.findViewById(R.id.probar);
        progressBar.setIndeterminate(false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Button button = view.findViewById(R.id.detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Detail.class);
                startActivity(intent);
            }
        });

        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        date = sdf.format(new Date());

        calendar = Calendar.getInstance();
        pro = dDay();
        progressBar.setProgress(pro);

        textView = view.findViewById(R.id.ing);
        textView.setText(pro + "일차 도전 중");
        textView2 = view.findViewById(R.id.chall);
        textView2.setText(diffi + " 30일 챌린지");

        RecyclerView recyclerView = view3.findViewById(R.id.dayRecyc);
        memoText = view2.findViewById(R.id.memo);
        adapter = new HAdapter();
        recyclerView.setAdapter(adapter);
        constraintLayout2 = view2.findViewById(R.id.memoLayout);

        plank1 = view.findViewById(R.id.pl1);
        plank2 = view.findViewById(R.id.pl2);
        constraintLayout = view.findViewById(R.id.s);

        dbPlank();

        editText = view.findViewById(R.id.blank);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setShowSoftInputOnFocus(false);
        } else {

        }

        //editText.setInputType(InputType.TYPE_NULL);

        Button asdd = view.findViewById(R.id.asdd);
        asdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHelper helper = new DBHelper(getContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                String sql2 = "select * from HistoryTable";

                Cursor c = db.rawQuery(sql2, null);
                while(c.moveToNext()){

                    int idx_pos = c.getColumnIndex("idx");
                    int idx = c.getInt(idx_pos);

                    int dateData_pos = c.getColumnIndex("dateData");
                    String dateData = c.getString(dateData_pos);

                    int intData_pos = c.getColumnIndex("intData");
                    int id = c.getInt(intData_pos);


                Log.d("bbb",dateData +"#"+idx + "#" +  id);
                }
                db.close();
                pref = getActivity().getSharedPreferences("PrefTest", 0);
                int asdf = pref.getInt("확인",0);
                editText.setText(asdf+"aa");

            }
        });

        ll = view.findViewById(R.id.ll);
        InputMethodManager controlManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(ll, controlManager);
        nestedScrollView = view.findViewById(R.id.NesScroll);

        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //키보드 내려왔을때
                    }
                });

            }

            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        dialog = new Dialog(getContext(), R.style.Dialog);
                        dialog.setContentView(R.layout.memo);
                        dialog.show();

                        save = dialog.findViewById(R.id.save);
                        cancel = dialog.findViewById(R.id.cancel);
                        editText2 = dialog.findViewById(R.id.todayMemo);

                        if (editText.getText().length() != 0) {
                            editText2.setText(editText.getText());
                        }

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String memo = editText2.getText() + "";

                                String today = sdf.format(new Date());

                                editText.setText(memo);
                                if(memo.length() == 0 ){
                                    constraintLayout2.setVisibility(View.GONE);
                                }

                                DBHelper helper = new DBHelper(getContext());
                                SQLiteDatabase db = helper.getReadableDatabase();

                                String sql = "select * from HistoryTable";
                                Cursor c = db.rawQuery(sql,null);

                                c.moveToLast();

                                int intData_pos = c.getColumnIndex("intData");
                                int dateData_pos = c.getColumnIndex("dateData");

                                int intData = c.getInt(intData_pos);
                                String dateData = c.getString(dateData_pos);

                                if(dateData.equals(today) && intData == pro){

                                    constraintLayout2.setVisibility(View.VISIBLE);
                                    memoText.setText(memo);
                                    adapter.notifyDataSetChanged();
                                }

                                Toast.makeText(getContext(), "오늘의 메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                dbSave(memo);

                                dialog.dismiss();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                            }
                        });

                        //키보드 올라왔을때
                    }
                });

            }
        });
        now = view.findViewById(R.id.now);

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), secondTimer.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();

    }

    public void dbSave(String s) {
        String memo = s;

        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        date = sdf.format(new Date());
        String sql = "update HistoryTable set memoData = ? where dateData = ?";
        String [] args = {memo, date};

        db.execSQL(sql, args);

        db.close();

    }

    public int dDay() {
        //오늘

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);
        int preMonth = calendar.get(calendar.MONTH) + 1;

        diffi = pref.getString("난이도", "");

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

    public void dbPlank() {
        DBHelper2 helper = new DBHelper2(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "select * from DifficultTable where idx = " + pro + "";
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();

        int textData_pos = c.getColumnIndex("textData");
        int textData_pos2 = c.getColumnIndex("textData2");

        textData = c.getString(textData_pos);
        textData2 = c.getString(textData_pos2);

        plank1.setText(textData);
        if (textData2 == null) {
            constraintLayout.setVisibility(View.GONE);
        } else {
            constraintLayout.setVisibility(View.VISIBLE);
            plank2.setText(textData2);
        }
        db.close();

    }



}