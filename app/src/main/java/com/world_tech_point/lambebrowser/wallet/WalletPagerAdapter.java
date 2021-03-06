package com.world_tech_point.lambebrowser.wallet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WalletPagerAdapter extends FragmentPagerAdapter {

    private int position;

    public WalletPagerAdapter(@NonNull FragmentManager fm, int position) {
        super(fm, position);
        this.position = position;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fm = null;

        switch (position){
            case 0:
                fm = new WithdrawFragment();
                break;
            case 1:
                fm = new HistoryFragment();
                break;
        }

        return fm;
    }

    @Override
    public int getCount() {
        return position;
    }
}
