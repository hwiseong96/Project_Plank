package com.example.plank;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
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
import java.util.Locale;

public class TodayFragment extends Fragment {

    private static final String TAG1 = "1";
    private View view;
    private TextView textView, textView2, plank1, plank2, second, second2, total;
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
        textView.setText(pro + "?????? ?????? ???");
        textView2 = view.findViewById(R.id.chall);
        textView2.setText(diffi + " 30??? ?????????");

        second = view.findViewById(R.id.second);
        second2 = view.findViewById(R.id.second2);
        total = view.findViewById(R.id.total);

        imageView = view.findViewById(R.id.iv1);
        imageView2 = view.findViewById(R.id.iv2);

        now = view.findViewById(R.id.now);
        editText = view.findViewById(R.id.blank);

        plank1 = view.findViewById(R.id.pl1);
        plank2 = view.findViewById(R.id.pl2);
        constraintLayout = view.findViewById(R.id.s);

        now.setText(pro + "?????? ?????? ????????????\uD83D\uDC4A");

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
        int dateData_pos = c.getColumnIndex("dateData");
        dateData = c.getString(dateData_pos);

        int memoData_pos = c.getColumnIndex("memoData");
        String memoData = c.getString(memoData_pos);
        if(memoData != null){
            editText.setText(memoData);
        }

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

        second.setText(timeData + "???");
        second2.setText(timeData2 + "???");
        total.setText(timeData + timeData2+"???");

        if (boolData == true) {
            imageView.setImageResource(R.drawable.check_3x);
            second.setBackgroundResource(R.drawable.seconditem2);
            second.setTextColor(ContextCompat.getColor(getContext(),R.color.Green));
            now.setTag("conti");
            now.setText(pro + "?????? ?????? ????????????\uD83D\uDC49");
        }
        if(boolData2 == true){
            imageView2.setImageResource(R.drawable.check_3x);
            second2.setBackgroundResource(R.drawable.seconditem2);
            second2.setTextColor(ContextCompat.getColor(getContext(),R.color.Green));
            now.setTag("succ");
            now.setText(pro + "?????? ?????? ??????! ?????? ??? ??????\uD83D\uDC4D");
            now.setTextColor(Color.parseColor("#707C97"));
            now.setBackgroundResource(R.drawable.planksuccess);
            now.setEnabled(false);
        }

        progressBar.setProgress(intData2);

        dbPlank();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setShowSoftInputOnFocus(false);
        } else {
        }

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
                        //????????? ???????????????
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
                                Toast toast = Toast.makeText(getContext(), "????????? ????????? ?????????????????????!", Toast.LENGTH_SHORT);
                                View view2 = toast.getView();
                                view2.setBackgroundResource(R.drawable.blackbtn);
                                TextView text = (TextView) view2.findViewById(android.R.id.message);
                                text.setTextColor(ContextCompat.getColor(getContext(),R.color.White));
                                toast.show();
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
                        //????????? ???????????????
                    }
                });
            }
        });


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
        //??????
        String today = sdf.format(new Date());

        SharedPreferences pref = getActivity().getSharedPreferences("PrefTest", 0);
        int year = pref.getInt("???", 0);
        int month = pref.getInt("???", 0);
        int day = pref.getInt("???", 0);

        diffi = pref.getString("?????????", "");

        //?????????
        String start = year + "-" + month + "-" + day;

        // ?????? - ????????? ???????????????
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

        if(textData.equals("??????")){
            now.setTag("??????");
            now.setBackgroundResource(R.drawable.planksuccess);
            now.setEnabled(false);
            DBHelper helper = new DBHelper(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();

            date = sdf.format(new Date());
            String sql = "update HistoryTable set ClearData = ? where dateData = ?";
            String[] args = {"0", date};

            db.execSQL(sql, args);
            db.close();
            constraintLayout.setVisibility(View.GONE);
        }else if (textData2 == null) {
            constraintLayout.setVisibility(View.GONE);
        } else {
            constraintLayout.setVisibility(View.VISIBLE);
            plank2.setText(textData2);
        }


        db2.close();

    }

}