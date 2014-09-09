package com.design3.log;

import com.design3.log.model.Car;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ViewCarActivity extends Activity {
	
	private Car car;
	private TextView yearText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
        	car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString());
        }
        setContentView(R.layout.activity_view_car);
        yearText = (TextView)findViewById(R.id.text_year);
        yearText.setText(car.getYear());
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
			NavUtils.navigateUpFromSameTask(this);
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
}
