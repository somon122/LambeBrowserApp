package com.world_tech_point.lambebrowser.videoShowFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.world_tech_point.lambebrowser.R;

import java.util.ArrayList;
import java.util.List;

public class VideoShowActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private List<VideoClass> _videos;
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_show);

        Toolbar toolbar = findViewById(R.id.videoListToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Video list");


        _videos = new ArrayList<>();
        recyclerView = findViewById(R.id.VideoRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        loadSongs();


    }
    private void loadSongs(){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
       // String selection = MediaStore.Video.Media.IS+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                    VideoClass s = new VideoClass(name,artist,url);
                    _videos.add(s);

                }while (cursor.moveToNext());
            }

            cursor.close();
            videoAdapter = new VideoAdapter(VideoShowActivity.this,_videos);
            recyclerView.setAdapter(videoAdapter);

        }
    }

}