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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

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
    private boolean danger;
    private Dialog dialog;
    Button button;
    private long backkeyPressedTime;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        preferences = getSharedPreferences("PrefTest", MODE_PRIVATE);
        sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

        //fab
        AndroidBug5497Workaround.assistActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger);

        vp = findViewById(R.id.viewpager);
        adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        historyFragment = new HistoryFragment();
        danger = getIntent().getBooleanExtra("경고",false);

        if(danger == true){
            dialog = new Dialog(MainActivity.this,R.style.Dialog);

            dialog.setContentView(R.layout.modal_danger);
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            button = dialog.findViewById(R.id.ghkrdls);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }
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

        tab();

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
                tab();
                break;
            case "up":
                fab.animate().translationY(fab.getHeight() + 60).setInterpolator(new AccelerateInterpolator(2)).start();
                break;
            case "down":
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(System.currentTimeMillis() > backkeyPressedTime + 2000){
            backkeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backkeyPressedTime + 2000){
            finish();
        }
    }
    public void tab(){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(this);
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/nexonlv1gothicregular.ttf");
                    tabTextView.setTypeface(typeface, Typeface.BOLD);
                }else{
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/nexonlv1gothicregular.ttf");
                    tabTextView.setTypeface(typeface, Typeface.NORMAL);
                }

            }

        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                vp.setCurrentItem(tab.getPosition());

                TextView text = (TextView) tab.getCustomView();
                if(text != null ) {
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/nexonlv1gothicregular.ttf");
                    text.setTypeface(typeface, Typeface.BOLD);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                if(text != null) {
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "font/nexonlv1gothicregular.ttf");
                    text.setTypeface(typeface, Typeface.NORMAL);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }
}