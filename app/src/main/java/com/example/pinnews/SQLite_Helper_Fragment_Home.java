package com.example.pinnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class SQLite_Helper_Fragment_Home extends SQLiteOpenHelper {
    SQLite_Helper_Fragment_Home(Context context) {
        super(context, "pinnews_db2", null, 2);
        Log.d("hello2", "onCreate: constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("hello", "onCreate: ws");
        db.execSQL("CREATE TABLE IF NOT EXISTS home_news (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, image TEXT, content TEXT, likeText TEXT, shareText TEXT, date_entry TEXT, newstag TEXT, l_temp TEXT, image_slider_array TEXT, category TEXT, views TEXT)");
        setDefaultLabel(db);
    }

    private void setDefaultLabel(SQLiteDatabase db) {
        // create default label
        ContentValues cv = new ContentValues();
        cv.put("title", "Pakistan win");
        cv.put("image", "");
        cv.put("content", "Content");
        cv.put("likeText", "0");
        cv.put("shareText", "0");
        cv.put("date_entry", "2019-07-19");
        cv.put("newstag", "Dawn.com");
        cv.put("t_temp", "0");
        cv.put("l_temp", "0");
        cv.put("image_slider_array", "");
        cv.put("category", "national");
        cv.put("views", "0");
        db.insert("home_news", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean insertData(String[] title, String[] image, String[] content, String[] likeText, String[] shareText, String[] date_entry, String[] newstag, String[] l_temp, ArrayList<String> image_slider_array, String[] category, String[] view) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result_insert = 0;
        ContentValues cv = new ContentValues();
        for (int i = 0; i < title.length; i++) {
            cv.put("title", title[i]);
            cv.put("image", image[i]);
            cv.put("content", content[i]);
            cv.put("likeText", likeText[i]);
            cv.put("shareText", shareText[i]);
            cv.put("date_entry", date_entry[i]);
            cv.put("newstag", newstag[i]);
            cv.put("l_temp", l_temp[i]);
            cv.put("image_slider_array", image_slider_array.get(i));
            cv.put("category", category[i]);
            cv.put("views", view[i]);

            result_insert = db.insert("home_news", null, cv); //Insert each time for loop count
        }
        db.close();
        return result_insert != -1;  // if(result_insert != -1) return false / true
    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT title, image, content, likeText, shareText, date_entry, newstag, l_temp, image_slider_array, category, views FROM home_news ORDER BY id DESC", null);
    }

    public void deleteResult() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM home_news");
    }
}

