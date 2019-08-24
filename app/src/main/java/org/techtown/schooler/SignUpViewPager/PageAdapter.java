package org.techtown.schooler.SignUpViewPager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.techtown.schooler.SignUpViewPager.Fragment.CallFragment;


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
                return new CallFragment();
//            case 1:
//                return new schoolMealsFragment();
//            case 2:
//                return new weatherFragment();
//            case 3:
//                return new timeTableFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}