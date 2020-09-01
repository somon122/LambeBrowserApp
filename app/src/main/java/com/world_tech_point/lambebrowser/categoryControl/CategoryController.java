package com.world_tech_point.lambebrowser.categoryControl;

import android.content.Context;
import android.content.SharedPreferences;

public class CategoryController {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CategoryController(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("CategoryControl", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void setStoreStatus ( String status){
        editor.putString("status",status);
        editor.commit();
    }

    public String getStoreStatus(){
        String status = sharedPreferences.getString("status",null);
        return status;
    }

    public void delete (){
        editor.clear();
        editor.commit();

    }



}
