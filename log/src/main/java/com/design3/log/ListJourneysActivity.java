package com.design3.log;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.design3.log.adapter.JourneyArrayAdapter;
import com.design3.log.adapter.JourneysPagerAdapter;
import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneySQLHelper;

import java.util.ArrayList;


public class ListJourneysActivity extends FragmentActivity {

    public static final CharSequence TAB_PERSONAL = "PERSONAL";
    public static final CharSequence TAB_BUSINESS = "BUSINESS";
    public static final CharSequence TAB_ALL      = "ALL";
    public static final String EXTRA_SECTION_NUMBER = "section_number";

    private JourneysPagerAdapter journeysPagerAdapter;
    private ViewPager viewPager;
    private Car car;
    private JourneyDataSource journeysDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_journeys);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        car = getIntent().getParcelableExtra(Car.EXTRA_CAR);
        journeysDB = new JourneyDataSource(this);
        journeysDB.open();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        journeysPagerAdapter = new JourneysPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(journeysPagerAdapter);

        /*
        Ensure that the tabs change foreground when selecting them
         */
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        /*
        Get actionbar, set up as tabbed, and implement a tablistener
         */
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        actionBar.addTab(actionBar.newTab().setText(TAB_PERSONAL).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(TAB_BUSINESS).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(TAB_ALL).setTabListener(tabListener));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_journeys, menu);
        return true;
    }

    /*
    *  Ensure that the back button on the top menu goes back to parent activity
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected JourneyDataSource getJourneysDB() {
        return journeysDB;
    }

    protected Car getCar() {
        return car;
    }




}
