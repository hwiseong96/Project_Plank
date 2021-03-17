package com.example.plank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context = this;
    Button button;
    private EditText editText;
    private NestedScrollView nestedScrollView;
    public SharedPreferences preferences, preferences2;
    private SimpleDateFormat sdf;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, guide.class);
//        startActivity(intent);
        preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        preferences2 = getSharedPreferences("one",MODE_MULTI_PROCESS);

        checkFirstRun();
        one();

        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = sdf.format(new Date());


        AndroidBug5497Workaround.assistActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(vp);

        drawerLayout = findViewById(R.id.drawer_layout);
        //
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setButton(View view) {

        switch (view.getId()) {
            case R.id.whatis:
                break;
            case R.id.alarm:
                Intent intent = new Intent(this, Alarm.class);
                startActivity(intent);
                break;
            case R.id.star:
                break;
            case R.id.open:
                break;
            case R.id.bug:
                break;

        }

    }

    public void checkFirstRun() {
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Intent intent = new Intent(this, guide.class);
            startActivity(intent);
            preferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }
    public void one() {
        boolean one = preferences2.getBoolean("isOne", true);
        if (one) {
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            preferences2.edit().putBoolean("isOne", false).apply();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void dbSave() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //메모, n일차, 날짜 ++성공여부 추가
        String sql1 = "select dateData from HistoryTable where dateData = " + date;
        Cursor c = db.rawQuery(sql1, null);

        while (c.moveToNext()) {
            int dateData_pos = c.getColumnIndex("dateData");
            String dateData = c.getString(dateData_pos);
            if (!dateData.equals(date)) {

            }
        }

        String sql = "insert into HistoryTable (textData, intData, dateData) values(?,?,?)";


        //? 에 바인딩 될 값 배열
        String[] arg1 = {"", "", date};

        db.execSQL(sql, arg1);

        db.close();
    }

}