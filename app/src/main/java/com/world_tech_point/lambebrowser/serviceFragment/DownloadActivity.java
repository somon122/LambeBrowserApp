package com.world_tech_point.lambebrowser.serviceFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.world_tech_point.lambebrowser.R;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

public class DownloadActivity extends AppCompatActivity {


    public ProgressBar progressBar;
    private AsynckT asynckT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

      /*  asynckT = new AsynckT(this);
        progressBar = findViewById(R.id.downloadProgressBar);
        asynckT.execute(10);*/



    }
}