package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.model.JourneyLog;
import com.design3.log.service.BluetoothService;
import com.design3.log.sql.CarDataSource;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneyLogDataSource;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddJourneyActivity extends Activity {

	private Car car;
    private JourneyDataSource journeysDB;
    private CarDataSource carsDB;
    private JourneyLogDataSource journeyLogDB;
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
        journeyLogDB = new JourneyLogDataSource(this);
        journeyLogDB.open();

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
        intent.putExtra(Car.EXTRA_CAR, car);
        startActivity(intent);
    }

    public void addDebugJourney(View view) {
        String start = "07/10/2014 1:30:00";
        Journey journey = new Journey(0, car.getCarID(), Journey.UseType.PERSONAL, start, 101456);
        journey.setStopTime("07/10/2014 1:34:00");
        journey = journeysDB.createJourney(journey);

        car.addJourney(Journey.UseType.PERSONAL);
        carsDB.updateCar(car);

        BufferedReader reader;
        String line, timePart;
        String[] parts;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("debugjourney.txt")));
            for(int i = 0; i < 480; i++) {
                line = reader.readLine();
                parts = line.split(" ");
                timePart = parts[0]+" "+parts[1];
                journeyLogDB.addJourneyData(new JourneyLog(journey.getJourneyID(), journey.getCarID(),
                    timePart, i * 2, Double.valueOf(parts[3]), Double.valueOf(parts[2])));
            }
        } catch (IOException e) {}

        journey = new Journey(0, car.getCarID(), Journey.UseType.BUSINESS, start, 123456);
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
