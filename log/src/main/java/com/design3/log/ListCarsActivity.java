package com.design3.log;

import java.util.ArrayList;

import com.design3.log.adapter.CarArrayAdapter;
import com.design3.log.model.Car;
import com.design3.log.sql.CarDataSource;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ListCarsActivity extends ListActivity {
	
	private CarArrayAdapter carsAdapter; // dynamic array for saved Car objects
	private CarDataSource carsDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		carsDB = new CarDataSource(this);
		carsDB.open();
		
		// Check if intent is source from AddCarActivity by checking EXTRA.
        // If so, create a new Car object, and add it to the cars ArrayList
        if(getIntent().hasExtra(AddCarActivity.EXTRA_ADD_CAR_ACTIVITY)) {
        	Intent intent = getIntent();
        	String make  = intent.getStringExtra(AddCarActivity.EXTRA_MAKE);
        	String model = intent.getStringExtra(AddCarActivity.EXTRA_MODEL);
        	String year  = intent.getStringExtra(AddCarActivity.EXTRA_YEAR);
        	long id = 0; // temporary id
        	Car car = new Car(id, make, model, year);
        	carsDB.createCar(car);
        }
        
        fillView();
        
	    ListView view = getListView();
	    view.setVerticalScrollBarEnabled(true);
	    
	    // When an item is long pressed a delete dialog props up
	    view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {				
				removeCar(position);
				return true;
			}
        	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_cars, menu);
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
	
	public void addCar(View view) {
		Intent intent = new Intent(this, AddCarActivity.class);
		startActivity(intent);
	}
	
	// Function to remove a car entry from the array adapter
	protected void removeCar(final int position) {
		AlertDialog.Builder alert = new AlertDialog.Builder(ListCarsActivity.this);
		alert.setTitle("Delete");
		alert.setMessage("Do you want to delete this item?");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ArrayAdapter<Car> adapter = ((ArrayAdapter<Car>) getListAdapter());
				carsDB.deleteCar(adapter.getItem(position));
				fillView();
			}
		});
		alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
		alert.show();
	}
    
	// method to fill list view with cars
    protected void fillView() {    	
    	ArrayList<Car> carsArray = (ArrayList<Car>) carsDB.getAllCars(); 
 	    
 		if(!carsArray.isEmpty()) {
 	        carsAdapter = new CarArrayAdapter(this, carsArray);
 	        setListAdapter(carsAdapter); // set view as list of cars
 	    }
 	    else setContentView(R.layout.activity_list_cars); // set view as empty
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Car car = (Car)getListAdapter().getItem(position);
    	Intent intent = new Intent(this, ViewCarActivity.class);
    	intent.putExtra(Car.EXTRA_CAR, car);
    	startActivity(intent);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	carsDB.close();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	carsDB.close();
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    }
}
