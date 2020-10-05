package com.world_tech_point.lambebrowser.wallet;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WithdrawFragment extends Fragment {

    TextInputEditText amount;
    Button submitBtn;
    MembershipSave membershipSave;
    View root;
    String value = "";
    RadioButton storeB;
    RadioGroup radioGroup;
    String dateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_withdraw, container, false);


        amount = root.findViewById(R.id.submitWithdrawAmount);
        submitBtn = root.findViewById(R.id.submitWithdrawButton);
        membershipSave = new MembershipSave(getContext());
        radioGroup = root.findViewById(R.id.withdrawRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                storeB = root.findViewById(checkedId);
                value = storeB.getText().toString();
            }
        });

        @SuppressLint
                ("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        dateTime = sdf.format(new Date());


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = amount.getText().toString().trim();

                if (a.isEmpty()){
                    amount.setError("please Enter Amount");
                }else if (value.equals("")){
                    Toast.makeText(getContext(), "Please select wallet Option", Toast.LENGTH_SHORT).show();
                }else {


                int rMoney = Integer.parseInt(a);
                int referPoint = Integer.parseInt(membershipSave.getReferPoint());
                int earnPoint = Integer.parseInt(membershipSave.getEarnPoint());

                if (rMoney >= 5000) {

                    if (value.equals("Refer Point")) {

                        if (rMoney >= referPoint) {

                            int lastPoint = referPoint - rMoney;

                            updateEarnPoint(membershipSave.getReferCode(),membershipSave.getReferPoint(), String.valueOf(lastPoint), value, String.valueOf(rMoney));

                        } else {

                            Toast.makeText(getContext(), "Sorry Not enough balance of Refer Point", Toast.LENGTH_SHORT).show();
                        }

                    } else if (value.equals("Earn point")) {

                        if (rMoney >= earnPoint) {

                            int lastPoint = earnPoint - rMoney;

                            updateEarnPoint(membershipSave.getReferCode(),membershipSave.getReferPoint(), String.valueOf(lastPoint), value, String.valueOf(rMoney));

                        } else {

                            Toast.makeText(getContext(), "Sorry Not enough balance Earn point", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {

                    Toast.makeText(getContext(), "Sorry Not enough balance", Toast.LENGTH_SHORT).show();
                }


                }

            }
        });


        return root;
    }


    public void updateEarnPoint(final String referCode, final String referPoint, final String earnPoint, final String w_type, final String amount) {
        String url = getString(R.string.BASS_URL) + "updateUserPoint";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        InsertWithdraw(String.valueOf(membershipSave.getId()), membershipSave.getUserName(),
                                membershipSave.getBankName(), membershipSave.getBankAccountNo(), amount, w_type, "Pending", dateTime);

                    } else {
                        Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();

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
                Params.put("referCode", referCode);
                Params.put("earnPoint", earnPoint);
                Params.put("referPoint", referPoint);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void InsertWithdraw(final String id, final String userName, final String bankName, final String bankAccountNo, final String amount, final String w_type, final String pending, final String dateTime) {


        String url = getString(R.string.BASS_URL) + "insertWithdraw";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Field", Toast.LENGTH_SHORT).show();
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
                Params.put("userId", id);
                Params.put("userName", userName);
                Params.put("bankName", bankName);
                Params.put("bankAccountNo", bankAccountNo);
                Params.put("amount", amount);
                Params.put("w_type", w_type);
                Params.put("status", pending);
                Params.put("date_time", dateTime);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }


}