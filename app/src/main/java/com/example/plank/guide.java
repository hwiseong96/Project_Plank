package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class guide extends AppCompatActivity {

    private List<String> temp;
    private TextView textView, textView2, textView3;
    private TabLayout tabLayout;
    public SharedPreferences preferences;
    private ViewFlipper flipper;
    private ViewPager pager;
    public static int countIndexes = 3;
    final int[] currentIndex = {0};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        preferences = getSharedPreferences("Pref2", MODE_PRIVATE);
        temp = new ArrayList<>();
        final float[] downX = new float[1];
        final float[] upX = new float[1];

        flipper = findViewById(R.id.flipper);
//        Animation showIn = AnimationUtils.loadAnimation(this, R.anim.flipper_left_out);
//        flipper.setInAnimation(showIn);
//        flipper.setOutAnimation(this, R.anim.flipper_right_in);

        textView = findViewById(R.id.explane);
        textView2 = findViewById(R.id.explane2);
        textView3 = findViewById(R.id.explane3);

        temp.add((String) textView.getText());
        temp.add((String) textView2.getText());
        temp.add((String) textView3.getText());

        OnboardingAdapter a = new OnboardingAdapter(temp, this);
        pager = (ViewPager) findViewById(R.id.onboardVP);
        pager.setAdapter(a);


        pager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                flipper.showNext();
                return false;
            }
        });

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    downX[0] = motionEvent.getX();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upX[0] = motionEvent.getX();

//                     뷰에서 터치 UP 했을때, 즉, 손가락을 떼었을떼의 위치 값이 뷰에서 터치 DOWN했을때 즉,
//                     손가락으로 스크린을 눌렀을때의 위치값보다 작다면, 이말은 좌측으로 swipe 한다는 의미이다.
                    if (upX[0] < downX[0]) {
//                         다음 슬라이드가 존재할 경우 다음 슬라이드를 보여준다.
                        if (currentIndex[0] < (countIndexes - 1)) {
                            flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_right_in));
                            flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_left_out));
                            flipper.showNext();
                            currentIndex[0]++;
                        }
                    } else if (upX[0] > downX[0]) {

//                         이전 플리퍼뷰가 존재할 경우, 이전 플리퍼뷰를 보여준다.
                        if (currentIndex[0] > 0) {
                            flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_left_in));
                            flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_right_out));
                            flipper.showPrevious();
                            // update index buttons
                            currentIndex[0]--;
                        }
                    }
                }
                return false;

            }
        });
        tabLayout = findViewById(R.id.onboardTL);
        tabLayout.setupWithViewPager(pager, true);
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (pager.getCurrentItem() == 2) {
                    finish();
                    checkSecondRun();
                }
                pager.setCurrentItem(pager.getCurrentItem() + 1);
                if (currentIndex[0] < (countIndexes - 1)) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_right_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flipper_left_out));
                    flipper.showNext();
                    currentIndex[0]++;
                }
                break;
            case R.id.skip:
                finish();
                checkSecondRun();
        }
    }

    public void checkSecondRun(){
        boolean isSecondRun = preferences.getBoolean("isSecondRun",true);
        if (isSecondRun){
            Intent intent = new Intent(this, Difficulty.class);
            startActivity(intent);
//            preferences2.edit().putBoolean("isSecondRun",false).apply();
        }
    }


}