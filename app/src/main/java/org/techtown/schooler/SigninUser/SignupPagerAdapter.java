package org.techtown.schooler.SigninUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.techtown.schooler.Fragment.EmailFragment;
import org.techtown.schooler.Fragment.EndFragment;
import org.techtown.schooler.Fragment.IdFragment;
import org.techtown.schooler.Fragment.PwFragment;
import org.techtown.schooler.Fragment.SearchSchoolFragment;
import org.techtown.schooler.Fragment.StartFragment;
import org.techtown.schooler.Fragment.UserFragment;

public class SignupPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public SignupPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:

                StartFragment startFragment = new StartFragment();
                return startFragment;

            case 1:

                IdFragment idFragment = new IdFragment();
                return idFragment;

            case 2:
                PwFragment pwFragment = new PwFragment();
                return pwFragment;

            case 3:
                EmailFragment emailFragment = new EmailFragment();
                return emailFragment;

            case 4:
                SearchSchoolFragment searchSchoolFragment = new SearchSchoolFragment();
                return searchSchoolFragment;

            case 5:
                UserFragment userFragment = new UserFragment();
                return userFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }


}
