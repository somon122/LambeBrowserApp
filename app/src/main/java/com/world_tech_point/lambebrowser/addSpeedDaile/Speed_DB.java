package com.world_tech_point.lambebrowser.addSpeedDaile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.world_tech_point.lambebrowser.Database.DB_helper;
import com.world_tech_point.lambebrowser.Database.LinkClass;

import java.util.ArrayList;
import java.util.List;

public class Speed_DB {


    Speed_DB_Helper helper;
    SQLiteDatabase db;
    String links;


    public Speed_DB(Context context) {
        helper = new Speed_DB_Helper(context);

    }

    public Boolean Save_All_Data(SpeedDialClass speedDialClass) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Speed_DB_Helper.KEY_TITLE, speedDialClass.getName());
        contentValues.put(Speed_DB_Helper.KEY_LOGO, speedDialClass.getImageURL());
        contentValues.put(Speed_DB_Helper.KEY_LINK, speedDialClass.getSiteURL());

        long isInsert = db.insert(Speed_DB_Helper.VISITED_TABLE, null, contentValues);
        db.close();
        if (isInsert > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<SpeedDialClass> getLinkClassList() {

        ArrayList<SpeedDialClass> dataList = new ArrayList<>();
        db = helper.getReadableDatabase();

        String Query = "Select * from " + Speed_DB_Helper.VISITED_TABLE;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Speed_DB_Helper.KEY_ID));
                String title = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_TITLE));
                String logo = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_LOGO));
                String link = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_LINK));
                SpeedDialClass speedDialClass = new SpeedDialClass(id,title,logo,link);
                dataList.add(speedDialClass);
            } while (cursor.moveToNext());
            db.close();
        }

        return dataList;

    }

    public String getData(String link) {

        db = helper.getReadableDatabase();
        String Query = "Select * from " + Speed_DB_Helper.VISITED_TABLE + " where " + Speed_DB_Helper.KEY_LINK + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{link});
        if (cursor.moveToFirst()) {
            do {
                links = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_LINK));
            } while (cursor.moveToNext());
            db.close();
        }
        return links;
    }


    public boolean Delete_Speed_Data (int rowId) {

        db = helper.getWritableDatabase();
        int isDeleted = db.delete(Speed_DB_Helper.VISITED_TABLE,Speed_DB_Helper.KEY_ID+" = ?",new String[]{Integer.toString(rowId)});

        db.close();
        if (isDeleted > 0)
        {
            return true;
        }else {
            return  false;
        }
    }

    public boolean removeAll()
    {

        db = helper.getWritableDatabase();
        int d = db.delete(Speed_DB_Helper.VISITED_TABLE, null, null);
        if (d > 0) {
            return true;
        } else {
            return false;
        }


    }
}
