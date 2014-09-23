package com.design3.log;

import java.util.ArrayList;
import java.util.Set;
import com.design3.log.adapter.BTArrayAdapter;
import android.os.Bundle;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ConnectBluetoothActivity extends ListActivity {
	
	public static final String EXTRA_BT_ADDRESS = "com.design3.log.EXTRA_BT_ADDRESS";
	
	private BluetoothAdapter bta;
	private BTArrayAdapter btAdapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		bta = BluetoothAdapter.getDefaultAdapter();
		showBluetoothDevices();
		
		ListView view = getListView();
	    view.setVerticalScrollBarEnabled(true);
	    
	    // Show the Up button in the action bar.
	 	setupActionBar();
	}
	
	/* This method lists all paried devices */
	protected void showBluetoothDevices() {
		
		// Find any paired devices (returns Set)
		Set<BluetoothDevice> pairedDevices = bta.getBondedDevices();
		// Convert Set to ArrayList<BluetoothDevice>
		ArrayList<BluetoothDevice> btList = new ArrayList<BluetoothDevice>();
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices) {
				btList.add(device); // copy from set to arrayadapter
			}
			btAdapter = new BTArrayAdapter(this, btList);
			setListAdapter(btAdapter);
		}
		else setContentView(R.layout.activity_connect_bluetooth);
	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_bluetooth, menu);
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
	
	// On click try to connect to device
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	String address = ((BluetoothDevice) getListAdapter().getItem(position)).getAddress();
    	
    	// Pass back to MainActivity with address
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.putExtra(EXTRA_BT_ADDRESS, address);
	    startActivity(intent);	    
    }
}
