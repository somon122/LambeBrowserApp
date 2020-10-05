package com.world_tech_point.lambebrowser.serviceFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.world_tech_point.lambebrowser.SuggestionClass;
import com.world_tech_point.lambebrowser.categoryControl.AddCategoryActivity;
import com.world_tech_point.lambebrowser.Database.DB_Manager;
import com.world_tech_point.lambebrowser.Database.LinkClass;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.WebViewActivity;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialAdapter;
import com.world_tech_point.lambebrowser.addSpeedDaile.SpeedDialClass;
import com.world_tech_point.lambebrowser.addSpeedDaile.Speed_DB;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;
import com.world_tech_point.lambebrowser.categoryControl.PageViewerAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    LinearLayout google,facebook,youTube,g_mail,searchUrl, addSpeedDial, visitedLinearLayout;
    AutoCompleteTextView autoCompleteTextView;

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
    List<String>sList;

    PageViewerAdapter pageViewerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;


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
        autoCompleteTextView = root.findViewById(R.id.urlEditText_id);

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



        SuggestionClass suggestionClass = new SuggestionClass(getContext());
        sList = suggestionClass.suggestionList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,sList);
        autoCompleteTextView.setAdapter(arrayAdapter);


     /*   pupUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.category_popup_model, (LinearLayout) root.findViewById(R.id.categoryPopUp_id));


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });*/


        viewPager = root.findViewById(R.id.homeViewPager_id);
        tabLayout = root.findViewById(R.id.homeTabLayout_id);

        tabLayout.addTab(tabLayout.newTab().setText("Politics"));
        tabLayout.addTab(tabLayout.newTab().setText("Funny"));
        tabLayout.addTab(tabLayout.newTab().setText("Movie"));
        tabLayout.addTab(tabLayout.newTab().setText("Fashion"));

        tabLayout.addTab(tabLayout.newTab().setText("LifeStyle"));
        tabLayout.addTab(tabLayout.newTab().setText("Sports"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Health"));

        tabLayout.addTab(tabLayout.newTab().setText("Crime"));
        tabLayout.addTab(tabLayout.newTab().setText("Automobile"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pageViewerAdapter = new PageViewerAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageViewerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        addSpeedDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryController.delete();
                categoryController.setStoreStatus("Add Speed Dial");
                startActivity(new Intent(getContext(), AddCategoryActivity.class));
                getActivity().finish();


            }
        });
        searchUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = autoCompleteTextView.getText().toString().trim();

                if (url.isEmpty()){
                    autoCompleteTextView.setError("Enter valid address");

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


