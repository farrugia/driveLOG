package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.service.BluetoothService;
import com.design3.log.sql.JourneyDataSource;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class AddJourneyActivity extends Activity {

	private Car car;
    private JourneyDataSource journeysDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);
        // check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
        	car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString() + ": New Journey");
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);

        journeysDB = new JourneyDataSource(this);
        journeysDB.open();
	}

    public void beginJourney(View view) {
        Intent intent = new Intent(this, MonitorJourneyActivity.class);
        startActivity(intent);
    }

    public void addJourney(View view) {
        SimpleDateFormat start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long id = 0; // temporary id
        Journey journey = new Journey(id, id, Journey.UseType.PERSONAL, start, 123456);
        journeysDB.createJourney(journey);
        journey = new Journey(id, id, Journey.UseType.BUSINESS, start, 123456);
        journeysDB.createJourney(journey);
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


}
