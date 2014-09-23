package com.design3.log;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class AddCarActivity extends Activity {

	public final static String EXTRA_ADD_CAR_ACTIVITY 
			= "com.design3.log.EXTRA_ADD_CAR_ACTIVITY";
	public final static String EXTRA_MAKE  = "com.design3.log.EXTRA_MAKE";
	public final static String EXTRA_MODEL = "com.design3.log.EXTRA_MODEL";
	public final static String EXTRA_YEAR  = "com.design3.log.EXTRA_YEAR";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_car);
		// Show the back button in the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	public void createCar(View view) {
		Intent intent = new Intent(this, ListCarsActivity.class);
		EditText make  = (EditText) findViewById(R.id.edit_make);
		EditText model = (EditText) findViewById(R.id.edit_model);
		EditText year  = (EditText) findViewById(R.id.edit_year);
		intent.putExtra(EXTRA_MAKE,  make.getText().toString());
		intent.putExtra(EXTRA_MODEL, model.getText().toString());
		intent.putExtra(EXTRA_YEAR,  year.getText().toString());
		// The following is an extra which determines that this intent is
		// sourced from this activity, AddCarActivity
		intent.putExtra(EXTRA_ADD_CAR_ACTIVITY, "Add Car Activity");
		startActivity(intent);
	}
}
