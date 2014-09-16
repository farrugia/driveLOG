package com.design3.log;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.design3.log.model.Car;
import com.design3.log.sql.JourneyDataSource;


public class ListJourneysActivity extends Activity {

    private JourneysPagerAdapter mJourneysPagerAdapter;
    private ViewPager mViewPager;
    private Car car;
    private JourneyDataSource journeysDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_journeys);

        journeysDB = new JourneyDataSource(this);
        journeysDB.open();

        car = getIntent().getParcelableExtra(Car.EXTRA_CAR);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mJourneysPagerAdapter = new JourneysPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mJourneysPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_journeys, menu);
        return true;
    }

    /**
     * returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class JourneysPagerAdapter extends FragmentPagerAdapter {

        public JourneysPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ListJourneysFragment.newInstance(position + 1, car, journeysDB);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section_personal).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_business).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section_all).toUpperCase(l);
            }
            return null;
        }
    }
}
