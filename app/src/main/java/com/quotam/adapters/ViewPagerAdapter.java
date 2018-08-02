package com.quotam.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public String getItemTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
            return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void removeFragment(Fragment fragment, String title) {
        fragmentList.remove(fragment);
        fragmentTitleList.remove(title);
    }

    public void removeFragment(int position) {
        fragmentList.remove(position);
        fragmentTitleList.remove(position);
    }


}
