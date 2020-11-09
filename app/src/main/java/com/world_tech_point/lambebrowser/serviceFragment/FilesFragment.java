package com.world_tech_point.lambebrowser.serviceFragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.simplefileexplorer.SimpleFileExplorerActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.world_tech_point.lambebrowser.HistoryActivity;
import com.world_tech_point.lambebrowser.ProfileActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.SettingActivity;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class FilesFragment extends Fragment {


    private final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int file_explorer_request_result_code = 22;
    private final int permissions_request_code = 23;
    LinearLayout file, exits,mp3,video,wallet,history;
    AdView adView;
    private InterstitialAd mInterstitialAd;
    String value=null;
    MembershipSave membershipSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_files, container, false);

        file = root.findViewById(R.id.File_id);
        exits = root.findViewById(R.id.fileExits_id);
        mp3 = root.findViewById(R.id.fileMp3Song_id);
        video = root.findViewById(R.id.fileVideo_id);
        wallet = root.findViewById(R.id.fileWallet_id);
        history = root.findViewById(R.id.fileHistory_id);
        membershipSave = new MembershipSave(getContext());

        adView = root.findViewById(R.id.fileBannerAds);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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

                if (membershipSave.getAdd_fee_status().equals("User_paid")){
                    taskPointAdd(membershipSave.getReferCode(),"5");
                }else {
                    taskPointAdd(membershipSave.getReferCode(),"1");
                }
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "file";
                    mInterstitialAd.show();
                }else {
                    value = "file";
                    actionMethod(value);

                }


            }
        });

        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "exits";
                    mInterstitialAd.show();
                }else {
                    value = "exits";
                    actionMethod(value);
                }
            }
        });
        mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "mp3";
                    mInterstitialAd.show();
                }else {
                    value = "mp3";
                    actionMethod(value);
                }
            }
        });
       video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "video";
                    mInterstitialAd.show();
                }else {
                    value = "video";
                    actionMethod(value);
                }
            }
        });
       wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "wallet";
                    mInterstitialAd.show();
                }else {
                    value = "wallet";
                    actionMethod(value);
                }
            }
        });
       history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()){
                    value = "history";
                    mInterstitialAd.show();
                }else {
                    value = "history";
                    actionMethod(value);
                }
            }
        });

        return  root;

    }

    private void taskPointAdd(final String refer_code, final String new_point) {
        String url = getString(R.string.BASS_URL)+ "add_tasks_point";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("response").equals("point_added")) {
                        actionMethod(value);
                    }else if (obj.getString("response").equals("point_not_added")){
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

        if (value.equals("file")){
            Intent intent = new Intent(getContext(),SimpleFileExplorerActivity.class);
            intent.putExtra(SimpleFileExplorerActivity.ENABLE_DIRECTORY_SELECT_KEY, false);
            startActivityForResult(intent, file_explorer_request_result_code);

        }else if (value.equals("exits")){
            exitsUser();

        }else if (value.equals("mp3")){
            startActivity(new Intent(getContext(), MP3_PlayActivity.class));

        }else if (value.equals("video")){
            startActivity(new Intent(getContext(), VideoShowActivity.class));

        }else if (value.equals("wallet")){
            startActivity(new Intent(getContext(), WalletActivity.class));

        }else if (value.equals("history")){
            startActivity(new Intent(getContext(), HistoryActivity.class));
        }

    }

    private void exitsUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Exits Alert");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


      @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if(data != null){
            String selectedAbsolutePath = data.getStringExtra(SimpleFileExplorerActivity.ON_ACTIVITY_RESULT_KEY);
            Toast.makeText(getContext(), selectedAbsolutePath, Toast.LENGTH_SHORT).show();

            if (selectedAbsolutePath.contains(".mp4") && selectedAbsolutePath.contains(".3gp")){

                Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("video_url", selectedAbsolutePath);
                startActivity(intent);
            }else if (selectedAbsolutePath.contains(".pdf")){

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedAbsolutePath));
                startActivity(browserIntent);

            }else if (selectedAbsolutePath.contains(".mp3")){

                Toast.makeText(getContext(), ""+selectedAbsolutePath, Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getContext(), ""+selectedAbsolutePath, Toast.LENGTH_SHORT).show();

            }


        }
    }
}