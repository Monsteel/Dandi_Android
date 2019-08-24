package org.techtown.schooler.SignUpViewPager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.linda.Fragment.calendarFragment;
import com.example.linda.Fragment.schoolMealsFragment;
import com.example.linda.Fragment.timeTableFragment;
import com.example.linda.Fragment.weatherFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;


    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new calendarFragment();
            case 1:
                return new schoolMealsFragment();
            case 2:
                return new weatherFragment();
            case 3:
                return new timeTableFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}