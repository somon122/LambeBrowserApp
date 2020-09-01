package com.world_tech_point.lambebrowser.serviceFragment;

import android.content.Context;
import android.content.SharedPreferences;

public class MembershipSave {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MembershipSave(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public String getT_wrongAns (){

        String t_wrongAns = sharedPreferences.getString("t_wrongAns","");
        return t_wrongAns;
    }
    public void storeResult (float tResult){

        editor.putFloat("result",tResult);
        editor.commit();

    }


}
