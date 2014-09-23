package com.design3.log.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.design3.log.ListJourneysActivity;
import com.design3.log.ListJourneysFragment;

/**
 * returns a fragment corresponding to one of the sections/tabs/pages.
 */
public class JourneysPagerAdapter extends FragmentPagerAdapter {

    public JourneysPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ListJourneysFragment fragment = new ListJourneysFragment();
        Bundle args = new Bundle();
        args.putInt(ListJourneysActivity.EXTRA_SECTION_NUMBER, position+1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ListJourneysActivity.TAB_PERSONAL;
            case 1:
                return ListJourneysActivity.TAB_BUSINESS;
            case 2:
                return ListJourneysActivity.TAB_ALL;
        }
        return null;
    }
}
