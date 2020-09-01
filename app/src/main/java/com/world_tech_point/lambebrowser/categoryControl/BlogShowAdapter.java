package com.world_tech_point.lambebrowser.categoryControl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.ReadBlogActivity;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.YoutubeVideoPlayerActivity;

import java.util.List;

public class BlogShowAdapter extends RecyclerView.Adapter<BlogShowAdapter.ViewHolder> {

    private Context context;
    private List<BlogShowClass>showClassList;
    private BlogShowClass blogShowClass;


    public BlogShowAdapter(Context context, List<BlogShowClass> showClassList) {
        this.context = context;
        this.showClassList = showClassList;
    }

    @NonNull
    @Override
    public BlogShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.show_blog,parent,false);

       return new BlogShowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogShowAdapter.ViewHolder holder, final int position) {
        blogShowClass = showClassList.get(position);

        holder.title.setText(blogShowClass.getTitle());
        holder.Desc.setText(blogShowClass.getSite_url());
        Picasso.get().load(context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+ blogShowClass.getImage()).fit().into(holder.imageView);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "likeButton", Toast.LENGTH_SHORT).show();


            }
        });

        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (blogShowClass.getCategory().equals("Movie") || blogShowClass.getCategory().equals("Video")){

                    blogShowClass = showClassList.get(position);
                    String url = blogShowClass.getSite_url();
                    if (url.contains(".mp4") || url.contains(".3gp")){

                        Intent intent = new Intent(context, VideoPlayActivity.class);
                        intent.putExtra("video_url",url);
                        context.startActivity(intent);

                    }else {
                        Intent intent = new Intent(context, YoutubeVideoPlayerActivity.class);
                        intent.putExtra("id",url);
                        context.startActivity(intent);
                    }
                }else {
                    blogShowClass = showClassList.get(position);
                    String img = context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+ blogShowClass.getImage();
                    Intent intent = new Intent(context, ReadBlogActivity.class);
                    intent.putExtra("title",blogShowClass.getTitle());
                    intent.putExtra("image",img);
                    intent.putExtra("category",blogShowClass.getCategory());
                    intent.putExtra("description",blogShowClass.getSite_url());
                    context.startActivity(intent);
                }


            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             share();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (blogShowClass.getCategory().equals("Movie") || blogShowClass.getCategory().equals("Video")){

                    blogShowClass = showClassList.get(position);
                    String url = blogShowClass.getSite_url();
                    if (url.contains(".mp4") || url.contains(".3gp")){

                        Intent intent = new Intent(context, VideoPlayActivity.class);
                        intent.putExtra("video_url",url);
                        context.startActivity(intent);

                    }else {
                        Intent intent = new Intent(context, YoutubeVideoPlayerActivity.class);
                        intent.putExtra("id",url);
                        context.startActivity(intent);
                    }
                }else {
                    blogShowClass = showClassList.get(position);
                    String img = context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+ blogShowClass.getImage();
                    Intent intent = new Intent(context, ReadBlogActivity.class);
                    intent.putExtra("title",blogShowClass.getTitle());
                    intent.putExtra("image",img);
                    intent.putExtra("category",blogShowClass.getCategory());
                    intent.putExtra("description",blogShowClass.getSite_url());
                    context.startActivity(intent);
                }



            }
        });

    }

    private void share() {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareSub = "";
            String shareBody = "";
            intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(intent, "Lambe browser"));



    }

    @Override
    public int getItemCount() {
        return showClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, Desc,likeCount;
        LinearLayout likeButton, seeMore,share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryShowImage);
            title = itemView.findViewById(R.id.categoryShowTitle);
            Desc = itemView.findViewById(R.id.categoryShowDesc);
            likeCount = itemView.findViewById(R.id.blogLikeCounter);

            likeButton = itemView.findViewById(R.id.blogLike_id);
            seeMore = itemView.findViewById(R.id.blogSeeMore_id);
            share = itemView.findViewById(R.id.blogShare_id);

        }
    }
}
