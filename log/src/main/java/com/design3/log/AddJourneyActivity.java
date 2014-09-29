package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.service.BluetoothService;
import com.design3.log.sql.CarDataSource;
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
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddJourneyActivity extends Activity {

	private Car car;
    private JourneyDataSource journeysDB;
    private CarDataSource carsDB;
    private Journey.UseType useType;

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
        carsDB = new CarDataSource(this);
        carsDB.open();

        useType = Journey.UseType.BUSINESS;
	}

    public void beginJourney(View view) {
        Date now = new Date();
        String start = DateFormat.getDateTimeInstance().format(now);
        long journeyID = 0; // temporary id
        long odometer = 123456; // temporal odometer
        Journey journey = new Journey(journeyID, car.getCarID(), useType, start, odometer);
        Intent intent = new Intent(this, MonitorJourneyActivity.class);
        intent.putExtra(Journey.EXTRA_JOURNEY, journey);
        startActivity(intent);
    }

    public void addDebugJourney(View view) {
        Date now = new Date();
        String start = DateFormat.getDateTimeInstance().format(now);
        long journeyID = 0; // temporary id
        Journey journey = new Journey(journeyID, car.getCarID(), Journey.UseType.PERSONAL, start, 123456);
        journey.setStopTime("23/09/2014 11:59:34 PM");
        car.addJourney(Journey.UseType.PERSONAL);
        carsDB.updateCar(car);
        journeysDB.createJourney(journey);
        journey = new Journey(journeyID, car.getCarID(), Journey.UseType.BUSINESS, start, 123456);
        journey.setStopTime("24/09/2014 01:34:34 AM");
        car.addJourney(Journey.UseType.BUSINESS);
        carsDB.updateCar(car);
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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_business :
                if(checked) useType = Journey.UseType.BUSINESS;
                break;
            case R.id.radio_personal :
                if(checked) useType = Journey.UseType.PERSONAL;
                break;
        }
    }


}
