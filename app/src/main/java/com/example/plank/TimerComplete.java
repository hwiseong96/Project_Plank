package com.example.plank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class TimerComplete extends AppCompatActivity {


    private Button fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_complete);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.appbar).bringToFront();

        fin = findViewById(R.id.fin);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(3);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                setResult(3);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}