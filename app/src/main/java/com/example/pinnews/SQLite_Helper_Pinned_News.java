package com.example.pinnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLite_Helper_Pinned_News extends SQLiteOpenHelper {
    SQLite_Helper_Pinned_News(Context context)
    {
        super(context, "pinnews_db",  null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS saved_News (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, image TEXT, content TEXT, likeText TEXT, shareText TEXT, date_entry TEXT, newstag TEXT, category TEXT)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS home_news (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, image TEXT, content TEXT, likeText TEXT, shareText TEXT, date_entry TEXT, newstag TEXT, t_temp TEXT, l_temp TEXT, image_slider_array TEXT, category TEXT, views TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean insertData(String title, String image, String content, String likeText, String shareText, String date_entry, String newstag, String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("image",image);
        cv.put("content",content);
        cv.put("likeText",likeText);
        cv.put("shareText",shareText);
        cv.put("date_entry",date_entry);
        cv.put("newstag",newstag);
        cv.put("category",category);

        long result_insert = db.insert("saved_News",null,cv);
        return result_insert != -1;  // if(result_insert != -1) return false / true
    }
    Cursor getAllData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT title,image,content,likeText,shareText,date_entry,newstag,category FROM saved_News ORDER BY id DESC", null);
    }
    Integer deleteResult(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("saved_News","title = ?",new String[]{title});
    }
    Cursor getData(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM saved_News WHERE title = ?", new String[] {title});
    }
}
