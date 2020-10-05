package com.world_tech_point.lambebrowser.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.serviceFragment.EarningPointClass;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    TabLayout tabLayout;
    ViewPager viewPager;

    TextView earnPoint, referPoint;
    MembershipSave membershipSave;
    EarningPointClass earningPointClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = findViewById(R.id.walletToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Wallet");

        membershipSave = new MembershipSave(this);
        getPoint(membershipSave.getNumber(),membershipSave.getEmail());

        tabLayout = findViewById(R.id.walletTabLayout);
        viewPager = findViewById(R.id.walletViewPager);
        referPoint = findViewById(R.id.walletReferAmount);
        earnPoint = findViewById(R.id.walletEarningAmount);
        tabLayout.addTab(tabLayout.newTab().setText("Withdraw"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        WalletPagerAdapter walletPagerAdapter = new WalletPagerAdapter(WalletActivity.this.getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(walletPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void getPoint(final String number, final String emailAddress) {
        String url = getString(R.string.BASS_URL)+"getUserProfile";
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
                            String referPoint2 = dataobj.getString("referPoint");
                            String earnPoint2 = dataobj.getString("earnPoint");
                            MembershipSave membershipSave = new MembershipSave(WalletActivity.this);
                            membershipSave.setTowBalance(earnPoint2,referPoint2);
                            referPoint.setText(referPoint2);
                            earnPoint.setText(earnPoint2);
                        }

                    } else {

                        Toast.makeText(WalletActivity.this, "hello point", Toast.LENGTH_SHORT).show();
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
                Params.put("number", number);
                Params.put("emailAddress", emailAddress);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(WalletActivity.this);
        queue.add(stringRequest);
    }


}
