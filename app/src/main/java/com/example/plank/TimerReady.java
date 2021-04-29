package com.example.plank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerReady extends AppCompatActivity {

    private Button skip, plus;
    private String plank, plank2, temp1, temp3;
    private int time, time2, val, temp2, temp4;
    private TextView title;
    private AudioManager audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_ready);

        audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        plus = findViewById(R.id.plus);
        plus.setVisibility(View.GONE);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        time = getIntent().getIntExtra("time", 0);
        plank = getIntent().getStringExtra("name");
        time2 = getIntent().getIntExtra("time2", 0);
        plank2 = getIntent().getStringExtra("name2");
        val = getIntent().getIntExtra("val", 0);

        if (val == 1) {
            temp1 = plank;
            temp2 = time;
            temp3 = plank2;
            temp4 = time2;
        } else if(val == 3){
            temp1 = plank2;
            temp2 = time2;
            plus.setVisibility(View.VISIBLE);
        }else{
            temp1 = plank2;
            temp2 = time2;
        }

//        DBHelper2 helper3 =new DBHelper2(getApplicationContext());
//        SQLiteDatabase db3 = helper3.getReadableDatabase();
//
//        String sql3 = "select * from DifficultTable where idx = " + intData2;
//        Cursor c3 = db3.rawQuery(sql3,null );
//
//        c3.moveToLast();
//        int timeData_pos = c3.getColumnIndex("timeData");
//        int timeData2_pos = c3.getColumnIndex("timeData2");
//
//        final int timeData = c3.getInt(timeData_pos);
//        final int timeData2 = c3.getInt(timeData2_pos);

        title = findViewById(R.id.plankName);
        title.setText(temp1 + " " + temp2 + "초");

        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), secondTimer.class);
                intent.putExtra("name", temp1);
                intent.putExtra("time", temp2);
                intent.putExtra("val", val);
                intent.putExtra("name3", temp3);
                intent.putExtra("time4", temp4);
                startActivityForResult(intent, 1);

            }
        });

    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sound, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.sound:
                // User chose the "Settings" item, show the app settings UI...

                if(item.getTitle().equals("on")){
                    Toast.makeText(this, "카운트 소리를 사용하지 않습니다.", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.mute_3x);
                    audio.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE,0);
                    item.setTitle("off");
                }else{
                    Toast.makeText(this,"카운트 소리를 사용합니다." , Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.frame_3x);
                    audio.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,0);
                    item.setTitle("on");
                }

                return true;

            case R.id.mute:

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                //audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            ((MainActivity) MainActivity.mContext).setFragment("refresh");
            if (data != null) {
                temp1 = data.getStringExtra("p2");
                temp2 = data.getIntExtra("t2", 0);
            }

            //title.setText(temp1 + " " + temp2 + "초");
            Intent refresh = new Intent(this, TimerReady.class);

            refresh.putExtra("name2", temp1);
            refresh.putExtra("time2", temp2);
            refresh.putExtra("val",3);

            startActivity(refresh);
            this.finish();

        } else if (resultCode == 1) {
            ((MainActivity) MainActivity.mContext).setFragment("refresh");
            Intent intent = new Intent(getApplicationContext(),TimerComplete.class);
            startActivity(intent);

        }

    }
}