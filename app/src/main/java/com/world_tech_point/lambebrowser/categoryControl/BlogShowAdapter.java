package com.world_tech_point.lambebrowser.categoryControl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.ReadBlogActivity;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.ViewCountClass;
import com.world_tech_point.lambebrowser.YoutubeVideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogShowAdapter extends RecyclerView.Adapter<BlogShowAdapter.ViewHolder> {

    private Context context;
    private List<BlogShowClass>showClassList;
    private BlogShowClass blogShowClass;
    private ViewCountClass viewCountClass;
    private  String viewSize = "0";


    public BlogShowAdapter(Context context, List<BlogShowClass> showClassList) {
        this.context = context;
        this.showClassList = showClassList;
    }

    @NonNull
    @Override
    public BlogShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.show_blog,parent,false);

       viewCountClass= new ViewCountClass(context);
       return new BlogShowAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BlogShowAdapter.ViewHolder holder, final int position) {
        blogShowClass = showClassList.get(position);

        holder.title.setText(blogShowClass.getTitle());
        holder.Desc.setText(blogShowClass.getSite_url());
        Picasso.get().load(context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+ blogShowClass.getImage()).fit().into(holder.imageView);

        getBlogView(holder, String.valueOf(position),blogShowClass.getCategory(),blogShowClass.getTitle());


        if (blogShowClass.getCategory().equals("Movie") || blogShowClass.getCategory().equals("Video")){
            holder.blogSeeText.setText("Watch now");
            holder.blogSeeText.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogShowClass = showClassList.get(position);
                String vc = holder.likeCount.getText().toString();
                int v = Integer.parseInt(vc)+1;
                viewCountClass.insertBlogView(String.valueOf(v),String.valueOf(position),blogShowClass.getCategory(),blogShowClass.getTitle());

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
                blogShowClass = showClassList.get(position);
             share(blogShowClass.getTitle(),blogShowClass.getSite_url(),blogShowClass.getImage());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                blogShowClass = showClassList.get(position);
                String vc = holder.likeCount.getText().toString();
                int v = Integer.parseInt(vc)+1;
                viewCountClass.insertBlogView(String.valueOf(v),String.valueOf(position),blogShowClass.getCategory(),blogShowClass.getTitle());

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

    private void share(String title, String desc, String img) {
        String imageUrl = context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+img;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareSub = title;
            String shareBody = desc+"\n"+imageUrl;
            intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(intent, "Lambe browser"));

    }


    public void getBlogView(final BlogShowAdapter.ViewHolder holder, final String position, final String category, final String title) {
        String url = context.getString(R.string.BASS_URL) + "getBlogView";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                ViewClass viewClass = new ViewClass();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            String viewCounterV = dataobj.getString("view");
                           holder.likeCount.setText(viewCounterV);
                        }
                    } else {
                        //noDataAlert();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("position", position);
                Params.put("category", category);
                Params.put("title", title);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return showClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, Desc,likeCount, blogSeeText;
        LinearLayout likeButton, seeMore,share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryShowImage);
            title = itemView.findViewById(R.id.categoryShowTitle);
            Desc = itemView.findViewById(R.id.categoryShowDesc);
            likeCount = itemView.findViewById(R.id.blogLikeCounter);
            blogSeeText = itemView.findViewById(R.id.blogSeeText_id);

            likeButton = itemView.findViewById(R.id.blogLike_id);
            seeMore = itemView.findViewById(R.id.blogSeeMore_id);
            share = itemView.findViewById(R.id.blogShare_id);

        }
    }
}
