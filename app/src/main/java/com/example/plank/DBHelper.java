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
                +"textData text, "
                +"intData integer, "
                +"dateData date not null"
                +")";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        switch (i){

            case 1:

        }

    }
}
