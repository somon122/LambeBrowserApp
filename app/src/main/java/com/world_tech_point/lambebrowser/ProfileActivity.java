package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextView name, email, number,userType, paidStatus,  referCode, bankName, bankAcNo, earnPointTV, referPointTV;
    MembershipSave membershipSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profileTooBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Profile");
        membershipSave = new MembershipSave(this);

        name = findViewById(R.id.profileUserName);
        email = findViewById(R.id.profileEmail);
        number = findViewById(R.id.profileNumber);

        paidStatus = findViewById(R.id.profilePaidStatus);
        userType = findViewById(R.id.profileUserType);

        referCode = findViewById(R.id.profileReferCode);
        bankName = findViewById(R.id.profileBankName);
        bankAcNo = findViewById(R.id.profileBankNo);

        earnPointTV = findViewById(R.id.profileEarnPoint);
        referPointTV = findViewById(R.id.profileReferPoint);

        name.setText(membershipSave.getUserName());
        email.setText(membershipSave.getEmail());
        number.setText(membershipSave.getNumber());

        paidStatus.setText(membershipSave.getAdd_fee_status());
        userType.setText(membershipSave.getUser_type());

        referCode.setText(membershipSave.getReferCode());
        bankName.setText(membershipSave.getBankName());
        bankAcNo.setText(membershipSave.getBankAccountNo());

        getUserProfile(membershipSave.getNumber(),membershipSave.getEmail());

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
                            //String userId = dataobj.getString("id");
                            String referPoint = dataobj.getString("referPoint");
                            String earnPoint = dataobj.getString("earnPoint");
                            earnPointTV.setText("Earn Point: "+earnPoint);
                            referPointTV.setText("Refer Point: "+referPoint);

                        }
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
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(stringRequest);
    }
}