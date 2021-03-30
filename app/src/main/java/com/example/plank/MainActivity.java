package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    public SharedPreferences preferences, preferences2;
    private SimpleDateFormat sdf;
    private String date;
    private ViewPager vp;
    private VPAdapter adapter;
    private FloatingActionButton fab;
    TabLayout tabLayout;
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

        AndroidBug5497Workaround.assistActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);

        vp = findViewById(R.id.viewpager);
        adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        historyFragment = new HistoryFragment();

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:

                        fab.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        fab.animate().translationY(fab.getHeight() + 60).setInterpolator(new AccelerateInterpolator(0)).start();
                        fab.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.commit();

                historyFragment = (HistoryFragment)adapter.getItem(1);
                historyFragment.up();

            }
        });

        tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(vp);

        drawerLayout = findViewById(R.id.drawer_layout);

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

    public void setFragment(String name) {
        switch (name) {
            case "refresh":
                vp.setAdapter(adapter);
                break;
            case "up":
                fab.animate().translationY(fab.getHeight() + 60).setInterpolator(new AccelerateInterpolator(2)).start();
                break;

            case "down":
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                break;
        }
    }
}