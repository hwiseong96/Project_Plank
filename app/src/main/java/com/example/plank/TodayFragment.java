package com.example.plank;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

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
import java.util.Locale;


public class TodayFragment extends Fragment {
    private View view;
    private int currentYear, currentMonth, currentDay;
    private Calendar calendar;
    private TextView textView;
    private EditText editText, editText2;
    private NestedScrollView nestedScrollView;
    private SoftKeyboard softKeyboard;
    private ConstraintLayout ll;
    private BroadcastReceiver receiver;
    private Dialog dialog;
    private Button now, save, cancel;

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
        ProgressBar progressBar = view.findViewById(R.id.probar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(1);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Button button = view.findViewById(R.id.detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Detail.class);
                startActivity(intent);
            }
        });

        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        textView = view.findViewById(R.id.chall);
        editText = view.findViewById(R.id.blank);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setShowSoftInputOnFocus(false);
        }else{

        }

        //editText.setInputType(InputType.TYPE_NULL);


        ll = view.findViewById(R.id.ll);
        InputMethodManager controlManager = (InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(ll, controlManager);
        nestedScrollView = view.findViewById(R.id.NesScroll);

        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //키보드 내려왔을때
                    }
                });

            }

            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        dialog = new Dialog(getContext(), R.style.Dialog);
                        dialog.setContentView(R.layout.memo);
                        dialog.show();

                        save = dialog.findViewById(R.id.save);
                        cancel = dialog.findViewById(R.id.cancel);
                        editText2 = dialog.findViewById(R.id.todayMemo);

                        if(editText.getText().length() != 0){
                            editText2.setText(editText.getText());
                        }

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String memo = editText2.getText()+"";

                                editText.setText(memo);
//                                Toast.makeText(getContext(), "오늘의 메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                //dbSave(memo);
                                dialog.dismiss();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DBHelper helper = new DBHelper(getContext());
                                SQLiteDatabase db = helper.getWritableDatabase();

                                String sql = "select * from HistoryTable";
                                Cursor c = db.rawQuery(sql, null);
                                while(c.moveToNext()){
                                    int dateData_pos = c.getColumnIndex("dateData");
                                    String dateData = c.getString(dateData_pos);

                                    int textData_pos = c.getColumnIndex("textData");
                                    String textData = c.getString(textData_pos);

                                    Log.d("vvv",textData+"bb"   );
                                    Log.d("vvvv",dateData+"aa");
                                }


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

    public void dbSave(String s){
        String memo = s;
        DBHelper helper  = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());

        String sql1 = "select dateData from HistoryTable";
        String sql2 = "update HistoryTable set textData = ? where idx = ?";
        Cursor c = db.rawQuery(sql1, null);

        while(c.moveToNext()){

            int dateDate_pos = c.getColumnIndex("dateData");

            if(date.equals(c.getString(dateDate_pos))){

                String []args = {memo, String.valueOf(dateDate_pos)};
                db.execSQL(sql2, args);

            }

        }

        db.close();
    }

}