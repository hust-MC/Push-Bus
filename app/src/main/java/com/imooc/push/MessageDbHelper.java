package com.imooc.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDbHelper extends SQLiteOpenHelper {
    static final String CONTENT = "content";

    public MessageDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //用于创建Book表
    private static final String CREATE_JPUSH = "create table JPush ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text)";
    //用于创建Category表
    private static final String CREATE_UPUSH = "create table UPush ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text)";

    //第一次创建数据库时会调用此方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JPUSH);
        db.execSQL(CREATE_UPUSH);
    }


    //数据库版本升级时会调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void insert(TableName table, String content) {
        ContentValues values = new ContentValues();
        values.put(CONTENT, content);
        getWritableDatabase().insert(table.toString(), null, values);
    }

    List<Map<String, String>> query(TableName tableName) {
        List<Map<String, String>> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tableName.toString(), null, null, null, null, null, null);
        while (c.moveToNext()) {
            String content = c.getString(c.getColumnIndex(CONTENT));
            Map<String, String> map = new HashMap<>();
            map.put(CONTENT, content);
            Log.d("MC", "content = " + content);
            list.add(map);
        }
        c.close();
        return list;
    }

    public void clear(TableName tableName) {
        getWritableDatabase().delete(tableName.toString(), null, null);
    }

    enum TableName {
        JPush,
        UPush
    }
}
