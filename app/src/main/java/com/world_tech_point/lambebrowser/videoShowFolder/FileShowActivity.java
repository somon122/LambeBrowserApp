package com.world_tech_point.lambebrowser.videoShowFolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.SeekBar;

import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.mp3Folder.SongAdapter;
import com.world_tech_point.lambebrowser.mp3Folder.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class FileShowActivity extends AppCompatActivity {

    private List<VideoClass> _videos;
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_show);

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
            videoAdapter = new VideoAdapter(FileShowActivity.this,_videos);
            recyclerView.setAdapter(videoAdapter);

        }
    }

}