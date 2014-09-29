package com.design3.log;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.design3.log.adapter.JourneysPagerAdapter;
import com.design3.log.model.Car;
import com.design3.log.sql.CarDataSource;
import com.design3.log.sql.JourneyDataSource;

public class ListJourneysActivity extends FragmentActivity {

    public static final CharSequence TAB_PERSONAL = "PERSONAL";
    public static final CharSequence TAB_BUSINESS = "BUSINESS";
    public static final CharSequence TAB_ALL      = "ALL";
    public static final String EXTRA_SECTION_NUMBER = "section_number";

    private JourneysPagerAdapter journeysPagerAdapter;
    private ViewPager viewPager;
    private Car car;
    private JourneyDataSource journeysDB;
    private CarDataSource carsDB;
    private long carID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_journeys);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        car = getIntent().getParcelableExtra(Car.EXTRA_CAR);
        carID = car.getCarID();
        journeysDB = new JourneyDataSource(this);
        journeysDB.open();
        carsDB = new CarDataSource(this);
        carsDB.open();

        setTitle(car.toString());

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

    public void addJourney(View view) {
        Intent intent = new Intent(this, AddJourneyActivity.class);
        intent.putExtra(Car.EXTRA_CAR, car);
        startActivity(intent);
    }

    protected JourneyDataSource getJourneysDB() {
        return journeysDB;
    }
    protected CarDataSource getCarsDB() { return carsDB; }
    protected Car getCar() {
        return car;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(Car.EXTRA_CAR_ID, carID);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        carsDB.close();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        carsDB.open();
        super.onRestart();
    }
}
