package com.example.plank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();
    private FragmentManager mFM;

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    public VPAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mFM = fm;
        items = new ArrayList<Fragment>();
        items.add(new TodayFragment());
        items.add(new HistoryFragment());

        itext.add("투데이");
        itext.add("히스토리");

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public FragmentManager getFM() {
        return mFM;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
