package com.world_tech_point.lambebrowser.openSplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.world_tech_point.lambebrowser.R;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open);



        SplashFragment openFragment = new SplashFragment();
        openFragmentMethod(openFragment);


    }

    private void openFragmentMethod(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.openHost,fragment)
                .commit();

    }


}