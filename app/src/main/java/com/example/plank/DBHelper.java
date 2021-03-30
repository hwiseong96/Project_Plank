package com.example.plank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context){

        super(context,"memo.db",null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table HistoryTable("
                +"idx integer primary key autoincrement, "
                +"memoData text, "//메모
                +"intData integer, "//진행일
                +"intData2 integer, "//진행 플랭크 번호
                +"dateData date not null, "
                +"boolData Integer not null default 1,"
                +"boolData2 Integer not null default 1, "
                +"ClearData Integer not null default 1 "
                +")";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        switch (i){

            case 1:

        }

    }
//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.disableWriteAheadLogging();
//    }
}
