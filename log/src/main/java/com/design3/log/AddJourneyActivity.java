package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AddJourneyActivity extends Activity {

	private Car car;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
        	car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString() + ": New Journey");
        }
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}	
}
