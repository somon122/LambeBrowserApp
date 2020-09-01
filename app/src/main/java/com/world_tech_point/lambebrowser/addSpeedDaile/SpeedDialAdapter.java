package com.world_tech_point.lambebrowser.addSpeedDaile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.WebViewActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpeedDialAdapter extends RecyclerView.Adapter<SpeedDialAdapter.ViewHolder> {

    private Context context;
    private List<SpeedDialClass>speedDialClassList;
    private SpeedDialClass speedDialClass;

    public SpeedDialAdapter(Context context, List<SpeedDialClass> speedDialClassList) {
        this.context = context;
        this.speedDialClassList = speedDialClassList;
    }

    @NonNull
    @Override
    public SpeedDialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_speed_dial,parent,false);

        return new SpeedDialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeedDialAdapter.ViewHolder holder, final int position) {

        speedDialClass = speedDialClassList.get(position);
        holder.name.setText(speedDialClass.getName());
        Picasso.get().load(context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+speedDialClass.getImageURL()).fit().into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedDialClass = speedDialClassList.get(position);
                String url = speedDialClass.getSiteURL();

                if (url.contains("www")){

                    String lastUrl = "https://"+url;
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url",lastUrl);
                    context.startActivity(intent);

                }else if (url.contains("https")){

                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url",url);
                    context.startActivity(intent);

                }else {
                    String lastUrl = "https://www.google.com/search?q="+url;
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url",lastUrl);
                    context.startActivity(intent);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return speedDialClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.speedDialImage);
            name = itemView.findViewById(R.id.speedDialName);

        }
    }
}
