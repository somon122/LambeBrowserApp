package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.world_tech_point.lambebrowser.Database.DB_Manager;
import com.world_tech_point.lambebrowser.Database.LinkClass;
import com.world_tech_point.lambebrowser.serviceFragment.HomeFragment;


import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    RecyclerView recyclerView;
    List<LinkClass> visitedClassList;
    DB_Manager db_manager;
    ImageView deleteHistoryData;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.historyToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("History");

        adView = new AdView(this, getString(R.string.faceBookBannerId), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.historyBanner_container);
        adContainer.addView(adView);
        adView.loadAd();

        deleteHistoryData = findViewById(R.id.deleteHistoryData_id);
        recyclerView =findViewById(R.id.mostVisitedRecyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        visitedClassList = new ArrayList<>();
        db_manager = new DB_Manager(this);
        visitedClassList=db_manager.getLinkClassList();
        VisitedAdapter visitedAdapter = new VisitedAdapter(this,visitedClassList);
        recyclerView.setAdapter(visitedAdapter);
        visitedAdapter.notifyDataSetChanged();
        deleteHistoryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVisited();
            }
        });

    }

    private void deleteVisited() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                boolean delete =  db_manager.removeAll();
                if (delete){
                  startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                  finish();
                }else {
                    Toast.makeText(HistoryActivity.this, "Already Clear", Toast.LENGTH_SHORT).show();
                }


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

}