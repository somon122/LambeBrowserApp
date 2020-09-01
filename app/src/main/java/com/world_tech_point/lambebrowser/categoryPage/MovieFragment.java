package com.world_tech_point.lambebrowser.categoryPage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialClass;
import com.world_tech_point.lambebrowser.categoryControl.AddSpeedDialAdapter;
import com.world_tech_point.lambebrowser.categoryControl.BlogShowAdapter;
import com.world_tech_point.lambebrowser.categoryControl.BlogShowClass;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieFragment extends Fragment {


    RecyclerView recyclerView;
    BlogShowAdapter blogShowAdapter;
    List<BlogShowClass> blogShowClassList;

    AddSpeedDialAdapter addSpeedDialAdapter;
    List<SpeedDialClass> speedDialClassList;
    CategoryController categoryController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_movie, container, false);

        recyclerView = root.findViewById(R.id.categoryMovieRecyclerView);
        categoryController = new CategoryController(getContext());

        if (categoryController.getStoreStatus().equals("Blog")){

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            recyclerView.setHasFixedSize(true);
            blogShowClassList = new ArrayList<>();
            blogList();

        }else {

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
            recyclerView.setHasFixedSize(true);
            speedDialClassList = new ArrayList<>();
            speedDialList();

        }



        return root;
    }

    private void blogList() {

        String url = getString(R.string.BASS_URL) + "getBlogCategory";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        //progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            BlogShowClass blogShowClass = new BlogShowClass();
                            blogShowClass.setId(dataobj.getInt("id"));
                            blogShowClass.setCategory(dataobj.getString("category"));
                            blogShowClass.setTitle(dataobj.getString("title"));
                            blogShowClass.setImage(dataobj.getString("image"));
                            blogShowClass.setSite_url(dataobj.getString("site_url"));
                            blogShowClassList.add(blogShowClass);
                        }
                        blogShowAdapter = new BlogShowAdapter(getActivity(), blogShowClassList);
                        recyclerView.setAdapter(blogShowAdapter);
                        blogShowAdapter.notifyDataSetChanged();

                    } else {

                        noDataAlert();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("category", "Movie");
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }


    private void speedDialList() {

        String url = getString(R.string.BASS_URL) + "getCategory";
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
                            SpeedDialClass speedDialClass = new SpeedDialClass();
                            speedDialClass.setName(dataobj.getString("title"));
                            speedDialClass.setImageURL(dataobj.getString("image"));
                            speedDialClass.setSiteURL(dataobj.getString("site_url"));
                            speedDialClassList.add(speedDialClass);
                        }
                        addSpeedDialAdapter = new AddSpeedDialAdapter(getActivity(), speedDialClassList);
                        recyclerView.setAdapter(addSpeedDialAdapter);
                        addSpeedDialAdapter.notifyDataSetChanged();

                    } else {

                        noDataAlert();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("category", "Movie");
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("No data found")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("Please make sure your net connection")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}