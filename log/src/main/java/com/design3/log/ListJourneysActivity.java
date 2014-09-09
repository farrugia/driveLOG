package com.design3.log;

import java.util.ArrayList;

import com.design3.log.adapter.JourneyArrayAdapter;
import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.JourneyDataSource;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListJourneysActivity extends ListActivity {
	
	private JourneyArrayAdapter journeysAdapter; // dynamic array for journeys
	private JourneyDataSource journeysDB;
	private Car car;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		journeysDB = new JourneyDataSource(this);
		journeysDB.open();
        
		car = (Car) getIntent().getParcelableExtra(Car.EXTRA_CAR);
        fillView(car.getCarID());
        
	    ListView view = getListView();
	    view.setVerticalScrollBarEnabled(true);
	    
	    // When an item is long pressed a delete dialog props up
	    view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {				
//				removeJourney(position); TODO
				return true;
			}
	    });
	}
	
	// method to fill list view with journeys
    protected void fillView(long carID) {    	
    	ArrayList<Journey> journeysArray = 
    			(ArrayList<Journey>) journeysDB.getJourneys(carID); 
 	    
 		if(!journeysArray.isEmpty()) {
 	        journeysAdapter = new JourneyArrayAdapter(this, journeysArray);
 	        setListAdapter(journeysAdapter); // set view as list of cars
 	    }
 	    else setContentView(R.layout.activity_list_journeys); // set view as empty
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
}
