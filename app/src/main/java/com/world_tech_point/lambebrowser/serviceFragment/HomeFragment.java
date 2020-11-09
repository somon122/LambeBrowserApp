package com.world_tech_point.lambebrowser.serviceFragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.world_tech_point.lambebrowser.HistoryActivity;
import com.world_tech_point.lambebrowser.ProfileActivity;
import com.world_tech_point.lambebrowser.SettingActivity;
import com.world_tech_point.lambebrowser.SuggestionClass;
import com.world_tech_point.lambebrowser.categoryControl.AddCategoryActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.WebViewActivity;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialAdapter;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialClass;
import com.world_tech_point.lambebrowser.addSpeedDaile.Speed_DB;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;
import com.world_tech_point.lambebrowser.categoryControl.PageViewerAdapter;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {


    LinearLayout google, facebook, youTube, searchUrl, addSpeedDial, visitedLinearLayout;
    AutoCompleteTextView autoCompleteTextView;
    RecyclerView speedDialRecyclerView;
    List<SpeedDialClass> dialClassList;
    Speed_DB speed_db;
    CategoryController categoryController;
    List<String> sList;
    PageViewerAdapter pageViewerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private InterstitialAd mInterstitialAd;
    MembershipSave membershipSave;

    String value = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);


        categoryController = new CategoryController(getContext());
        google = root.findViewById(R.id.google_id);
        facebook = root.findViewById(R.id.facebook_id);
        youTube = root.findViewById(R.id.youTube_id);
        searchUrl = root.findViewById(R.id.searchUrl_id);
        autoCompleteTextView = root.findViewById(R.id.urlEditText_id);
        membershipSave = new MembershipSave(getContext());

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.mInterstitialAdsId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdImpression() {
            }

            @Override
            public void onAdClosed() {

                if (membershipSave.getAdd_fee_status().equals("User_paid")) {
                    taskPointAdd(membershipSave.getReferCode(), "5");
                } else {
                    taskPointAdd(membershipSave.getReferCode(), "1");
                }
                mInterstitialAd.loadAd(new AdRequest.Builder().build());


            }
        });


        visitedLinearLayout = root.findViewById(R.id.visitedLinearLayout);
        speedDialRecyclerView = root.findViewById(R.id.speedDialRecyclerView);
        speedDialRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        speedDialRecyclerView.setHasFixedSize(true);
        dialClassList = new ArrayList<>();

        speed_db = new Speed_DB(getContext());
        dialClassList = speed_db.getLinkClassList();

        SpeedDialAdapter speedDialAdapter = new SpeedDialAdapter(getContext(), dialClassList);
        speedDialRecyclerView.setAdapter(speedDialAdapter);
        speedDialAdapter.notifyDataSetChanged();

        addSpeedDial = root.findViewById(R.id.addSpeedDial);

        SuggestionClass suggestionClass = new SuggestionClass(getContext());
        sList = suggestionClass.suggestionList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sList);
        autoCompleteTextView.setAdapter(arrayAdapter);


        viewPager = root.findViewById(R.id.homeViewPager_id);
        tabLayout = root.findViewById(R.id.homeTabLayout_id);

        tabLayout.addTab(tabLayout.newTab().setText("Politics"));
        tabLayout.addTab(tabLayout.newTab().setText("Funny"));
        tabLayout.addTab(tabLayout.newTab().setText("Movie"));
        tabLayout.addTab(tabLayout.newTab().setText("Fashion"));

        tabLayout.addTab(tabLayout.newTab().setText("LifeStyle"));
        tabLayout.addTab(tabLayout.newTab().setText("Sports"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Health"));

        tabLayout.addTab(tabLayout.newTab().setText("Crime"));
        tabLayout.addTab(tabLayout.newTab().setText("Automobile"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pageViewerAdapter = new PageViewerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageViewerAdapter);
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


        addSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    value = "addSpeedDial";
                    mInterstitialAd.show();
                } else {
                    value = "addSpeedDial";
                    actionMethod(value);
                }

            }
        });
        searchUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = autoCompleteTextView.getText().toString().trim();

                if (url.isEmpty()) {
                    autoCompleteTextView.setError("Enter valid address");

                } else {
                    if (url.contains("www")) {

                        String lastUrl = "https://" + url;
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", lastUrl);
                        startActivity(intent);

                    } else if (url.contains("https")) {

                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);

                    } else {
                        String lastUrl = "https://www.google.com/search?q=" + url;
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", lastUrl);
                        startActivity(intent);

                    }

                }


            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    value = "facebook";
                    mInterstitialAd.show();
                } else {
                    value = "facebook";
                    actionMethod(value);
                }
            }
        });
        youTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    value = "youTube";
                    mInterstitialAd.show();
                } else {
                    value = "youTube";
                    actionMethod(value);
                }
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    value = "google";
                    mInterstitialAd.show();
                } else {
                    value = "google";
                    actionMethod(value);
                }
            }
        });
        return root;
    }

    private void fragmentSet(Fragment fragment) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hostFragment, fragment)
                .commit();

    }

    private void customSearch(String url) {

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);


    }

    private void taskPointAdd(final String refer_code, final String new_point) {
        String url = getString(R.string.BASS_URL) + "add_tasks_point";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("response").equals("point_added")) {
                        actionMethod(value);
                    } else if (obj.getString("response").equals("point_not_added")) {
                        actionMethod(value);
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
                Params.put("referCode", refer_code);
                Params.put("new_point", new_point);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void actionMethod(String value) {

        if (value.equals("addSpeedDial")) {
            categoryController.delete();
            categoryController.setStoreStatus("Add Speed Dial");
            startActivity(new Intent(getContext(), AddCategoryActivity.class));
            getActivity().finish();

        } else if (value.equals("facebook")) {
            customSearch("https://www.facebook.com/");
        } else if (value.equals("youTube")) {
            customSearch("https://www.youtube.com/");

        } else if (value.equals("google")) {
            customSearch("https://www.google.com/");
        }

    }


}


