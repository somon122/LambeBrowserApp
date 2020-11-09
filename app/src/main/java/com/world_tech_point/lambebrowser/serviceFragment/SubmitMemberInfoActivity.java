package com.world_tech_point.lambebrowser.serviceFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubmitMemberInfoActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    String user_type;
    TextInputEditText userName,email, number, bankName, bankNumber, referCode,fbId, instragramId;
    Button submitBtn;
    TextInputLayout bankLayoutName,bankLayoutNumber,fbLayoutId, instragramLayoutId;

    TextWatcher textWatcher;
    TextWatcher textWatcher2;
    int point;

    MembershipSave membershipSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_member);

        Toolbar toolbar = findViewById(R.id.submitToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userName = findViewById(R.id.submitUserName);
        email = findViewById(R.id.submitEmail);
        number = findViewById(R.id.submitNumber);
        bankName = findViewById(R.id.submitBankName);
        bankNumber = findViewById(R.id.submitBankNumber);
        referCode = findViewById(R.id.submitReferCode);
        fbId = findViewById(R.id.submitFbId);
        instragramId = findViewById(R.id.submitInstagramId);
        submitBtn = findViewById(R.id.submitButton);
        bankLayoutName = findViewById(R.id.submitBNameTextInputLayout);
        bankLayoutNumber = findViewById(R.id.submitAccountNumberTextInputLayout);
        fbLayoutId = findViewById(R.id.submitFbTextInputLayout);
        instragramLayoutId = findViewById(R.id.submitInstagramTextInputLayout);
        membershipSave = new MembershipSave(this);

        number.setText(membershipSave.getNumber());
        email.setText(membershipSave.getEmail());
        Toast.makeText(this, ""+membershipSave.getEmail(), Toast.LENGTH_SHORT).show();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            user_type = bundle.getString("userStatus");
            setTitle(user_type);
            if (user_type.equals("Free membership")){
                bankLayoutName.setVisibility(View.GONE);
                bankLayoutNumber.setVisibility(View.GONE);
                fbLayoutId.setVisibility(View.GONE);
                instragramLayoutId.setVisibility(View.GONE);
            }
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("Free membership")){
                   freeSubmit();
                }else {
                    proSubmit();
                }
            }
        });



        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!referCode.getText().toString().isEmpty()){
                    referCodeCheck(s.toString());
                }

            }
        };
        referCode.addTextChangedListener(textWatcher);


         textWatcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!userName.getText().toString().isEmpty()){
                    userNameCheck(s.toString());
                }

            }
        };

        userName.addTextChangedListener(textWatcher2);

        ////===== onCreate is end point ===========
    }




    private void userNameCheck(String toString) {

        checkUserName(toString);


    }


    private void referCodeCheck(String toString) {

        checkReferCode(toString);

    }

    private String random_refer_code(){
        char[] chars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<8;i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void proSubmit() {
        String name = userName.getText().toString().trim();
        String emailAdd = email.getText().toString();
        String phone = number.getText().toString();
        String refer = referCode.getText().toString().trim();
        String fb = fbId.getText().toString();
        String instraGram = instragramId.getText().toString();
        String bName = bankName.getText().toString();
        String bNumber = bankNumber.getText().toString();

        if (name.isEmpty()){
            userName.setError("Please Enter Name");
        }else if (emailAdd.isEmpty()){
            email.setError("Please Enter Email Address");
        }else if (phone.isEmpty()){
            number.setError("Please Enter Number");
        }/*else if (refer.isEmpty()){
            referCode.setError("Please Enter referCode");
        }*/else if (fb.isEmpty()){
            fbId.setError("Please Enter Facebook id");
        }else if (instraGram.isEmpty()){
            instragramId.setError("Please Enter instragram Id");
        }else if (bName.isEmpty()){
            bankName.setError("Please Enter bank Name");
        }else if (bNumber.isEmpty()){
            bankNumber.setError("Please Enter bank Number");
        }else {

            int bonusP = point+500;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            String dateTime = sdf.format(new Date());

            submitUserInfo(name,emailAdd,phone,user_type,bName,bNumber
                    ,fb,instraGram,String.valueOf(bonusP),refer,dateTime);

        }
    }


    private void freeSubmit() {

        String name = userName.getText().toString().trim();
        String emailAdd = email.getText().toString();
        String phone = number.getText().toString();
        String refer = referCode.getText().toString().trim();

        if (name.isEmpty()){
            userName.setError("Please Enter Name");
        }else if (emailAdd.isEmpty()){
            email.setError("Please Enter Email Address");
        }else if (phone.isEmpty()){
            number.setError("Please Enter Number");
        }/*else if (refer.isEmpty()){
            referCode.setError("Please Enter referCode");
        }*/else {

            int bonusP = point+100;
            @SuppressLint
            ("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            String dateTime = sdf.format(new Date());

           submitUserInfo(name,emailAdd,phone,user_type,"",""
                   ,"","", String.valueOf(bonusP),refer,dateTime);

        }

    }

    private void submitUserInfo(final String userName, final String emailAddress, final String number,final String user_type,final String bankName,final String bankAccountNo,
                                final String facebookID,final String instragramID,final String referBonus,final String referCode,final String date_time) {

            String url = getString(R.string.BASS_URL)+"insertUserProfile";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {

                            getUserProfile(number,emailAddress);


                        } else {

                            Toast.makeText(SubmitMemberInfoActivity.this, "hello", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Params = new HashMap<>();
                    Params.put("userName", userName);
                    Params.put("emailAddress", emailAddress);
                    Params.put("number", number);
                    Params.put("user_type", user_type);
                    Params.put("bankName", bankName);
                    Params.put("bankAccountNo", bankAccountNo);
                    Params.put("facebookID", facebookID);
                    Params.put("instragramID", instragramID);
                    Params.put("referCode", referCode);
                    Params.put("referBonus", referBonus);
                    Params.put("newReferCode", random_refer_code());
                    Params.put("date_time", date_time);
                    return Params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(SubmitMemberInfoActivity.this);
            queue.add(stringRequest);
        }


   private void checkReferCode(final String value) {

            String url = getString(R.string.BASS_URL)+"getCheckReferCode";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            referCode.setTextColor(getResources().getColor(R.color.green));
                            getReferPoint(value);

                        } else {
                            referCode.setTextColor(getResources().getColor(R.color.red));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Params = new HashMap<>();
                    Params.put("referCode", value);
                    return Params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(SubmitMemberInfoActivity.this);
            queue.add(stringRequest);
        }

    private void getReferPoint(final String value) {

            String url = getString(R.string.BASS_URL) + "getReferPoint";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            String res = obj.getString("user");
                            JSONArray jsonArray = new JSONArray(res);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataobj = jsonArray.getJSONObject(i);
                                String referPoint = dataobj.getString("referPoint");
                                if (referPoint.equals("")){
                                    point =0;
                                    Toast.makeText(SubmitMemberInfoActivity.this, ""+point, Toast.LENGTH_SHORT).show();

                                }else {
                                    point = Integer.parseInt(referPoint);
                                    Toast.makeText(SubmitMemberInfoActivity.this, ""+point, Toast.LENGTH_SHORT).show();
                                }
                                point = Integer.parseInt(referPoint);
                                Toast.makeText(SubmitMemberInfoActivity.this, ""+point, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                             point =0;
                            Toast.makeText(SubmitMemberInfoActivity.this, ""+point, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //netAlert();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //netAlert();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Params = new HashMap<>();
                    Params.put("referCode", value);
                    return Params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(SubmitMemberInfoActivity.this);
            queue.add(stringRequest);



    }


    private void checkUserName(final String value) {

            String url = getString(R.string.BASS_URL)+"getCheckUserName";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {

                            userName.setTextColor(getResources().getColor(R.color.red));
                        } else {

                            userName.setTextColor(getResources().getColor(R.color.green));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Params = new HashMap<>();
                    Params.put("userName", value);
                    return Params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(SubmitMemberInfoActivity.this);
            queue.add(stringRequest);
        }


    private void getUserProfile(final String number , final String emailAddress) {

        String url = getString(R.string.BASS_URL) + "getUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);

                            String userId = dataobj.getString("id");
                            String userName = dataobj.getString("userName");
                            String emailAddress = dataobj.getString("emailAddress");
                            String number = dataobj.getString("number");
                            String user_type = dataobj.getString("user_type");
                            String add_fee_status = dataobj.getString("add_fee_status");
                            String bankName = dataobj.getString("bankName");
                            String bankAccountNo = dataobj.getString("bankAccountNo");
                            String facebookID = dataobj.getString("facebookID");
                            String instragramID = dataobj.getString("instragramID");
                            String referCode = dataobj.getString("referCode");

                            if (userName.equals("")){

                                Toast.makeText(SubmitMemberInfoActivity.this, "Empty", Toast.LENGTH_SHORT).show();


                            }else {

                                membershipSave.saveUserInfo(Integer.parseInt(userId),userName,emailAddress,number,user_type,add_fee_status,bankName,bankAccountNo,
                                        facebookID,instragramID,referCode);
                                startActivity(new Intent(SubmitMemberInfoActivity.this, MainActivity.class));
                                finish();
                            }

                        }

                    } else {

                        Toast.makeText(SubmitMemberInfoActivity.this, "Problem", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                Params.put("emailAddress", emailAddress);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SubmitMemberInfoActivity.this);
        queue.add(stringRequest);

    }


}