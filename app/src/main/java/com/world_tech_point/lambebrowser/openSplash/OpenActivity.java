package com.world_tech_point.lambebrowser.openSplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN = 1500;


    //varriable
    Animation top,bottom;
    ImageView imageView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open);



       /* SplashFragment openFragment = new SplashFragment();
        openFragmentMethod(openFragment);*/



        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        imageView = findViewById(R.id.image1);
        textView = findViewById(R.id.text1);
        imageView.setAnimation(top);
        textView.setAnimation(bottom);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(OpenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);


    }

    private void openFragmentMethod(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.openHost,fragment)
                .commit();

    }


}