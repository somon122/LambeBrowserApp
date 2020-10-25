package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.world_tech_point.lambebrowser.categoryControl.AddCategoryActivity;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;
import com.world_tech_point.lambebrowser.serviceFragment.DownloadActivity;
import com.world_tech_point.lambebrowser.serviceFragment.DownloadFragment;
import com.world_tech_point.lambebrowser.serviceFragment.FilesFragment;
import com.world_tech_point.lambebrowser.serviceFragment.HomeFragment;
import com.world_tech_point.lambebrowser.serviceFragment.MeFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int exitCount;
    ImageView pupUpButton;
    int cHide;

    CategoryController categoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        fragmentSet(homeFragment);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        pupUpButton = findViewById(R.id.pupUpButton);

        categoryController = new CategoryController(this);

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

              /*  BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,
                        R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.category_popup_model,
                        (LinearLayout)findViewById(R.id.categoryPopUp_id));


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();*/


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

      /*  if (exitCount >1){
            //finishAffinity();
            deleteCache(MainActivity.this);

        }else {
            exitCount = exitCount+1;
            HomeFragment homeFragment = new HomeFragment();
            fragmentSet(homeFragment);
        }*/

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



    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
           boolean cd =  deleteDir(dir);
           if (cd){
               Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
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