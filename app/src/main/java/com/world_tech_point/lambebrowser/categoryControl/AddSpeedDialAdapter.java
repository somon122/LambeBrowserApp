package com.world_tech_point.lambebrowser.categoryControl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialClass;
import com.world_tech_point.lambebrowser.addSpeedDaile.Speed_DB;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddSpeedDialAdapter extends RecyclerView.Adapter<AddSpeedDialAdapter.ViewHolder> {

    private Context context;
    private List<SpeedDialClass> speedDialClassList;
    private SpeedDialClass speedDialClass;
    private Speed_DB speed_db;

    public AddSpeedDialAdapter(Context context, List<SpeedDialClass> speedDialClassList) {
        this.context = context;
        this.speedDialClassList = speedDialClassList;
    }

    @NonNull
    @Override
    public AddSpeedDialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_add_speed_dial, parent, false);
        speed_db = new Speed_DB(context);
        return new AddSpeedDialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSpeedDialAdapter.ViewHolder holder, final int position) {

        speedDialClass = speedDialClassList.get(position);
        holder.name.setText(speedDialClass.getName());
        Picasso.get().load(context.getString(R.string.BASS_URL_FOR_IMAGE) + "public/files/image_file/" + speedDialClass.getImageURL()).fit().into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                speedDialClass = speedDialClassList.get(position);
                confirmAlert(speedDialClass.getSiteURL(), speedDialClass.getName(), speedDialClass.getImageURL());

            }
        });


    }

    private void confirmAlert(final String site_url, final String title, final String image) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Alert!")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SpeedDialClass speedDialClass = new SpeedDialClass(title, image, site_url);
                        Boolean isInsert = speed_db.Save_All_Data(speedDialClass);

                        if (isInsert) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MainActivity.class));
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
