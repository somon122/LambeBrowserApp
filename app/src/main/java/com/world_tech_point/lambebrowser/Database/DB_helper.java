package com.world_tech_point.lambebrowser.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DB_helper extends SQLiteOpenHelper {

    private Context context;

    public static final String DB_NAME = "Visited_db";
    public static final int VERSION = 1;

    public static final String VISITED_TABLE = "Visited_Details";

    public static final String KEY_ID = "Id";
    public static final String KEY_LINK = "link";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LOGO = "logo";

    private static final String Create_Table = "CREATE TABLE " + VISITED_TABLE + "( " + KEY_ID + " INTEGER  PRIMARY KEY ," + KEY_LINK + " TEXT, " +
            "" + KEY_TITLE +" TEXT, " + KEY_LOGO +" TEXT )";


    public DB_helper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(Create_Table);
            //Toast.makeText(context, "onCreate is called", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + VISITED_TABLE);

        onCreate(db);


    }


}
