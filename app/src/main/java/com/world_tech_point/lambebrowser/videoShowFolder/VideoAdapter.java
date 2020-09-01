package com.world_tech_point.lambebrowser.videoShowFolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.mp3Folder.SongAdapter;
import com.world_tech_point.lambebrowser.mp3Folder.SongInfo;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.SongHolder> {


    private List<VideoClass> _songs;
    private Context context;

    public VideoAdapter(Context context, List<VideoClass> songs) {
        this.context = context;
        this._songs = songs;
    }



    @Override
    public VideoAdapter.SongHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.video_songs,viewGroup,false);
        return new VideoAdapter.SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.SongHolder songHolder, final int i) {
        final VideoClass s = _songs.get(i);
        songHolder.name.setText(_songs.get(i).getSongname());
        Picasso.get().load(s.getSongUrl()).placeholder(R.drawable.music).fit().into(songHolder.imageView);

        songHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final VideoClass s = _songs.get(i);
                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.putExtra("video_url",s.getSongUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        ImageView imageView,btnAction;
        TextView name;
        public SongHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.videoName);
            imageView = itemView.findViewById(R.id.videoImage);
            btnAction = itemView.findViewById(R.id.videoPlayBtn);
        }
    }
}
