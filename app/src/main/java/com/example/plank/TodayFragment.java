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

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class TodayFragment extends Fragment {

    private static final String TAG1 = "1";
    private View view;
    private TextView textView, textView2, plank1, plank2, second, second2;
    private EditText editText, editText2;
    private NestedScrollView nestedScrollView;
    private SoftKeyboard softKeyboard;
    private ConstraintLayout ll;
    private Dialog dialog;
    private Button now, save, cancel;
    private SimpleDateFormat sdf;
    private String date, diffi;
    private Calendar calendar, dCalendar, dDaycalendar;
    private long diffDay;
    int pro, intData2;
    String textData, textData2;
    private ConstraintLayout constraintLayout, constraintLayout2;
    public SharedPreferences preferences, pref;
    final int NOW = 1;
    final int CONTI = 2;
    private ImageView imageView, imageView2;
    SQLiteDatabase db;
    MainActivity activity;
    String dateData;
    private Dialog dialog2;
    Button asdd;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_today, container, false);
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

        activity = (MainActivity) getActivity();

        calendar = Calendar.getInstance();
        pro = dDay();

        textView = view.findViewById(R.id.ing);
        textView.setText(pro + "일차 도전 중");
        textView2 = view.findViewById(R.id.chall);
        textView2.setText(diffi + " 30일 챌린지");
        second = view.findViewById(R.id.second);
        second2 = view.findViewById(R.id.second2);

        imageView = view.findViewById(R.id.iv1);
        imageView2 = view.findViewById(R.id.iv2);

        now = view.findViewById(R.id.now);
        editText = view.findViewById(R.id.blank);

        plank1 = view.findViewById(R.id.pl1);
        plank2 = view.findViewById(R.id.pl2);
        constraintLayout = view.findViewById(R.id.s);

        now.setText(pro + "일차 운동 시작하기");

        DBHelper helper = new DBHelper(getContext());
        db = helper.getReadableDatabase();

        String sql = "select * from HistoryTable";

        Cursor c = db.rawQuery(sql, null);
        c.moveToLast();

        int intData2_pos = c.getColumnIndex("intData2");
        intData2 = c.getInt(intData2_pos);

        int boolData_pos = c.getColumnIndex("boolData");
        Boolean boolData = (c.getInt(boolData_pos)==0);
        int boolData2_pos = c.getColumnIndex("boolData2");
        Boolean boolData2 = (c.getInt(boolData2_pos)==0);

        if (boolData == true) {
            imageView.setImageResource(R.drawable.ic_launcher_background);
            second.setBackgroundResource(R.drawable.seconditem2);
            second.setTextColor(ContextCompat.getColor(getContext(),R.color.Green));
            now.setTag("conti");
            now.setText(pro + "일차 운동 계속하기");
        }
        if(boolData2 == true){
            imageView2.setImageResource(R.drawable.ic_launcher_background);
            second2.setBackgroundResource(R.drawable.seconditem2);
            second2.setTextColor(ContextCompat.getColor(getContext(),R.color.Green));
            now.setTag("succ");
            now.setText(pro + "일차 운동 성공! 내일 또 봐요");
            now.setBackgroundResource(R.drawable.planksuccess);
            now.setEnabled(false);
        }

        progressBar.setProgress(intData2);

        dbPlank();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setShowSoftInputOnFocus(false);
        } else {
        }

        int dateData_pos = c.getColumnIndex("dateData");
        dateData = c.getString(dateData_pos);
        asdd = view.findViewById(R.id.asdd);

        asdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("asdd",dateData);
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
                                dialog.dismiss();
                                String memo = editText2.getText() + "";

                                editText.setText(memo);
                                Toast.makeText(getContext(), "오늘의 메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getContext(), "오늘의 메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                dbSave(memo);
                                activity.setFragment("refresh");

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

        DBHelper2 helper3 =new DBHelper2(getContext());
        SQLiteDatabase db3 = helper3.getReadableDatabase();

        String sql3 = "select * from DifficultTable where idx = "+intData2;
        Cursor c3 = db3.rawQuery(sql3,null );

        c3.moveToLast();
        int timeData_pos = c3.getColumnIndex("timeData");
        int timeData2_pos = c3.getColumnIndex("timeData2");
        int textData_pos= c3.getColumnIndex("textData");
        int textData2_pos= c3.getColumnIndex("textData2");

        final int timeData = c3.getInt(timeData_pos);
        final int timeData2 = c3.getInt(timeData2_pos);
        final String textData = c3.getString(textData_pos);
        final String textData2 = c3.getString(textData2_pos);

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getContext(), secondTimer.class);
                Intent intent = new Intent(getContext(), TimerReady.class);
                if(now.getTag().equals("conti")){

                    intent.putExtra("val",CONTI);
                    intent.putExtra("time2",timeData2);
                    intent.putExtra("name2",textData2);
                    startActivity(intent);

                } else {
                    intent.putExtra("time",timeData);
                    intent.putExtra("name",textData);
                    intent.putExtra("time2",timeData2);
                    intent.putExtra("name2",textData2);
                    intent.putExtra("val",NOW);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case NOW:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        imageView.setImageResource(R.drawable.ic_launcher_background);
//                        //now.setTag("conti");
//                        //now.setText(pro + "일차 운동 계속하기");
//                        String sql = "update HistoryTable set boolData = 0 where intData2 = " +intData2;
//                        db.execSQL(sql);
//                        Toast.makeText(getContext(), "bbbbbbbbbbbbbbb", Toast.LENGTH_SHORT).show();
//                        activity.setFragment("refresh");
//                        break;
//                    default:
//                        break;
//
//                }
//                break;
//            case CONTI:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        //now.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.planksuccess));
//                        //now.setTag("succ");
//                        //now.setText(pro + "일차 운동 성공! 내일 또 봐요");
//                        //now.setEnabled(false);
//                        String sql = "update HistoryTable set boolData2 = 0, ClearData = 0 where intData2 = " +intData2;
//                        db.execSQL(sql);
//                        activity.setFragment("refresh");
//                        break;
//                    default:
//                        break;
//
//                }
//                break;
//        }
//
//    }

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
        String[] args = {memo, date};

        db.execSQL(sql, args);
        db.close();
    }

    public int dDay() {
        //오늘
        String today = sdf.format(new Date());

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("년", 0);
        int month = pref.getInt("월", 0);
        int day = pref.getInt("일", 0);

        diffi = pref.getString("난이도", "");

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

    public void dbPlank() {
        DBHelper2 helper2 = new DBHelper2(getContext());
        SQLiteDatabase db2 = helper2.getReadableDatabase();

        String sql2 = "select * from DifficultTable where idx = " + intData2 + "";
        Cursor c2 = db2.rawQuery(sql2, null);

        c2.moveToFirst();

        int textData_pos = c2.getColumnIndex("textData");
        int textData_pos2 = c2.getColumnIndex("textData2");

        textData = c2.getString(textData_pos);
        textData2 = c2.getString(textData_pos2);

        plank1.setText(textData);
        if (textData2 == null) {
            constraintLayout.setVisibility(View.GONE);
        } else {
            constraintLayout.setVisibility(View.VISIBLE);
            plank2.setText(textData2);
        }
        db2.close();

    }

}