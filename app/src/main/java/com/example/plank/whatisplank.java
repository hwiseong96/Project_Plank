//package com.example.plank;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//
//public class Detail extends AppCompatActivity {
//    RVAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//
//        Toolbar mToolbar =  findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        init();
//        getData();
//    }
//
//    private void init(){
//        RecyclerView recyclerView = findViewById(R.id.deRecyc);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new RVAdapter();
//        recyclerView.setAdapter(adapter);
//    }
//    private void getData(){
//
//        for(int i = 1 ; i < 31; i++){
//            DataPlank data = new DataPlank(R.drawable.ic_launcher_foreground, i + "일차");
//            adapter.addItem(data);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()){
//            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
//                finish();
//                return true;
//            }
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }
//}