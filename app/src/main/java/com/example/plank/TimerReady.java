package com.example.plank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private int time, time2, val, temp2, temp4, second;
    private TextView title, textView;
    private AudioManager audio;
    private long counter;
    private long milliLeft, sec;
    private int count = 0;
    private CountDownTimer countDownTimer;
    private MediaPlayer mp;
    private CircularProgressBar circularProgressBar;

    private SoundPool soundPool;
    int playSoundId, streamId;
    private MenuItem menuItem;
    private AudioManager mAlramMAnager;
    private boolean sound;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_ready);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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

        pref = getSharedPreferences("PrefTest", 0);
        sound = pref.getBoolean("mute",true);

        if (val == 1) {
            temp1 = plank;
            temp2 = time;
            temp3 = plank2;
            temp4 = time2;
        } else if (val == 3) {
            val = 2;
            temp1 = plank2;
            temp2 = time2;
            plus.setVisibility(View.VISIBLE);
        } else {
            temp1 = plank2;
            temp2 = time2;
        }

        circularProgressBar = findViewById(R.id.circle);
        circularProgressBar.mMaxProgress = 10;

        circularProgressBar.CircularWidth((int) getResources().getDimension(R.dimen.circleWidth2));
        circularProgressBar.CircularProgressBar(10);
        textView = findViewById(R.id.second);
        second = 10;
        int ten = 10000;

        timerStart(ten);


        //롤리팝 이상 버전일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        } else {
            //롤리팝 이하 버전일 경우
            // new SoundPool(1번,2번,3번)
            // 1번 - 음악 파일 갯수
            // 2번 - 스트림 타입
            // 3번 - 음질
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        }

        playSoundId = soundPool.load(this, R.raw.beep, 1);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        mAlramMAnager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);

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
                countDownTimer.cancel();
                startActivityForResult(intent, 1);

            }
        });

        plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                circularProgressBar.mMaxProgress += 20;
                second += 20;
                milliLeft += 20000;
                timerStart(milliLeft);
            }
        });

    }

    public void timerStart(long timeLengthMilli) {
        counter = timeLengthMilli;
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long l) {

                milliLeft = l;
                sec = (l / 1000);
                second -= 1;
                if (second < 0) {
                    second = 0;
                }
                milliLeft = l;
                if (second <= 3) {
                    if(sound) {
                        soundPool.play(playSoundId, 1, 1, 0, 0, 0.5f);
                    }
                }
                milliLeft = l;
                textView.setText(second + "초");
                count++;
                circularProgressBar.setProgress(count);
                sec--;

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), secondTimer.class);
                intent.putExtra("name", temp1);
                intent.putExtra("time", temp2);
                intent.putExtra("val", val);
                intent.putExtra("name3", temp3);
                intent.putExtra("time4", temp4);
                startActivityForResult(intent, 1);
            }
        }.start();
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sound, menu);

        menuItem = menu.getItem(0);

        if (sound) {
            //menuItem.setTitle("on");
            menuItem.setIcon(R.drawable.frame_3x);
        } else {
            //menuItem.setTitle("off");
            menuItem.setIcon(R.drawable.mute_3x);
        }
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                countDownTimer.cancel();
                finish();
                return true;
            }

            case R.id.sound:
                // User chose the "Settings" item, show the app settings UI...
                SharedPreferences.Editor edit = pref.edit();
//                if (item.getTitle().equals("on")) {
                if (sound) {
                    Toast toast = Toast.makeText(this, "카운트 소리를 사용하지 않습니다.", Toast.LENGTH_SHORT);
                    View view2 = toast.getView();
                    view2.setBackgroundResource(R.drawable.blackbtn);
                    TextView text = (TextView) view2.findViewById(android.R.id.message);
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                    toast.show();
                    item.setIcon(R.drawable.mute_3x);
                    sound = false;
                    edit.putBoolean("mute",false);
                    edit.commit();
                    //MuteAudio();
                    //item.setTitle("off");
                } else {
                    Toast toast = Toast.makeText(this, "카운트 소리를 사용합니다.", Toast.LENGTH_SHORT);
                    View view2 = toast.getView();
                    view2.setBackgroundResource(R.drawable.blackbtn);
                    TextView text = (TextView) view2.findViewById(android.R.id.message);
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                    toast.show();

                    item.setIcon(R.drawable.frame_3x);
                    sound = true;
                    edit.putBoolean("mute",true);
                    edit.commit();
                    //UnMuteAudio();
                    //item.setTitle("on");
                }

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                //audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        finish();

        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            if(((MainActivity) MainActivity.mContext) != null){
                ((MainActivity) MainActivity.mContext).setFragment("refresh");
            }
            if (data != null) {
                temp1 = data.getStringExtra("p2");
                temp2 = data.getIntExtra("t2", 0);
            }

            //title.setText(temp1 + " " + temp2 + "초");
            Intent refresh = new Intent(this, TimerReady.class);

            refresh.putExtra("name2", temp1);
            refresh.putExtra("time2", temp2);
            refresh.putExtra("val", 3);

            startActivity(refresh);
            this.finish();

        } else if (resultCode == 1) {
            ((MainActivity) MainActivity.mContext).setFragment("refresh");
            Intent intent = new Intent(getApplicationContext(), TimerComplete.class);
            startActivityForResult(intent,3);

        } else if( resultCode == 3){
            finish();
        }

    }
}