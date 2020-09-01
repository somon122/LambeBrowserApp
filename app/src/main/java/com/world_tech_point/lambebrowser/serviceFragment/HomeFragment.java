package com.world_tech_point.lambebrowser.serviceFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.categoryControl.AddCategoryActivity;
import com.world_tech_point.lambebrowser.Database.DB_Manager;
import com.world_tech_point.lambebrowser.Database.LinkClass;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.WebViewActivity;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialAdapter;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialClass;
import com.world_tech_point.lambebrowser.addSpeedDaile.Speed_DB;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    LinearLayout google,facebook,youTube,g_mail,searchUrl, addSpeedDial, visitedLinearLayout;
    EditText urlEditText;

    RecyclerView recyclerView;
    List<LinkClass>visitedClassList;
    DB_Manager db_manager;
    ImageView deleteHistoryData;
    ConstraintLayout constraintLayoutScroll;
    TextView pupUpButton;
    FrameLayout categoryHost;
    int cHide;

    RecyclerView speedDialRecyclerView;
    List<SpeedDialClass>dialClassList;
    Speed_DB speed_db;

    CategoryController categoryController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root =  inflater.inflate(R.layout.fragment_home, container, false);


        categoryController = new CategoryController(getContext());
        google = root.findViewById(R.id.google_id);
        facebook = root.findViewById(R.id.facebook_id);
        youTube = root.findViewById(R.id.youTube_id);
        g_mail = root.findViewById(R.id.g_mail_id);
        searchUrl = root.findViewById(R.id.searchUrl_id);
        urlEditText = root.findViewById(R.id.urlEditText_id);
        deleteHistoryData = root.findViewById(R.id.deleteHistoryData_id);
        visitedLinearLayout = root.findViewById(R.id.visitedLinearLayout);

        speedDialRecyclerView = root.findViewById(R.id.speedDialRecyclerView);
        speedDialRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        speedDialRecyclerView.setHasFixedSize(true);
        dialClassList = new ArrayList<>();

        speed_db = new Speed_DB(getContext());
        dialClassList=speed_db.getLinkClassList();

        SpeedDialAdapter speedDialAdapter = new SpeedDialAdapter(getContext(),dialClassList);
        speedDialRecyclerView.setAdapter(speedDialAdapter);
        speedDialAdapter.notifyDataSetChanged();


        addSpeedDial = root.findViewById(R.id.addSpeedDial);
        //pupUpButton = root.findViewById(R.id.pupUpButton);


        recyclerView = root.findViewById(R.id.mostVisitedRecyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        visitedClassList = new ArrayList<>();
        db_manager = new DB_Manager(getContext());
        visitedClassList=db_manager.getLinkClassList();
        VisitedAdapter visitedAdapter = new VisitedAdapter(getContext(),visitedClassList);
        recyclerView.setAdapter(visitedAdapter);
        visitedAdapter.notifyDataSetChanged();


     /*   pupUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.category_popup_model, (LinearLayout) root.findViewById(R.id.categoryPopUp_id));


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });*/
        deleteHistoryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             deleteVisited();
            }
        });

        addSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryController.delete();
                categoryController.setStoreStatus("Add Speed Dial");
             startActivity(new Intent(getContext(), AddCategoryActivity.class));


            }
        });
        searchUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = urlEditText.getText().toString().trim();

                if (url.isEmpty()){
                    urlEditText.setError("Enter valid address");

                }else {
                    if (url.contains("www")){

                        String lastUrl = "https://"+url;
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url",lastUrl);
                        startActivity(intent);

                    }else if (url.contains("https")){

                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);

                    }else {
                        String lastUrl = "https://www.google.com/search?q="+url;
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url",lastUrl);
                        startActivity(intent);

                    }

                }


            }
        });

                 facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customSearch("https://www.facebook.com/");

            }
        });
                 youTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSearch("https://www.youtube.com/");

            }
        });
                 google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSearch("https://www.google.com/");

            }
        });
                 g_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customSearch("https://mail.google.com/");

            }
        });





        return root;
    }

    private void deleteVisited() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                boolean delete =  db_manager.removeAll();
                if (delete){
                    Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentSet(homeFragment);
                }else {
                    Toast.makeText(getContext(), "Already Clear", Toast.LENGTH_SHORT).show();
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

    private void fragmentSet(Fragment fragment){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hostFragment,fragment)
                .commit();

    }
    private void customSearch(String url){

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);


    }

}


 class VisitedAdapter extends RecyclerView.Adapter<VisitedAdapter.ViewHolder>{

    private Context context;
    private List<LinkClass>linkClassList;
    private LinkClass linkClass;

     public VisitedAdapter(Context context, List<LinkClass> linkClassList) {
         this.context = context;
         this.linkClassList = linkClassList;
     }

     @NonNull
    @Override
    public VisitedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.visited_model_view,parent,false);

        return new VisitedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitedAdapter.ViewHolder holder, final int position) {

         linkClass = linkClassList.get(position);

         holder.titleTV.setText(linkClass.getTitle());
         holder.urlTV.setText(linkClass.getLink());
        Picasso.get().load(linkClass.getLogo()).placeholder(R.drawable.world).into(holder.vLogo);

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