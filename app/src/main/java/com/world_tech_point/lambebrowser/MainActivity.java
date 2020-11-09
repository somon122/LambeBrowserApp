package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.world_tech_point.lambebrowser.categoryControl.AddCategoryActivity;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;
import com.world_tech_point.lambebrowser.serviceFragment.DownloadActivity;
import com.world_tech_point.lambebrowser.serviceFragment.DownloadFragment;
import com.world_tech_point.lambebrowser.serviceFragment.FilesFragment;
import com.world_tech_point.lambebrowser.serviceFragment.HomeFragment;
import com.world_tech_point.lambebrowser.serviceFragment.MeFragment;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int exitCount;
    ImageView pupUpButton;
    int cHide;
    CategoryController categoryController;
    MembershipSave membershipSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        fragmentSet(homeFragment);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pupUpButton = findViewById(R.id.pupUpButton);

        membershipSave = new MembershipSave(this);
        categoryController = new CategoryController(this);


        if (membershipSave.getAdd_fee_status().equals("User_pending")){
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,
                    R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.category_popup_model,
                    (LinearLayout)findViewById(R.id.pendingAlertPopUp_id));

            TextView userName = bottomSheetView.findViewById(R.id.payAlertUserName);
            TextView number = bottomSheetView.findViewById(R.id.payAlertNumber);
            TextView referCode = bottomSheetView.findViewById(R.id.payAlertReferCode);
            Button payNow = bottomSheetView.findViewById(R.id.payAlertPayNowBtn);
            Button payLater = bottomSheetView.findViewById(R.id.payAlertPayLaterBtn);

            userName.setText("Your Name: "+membershipSave.getUserName());
            number.setText("Your Number: "+membershipSave.getNumber());
            referCode.setText("Your Refer code: "+membershipSave.getReferCode());
            payNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paystack.com/pay/2l0ek9m6vu")));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paystack.com/pay/2l0ek9m6vu")));
                    }

                }
            });
            payLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

        }


        pupUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cHide>0){
                    cHide=0;
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentSet(homeFragment);
                }else {
                    cHide=cHide+1;
                    categoryController.delete();
                    categoryController.setStoreStatus("Blog");
                    startActivity(new Intent(MainActivity.this, AddCategoryActivity.class));

                }
            }
        });


        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.bottomHome_id:
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentSet(homeFragment);
                        break;
                    case R.id.bottomDownload_id:
                      startActivity(new Intent(getApplicationContext(), DownloadActivity.class));
                        break;
                    case R.id.bottomBlog_id:

                        categoryController.delete();
                        categoryController.setStoreStatus("Blog");
                        startActivity(new Intent(MainActivity.this, AddCategoryActivity.class));
                        break;
                    case R.id.bottom_Me_id:
                        MeFragment meFragment = new MeFragment();
                        fragmentSet(meFragment);
                        break;
                    case R.id.bottom_File_id:

                        ///startActivity(new Intent(getApplicationContext(),MainActivity.class));

                       FilesFragment filesFragment = new FilesFragment();
                        fragmentSet(filesFragment);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private  void fragmentSet(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hostFragment,fragment)
                .commit();

    }

    private void exitsAlert(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,
                R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.exits_model,
                (LinearLayout)findViewById(R.id.exitsAlertPopUp_id));
        TextView exits = bottomSheetView.findViewById(R.id.exitsModelBtn);
        TextView exitsCacheClear = bottomSheetView.findViewById(R.id.exitsModelCacheBtn);
        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finishAffinity();
            }
        });
        exitsCacheClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              deleteCache(MainActivity.this);
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
      exitsAlert();
    }

  /*  PackageManager pm = getPackageManager();
    // Get all methods on the PackageManager
    Method[] methods = pm.getClass().getDeclaredMethods();
            for(Method m : methods) {
        if (m.getName().equals("freeStorage")) {
            // Found the method I want to use
            try {
                long desiredFreeStorage = 8 * 1024 * 1024 * 1024; // Request for 8GB of free space
                m.invoke(pm, desiredFreeStorage , null);
            } catch (Exception e) {
                // Method invocation failed. Could be a permission problem
            }
            break;
        }
    }*/


    public  void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
           boolean cd =  deleteDir(dir);
           if (cd){
              finishAffinity();
           }

        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}