package com.world_tech_point.lambebrowser.serviceFragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.CodeBoy.MediaFacer.AudioGet;
import com.CodeBoy.MediaFacer.MediaFacer;
import com.CodeBoy.MediaFacer.mediaHolders.audioContent;
import com.example.simplefileexplorer.SimpleFileExplorerActivity;
import com.example.simplefileexplorer.SimpleFileResources;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.world_tech_point.lambebrowser.HistoryActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.WebViewActivity;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import java.util.ArrayList;


public class FilesFragment extends Fragment {


    private final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int file_explorer_request_result_code = 22;
    private final int permissions_request_code = 23;
    LinearLayout file, exits,mp3,video,wallet,history;
    AdView adView;

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

        adView = root.findViewById(R.id.fileBannerAds);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),SimpleFileExplorerActivity.class);
                intent.putExtra(SimpleFileExplorerActivity.ENABLE_DIRECTORY_SELECT_KEY, false);
                startActivityForResult(intent, file_explorer_request_result_code);

            }
        });

        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitsUser();
            }
        });
        mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MP3_PlayActivity.class));
            }
        });
       video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VideoShowActivity.class));
            }
        });
       wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WalletActivity.class));
            }
        });
       history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });

        return  root;

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