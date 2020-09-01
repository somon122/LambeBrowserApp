package com.world_tech_point.lambebrowser.categoryControl;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.world_tech_point.lambebrowser.categoryPage.AutomobileFragment;
import com.world_tech_point.lambebrowser.categoryPage.BusinessFragment;
import com.world_tech_point.lambebrowser.categoryPage.CrimeFragment;
import com.world_tech_point.lambebrowser.categoryPage.FashionFragment;
import com.world_tech_point.lambebrowser.categoryPage.FunnyFragment;
import com.world_tech_point.lambebrowser.categoryPage.HealthFragment;
import com.world_tech_point.lambebrowser.categoryPage.MovieFragment;
import com.world_tech_point.lambebrowser.categoryPage.LifeStyleFragment;
import com.world_tech_point.lambebrowser.categoryPage.PoliticsFragment;
import com.world_tech_point.lambebrowser.categoryPage.SportsFragment;
import com.world_tech_point.lambebrowser.categoryPage.TechnologyFragment;
import com.world_tech_point.lambebrowser.categoryPage.VideoFragment;


public class PageViewerAdapter extends FragmentPagerAdapter {

    private int position;

    public PageViewerAdapter(@NonNull FragmentManager fm, int position) {
        super(fm, position);
        this.position = position;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fm = null;

        switch (position){
            case 0:
                fm = new PoliticsFragment();
                break;
            case 1:
                fm = new FunnyFragment();
                break;
            case 2:
                fm = new MovieFragment();
                break;
            case 3:
                fm = new FashionFragment();
                break;
            case 4:
                fm = new LifeStyleFragment();
                break;

            case 5:
                fm = new SportsFragment();
                break;

            case 6:
                fm = new TechnologyFragment();
                break;

            case 7:
                fm = new BusinessFragment();
                break;

            case 8:
                fm = new HealthFragment();
                break;

            case 9:
                fm = new CrimeFragment();
                break;
            case 10:
                fm = new AutomobileFragment();
                break;
            case 11:
                fm = new VideoFragment();
                break;

        }

        return fm;
    }

    @Override
    public int getCount() {
        return position;
    }
}
