package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.sql.CarDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ViewCarActivity extends Activity {
	
	private long carID;
    private Car car;
    private CarDataSource carsDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        carsDB = new CarDataSource(this);
        carsDB.open();

        setContentView(R.layout.activity_view_car);

        // Check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
        	car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString());
            carID = car.getCarID();
            fillView();
        }

        else if(savedInstanceState != null) {
            carID = savedInstanceState.getLong(Car.EXTRA_CAR_ID);
            car = carsDB.getCar(carID);
            fillView();
        }

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void listJourneys(View view) {
		Intent intent = new Intent(this, ListJourneysActivity.class);
		intent.putExtra(Car.EXTRA_CAR, car);
		startActivity(intent);
	}
	
	public void beginJourney(View view) {
		Intent intent = new Intent(this, AddJourneyActivity.class);
		intent.putExtra(Car.EXTRA_CAR, car);
		startActivity(intent);
	}

    protected void fillView() {

        TextView carIdText, yearText, personalJourneyText, businessJourneyText, totalJourneyText,
                avgEconomyText, odometerText;

        carIdText = (TextView)findViewById(R.id.text_carid);
        yearText = (TextView)findViewById(R.id.text_year);
        personalJourneyText = (TextView)findViewById((R.id.text_personal_journeys));
        businessJourneyText = (TextView)findViewById(R.id.text_business_journeys);
        totalJourneyText = (TextView)findViewById(R.id.text_total_journeys);
        avgEconomyText = (TextView)findViewById(R.id.text_avg_economy);
        odometerText = (TextView)findViewById(R.id.text_odometer);

        yearText.setText(car.getYear());
        carIdText.setText(String.format("# %03d",(int)car.getCarID()));
        personalJourneyText.setText(String.valueOf(car.getPersonalJourneyCount()));
        businessJourneyText.setText(String.valueOf(car.getBusinessJourneyCount()));
        totalJourneyText.setText(String.valueOf(car.getTotalJourneyCount()));
        avgEconomyText.setText(String.format("%2.1f  L/100km", car.getFuelAverageEconomy()));
        odometerText.setText(String.format("%d  km", (int)car.getOdometer()));
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
        car = carsDB.getCar(carID);
        fillView();
        super.onRestart();
    }
}
