package com.world_tech_point.lambebrowser.serviceFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.world_tech_point.lambebrowser.R;


public class DownloadFragment extends Fragment {
    public DownloadFragment() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_download, container, false);


        return root;
    }
}