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


    public void saveUserInfo (int id,String userName, String email, String user_type, String add_fee_status, String number,
                           String referCode,String bankName, String bankAccountNo, String facebookID, String instragramID){

        editor.putInt("id",id);
        editor.putString("number",number);
        editor.putString("email",email);
        editor.putString("userName",userName);
        editor.putString("user_type",user_type);
        editor.putString("add_fee_status",add_fee_status);
        editor.putString("referCode",referCode);
        editor.putString("bankName",bankName);
        editor.putString("bankAccountNo",bankAccountNo);
        editor.putString("facebookID",facebookID);
        editor.putString("instragramID",instragramID);

        editor.commit();

    }

    public void setTowBalance (String earnPoint, String referPoint){

        editor.putString("earnPoint",earnPoint);
        editor.putString("referPoint",referPoint);
        editor.commit();
    }

    public String getEarnPoint (){

        String earnPoint = sharedPreferences.getString("earnPoint","0");
        return earnPoint;
    }
    public String getReferPoint (){

        String referPoint = sharedPreferences.getString("referPoint","0");
        return referPoint;
    }



    public void setEmailNumber (String email, String number){

        editor.putString("email",email);
        editor.putString("number",number);
        editor.commit();
    }


    public int getId (){

        int id = sharedPreferences.getInt("id",0);
        return id;
    }

    public String getEmail (){

        String email = sharedPreferences.getString("email","");
        return email;
    }
    public String getNumber (){

        String number = sharedPreferences.getString("number","");
        return number;
    }
    public String getUserName (){

        String userName = sharedPreferences.getString("userName","");
        return userName;
    }
    public String getUser_type (){

        String user_type = sharedPreferences.getString("user_type","");
        return user_type;
    }
    public String getAdd_fee_status (){

        String add_fee_status = sharedPreferences.getString("add_fee_status","");
        return add_fee_status;
    }
    public String getReferCode (){

        String referCode = sharedPreferences.getString("referCode","");
        return referCode;
    }
    public String getBankName (){

        String bankName = sharedPreferences.getString("bankName","");
        return bankName;
    }
    public String getBankAccountNo (){

        String bankAccountNo = sharedPreferences.getString("bankAccountNo","");
        return bankAccountNo;
    }
    public String getFacebookID (){

        String facebookID = sharedPreferences.getString("facebookID","");
        return facebookID;
    }
    public String getInstragramID (){

        String instragramID = sharedPreferences.getString("instragramID","");
        return instragramID;
    }


    public  void delete(){
        editor.clear();
        editor.commit();

    }


}
