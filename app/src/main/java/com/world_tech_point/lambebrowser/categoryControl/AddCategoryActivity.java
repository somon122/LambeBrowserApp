package com.world_tech_point.lambebrowser.categoryControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;

public class AddCategoryActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            categoryController.delete();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryController.delete();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        categoryController.delete();
    }

    PageViewerAdapter pageViewerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    CategoryController categoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        Toolbar toolbar = findViewById(R.id.addSpeedToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryController=new CategoryController(this);
        setTitle(categoryController.getStoreStatus());


        viewPager = findViewById(R.id.addSpeedViewPager_id);
        tabLayout = findViewById(R.id.addSpeedTabLayout_id);

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

        pageViewerAdapter = new PageViewerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
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

    }
}