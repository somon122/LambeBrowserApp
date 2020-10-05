package com.world_tech_point.lambebrowser.mp3Folder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.world_tech_point.lambebrowser.R;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private List<SongInfo> _songs;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public SongAdapter(Context context, List<SongInfo> songs) {
        this.context = context;
        this._songs = songs;
    }

    public interface OnItemClickListener {
        void onItemClick(Button b, View view, SongInfo obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.row_songs,viewGroup,false);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(final SongHolder songHolder, final int i) {
            final SongInfo s = _songs.get(i);
            songHolder.tvSongName.setText(_songs.get(i).getSongname());
            songHolder.tvSongArtist.setText(_songs.get(i).getArtistname());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(s.getSongUrl());
        byte [] data = mmr.getEmbeddedPicture();
        Bitmap bitmap = null;
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
           // songHolder.mp3Image.setImageBitmap(bitmap);
        }
        Glide.with(context).asBitmap().centerCrop().placeholder(R.drawable.music).load(bitmap).into(songHolder.mp3Image);
            songHolder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(songHolder.btnAction,v, s, i);
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSongName,tvSongArtist;
        Button btnAction;
        ImageView mp3Image;
        public SongHolder(View itemView) {
            super(itemView);
            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvSongArtist = (TextView) itemView.findViewById(R.id.tvArtistName);
            btnAction = (Button) itemView.findViewById(R.id.btnPlay);
            mp3Image =  itemView.findViewById(R.id.mp3Image_id);
        }
    }
}
