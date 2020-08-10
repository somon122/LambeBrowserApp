package com.world_tech_point.lambebrowser.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DB_Manager {


    DB_helper dbHelper;
    SQLiteDatabase db;


    public DB_Manager(Context context) {
        dbHelper = new DB_helper(context);

    }

    public Boolean Save_All_Data(LinkClass linkClass) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DB_helper.KEY_LINK, linkClass.getLink());
        contentValues.put(DB_helper.KEY_TITLE, linkClass.getTitle());
        contentValues.put(DB_helper.KEY_LOGO, linkClass.getLogo());

        long isInsert = db.insert(DB_helper.VISITED_TABLE, null, contentValues);
        db.close();
        if (isInsert > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<LinkClass>getLinkClassList() {

        ArrayList<LinkClass>dataList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String Query = "Select * from " + DB_helper.VISITED_TABLE;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                String link = cursor.getString(cursor.getColumnIndex(DB_helper.KEY_LINK));
                String title = cursor.getString(cursor.getColumnIndex(DB_helper.KEY_TITLE));
                String logo = cursor.getString(cursor.getColumnIndex(DB_helper.KEY_LOGO));
                LinkClass linkClass = new LinkClass(link,title,logo);
                dataList.add(linkClass);
            } while (cursor.moveToNext());
            db.close();
        }

        return dataList;

    }

    public boolean removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        db = dbHelper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        int d = db.delete(DB_helper.VISITED_TABLE, null, null);
       // db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);

        if (d > 0) {
            return true;
        } else {
            return false;
        }


    }
}
