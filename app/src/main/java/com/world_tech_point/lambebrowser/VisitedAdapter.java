package com.world_tech_point.lambebrowser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.world_tech_point.lambebrowser.Database.LinkClass;

import java.util.List;

class VisitedAdapter extends RecyclerView.Adapter<VisitedAdapter.ViewHolder>{

   private Context context;
   private List<LinkClass> linkClassList;
   private LinkClass linkClass;

    public VisitedAdapter(Context context, List<LinkClass> linkClassList) {
        this.context = context;
        this.linkClassList = linkClassList;
    }

    @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.visited_model_view,parent,false);

       return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        linkClass = linkClassList.get(position);

        holder.titleTV.setText(linkClass.getTitle());
        holder.urlTV.setText(linkClass.getLink());
       Glide.with(context).load(linkClass.getLogo()).placeholder(R.drawable.world).into(holder.vLogo);


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               linkClass = linkClassList.get(position);
               String lastUrl = linkClass.getLink();
               Intent intent = new Intent(context, WebViewActivity.class);
               intent.putExtra("url",lastUrl);
               context.startActivity(intent);

           }
       });

   }

   @Override
   public int getItemCount() {
       return linkClassList.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder {

           ImageView vLogo;
           TextView titleTV, urlTV;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           vLogo = itemView.findViewById(R.id.visitedLogo_id);
           titleTV = itemView.findViewById(R.id.visitedTitle_id);
           urlTV = itemView.findViewById(R.id.visitedLink_id);

       }
   }
}
