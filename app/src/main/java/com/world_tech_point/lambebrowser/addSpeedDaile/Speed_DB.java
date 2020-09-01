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
                String title = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_TITLE));
                String logo = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_LOGO));
                String link = cursor.getString(cursor.getColumnIndex(Speed_DB_Helper.KEY_LINK));
                SpeedDialClass speedDialClass = new SpeedDialClass(title,logo,link);
                dataList.add(speedDialClass);
            } while (cursor.moveToNext());
            db.close();
        }

        return dataList;

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
