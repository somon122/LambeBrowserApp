package com.world_tech_point.lambebrowser.addSpeedDaile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.WebViewActivity;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class SpeedDialAdapter extends RecyclerView.Adapter<SpeedDialAdapter.ViewHolder> {

    private Context context;
    private List<SpeedDialClass>speedDialClassList;
    private SpeedDialClass speedDialClass;
    private Speed_DB speed_db;
    private CategoryController categoryController;
    private int mPosition;
    private InterstitialAd interstitialAd;
    private MembershipSave membershipSave;

    public SpeedDialAdapter(Context context, List<SpeedDialClass> speedDialClassList) {
        this.context = context;
        this.speedDialClassList = speedDialClassList;
    }

    @NonNull
    @Override
    public SpeedDialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_speed_dial,parent,false);

        speed_db = new Speed_DB(context);
        categoryController = new CategoryController(context);
        membershipSave= new MembershipSave(context);
        interstitialAd = new InterstitialAd(context, context.getString(R.string.facebookInterstitialAdId));

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                if (membershipSave.getUser_type().equals("Affiliate partnership")){

                    if (membershipSave.getAdd_fee_status().equals("User_paid")){
                        taskPointAdd(membershipSave.getReferCode(),"5");
                    }else {
                        taskPointAdd(membershipSave.getReferCode(),"1");
                    }
                }else if (membershipSave.getUser_type().equals("Free membership")){
                    taskPointAdd(membershipSave.getReferCode(),"1");
                }else {
                    sentForSearch(mPosition);
                }


            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback

            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
        return new SpeedDialAdapter.ViewHolder(view);
    }

    private void sentForSearch(int mPosition) {

        speedDialClass = speedDialClassList.get(mPosition);
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

    @Override
    public void onBindViewHolder(@NonNull SpeedDialAdapter.ViewHolder holder, final int position) {

        speedDialClass = speedDialClassList.get(position);
        holder.name.setText(speedDialClass.getName());
        Picasso.get().load(context.getString(R.string.BASS_URL_FOR_IMAGE)+"public/files/image_file/"+speedDialClass.getImageURL()).fit().into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition = position;
                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }else {
                   sentForSearch(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                speedDialClass = speedDialClassList.get(position);
               alreadyAlert(speedDialClass.getId());
                return false;
            }
        });
    }

    private void alreadyAlert(final int id) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Alert!")
                .setMessage("Are you sure to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        speed_db.Delete_Speed_Data(id);
                        categoryController.delete();
                        context.startActivity(new Intent(context, MainActivity.class));

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

    private void taskPointAdd(final String refer_code, final String new_point) {
        String url = context.getString(R.string.BASS_URL)+ "add_tasks_point";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("response").equals("point_added")) {
                        Toasty.success(context,"Success",Toasty.LENGTH_LONG).show();
                        sentForSearch(mPosition);

                    }else if (obj.getString("response").equals("point_not_added")){
                        Toasty.error(context,"Please Try again",Toasty.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Problem", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "url problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("referCode", refer_code);
                Params.put("new_point", new_point);
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
