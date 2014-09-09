package com.design3.log;

import com.design3.log.service.BluetoothService;
import com.design3.log.service.BluetoothService.BTBinder;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private BluetoothDevice btDevice;
	private BluetoothAdapter btAdapter;
	
	private boolean toggleLED = false;
	
	private TextView statusText;
	private ProgressBar statusProgress;
	
	private String btAddress = null;
	private BluetoothService btService;
	private boolean isBound = false;
	
	public static final int REQUEST_ENABLE_BT = 1;
	public static final String EXTRA_IS_CONNECTED = "com.design3.log.EXTRA_IS_CONNECTED";
		
    // This function is called when the activity is created
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_main); 
	    
	    Log.d("MAIN_ACTIVITY","MainActivity created...");
	    
	    statusText = ((TextView) findViewById(R.id.text_status));
	    statusProgress = (ProgressBar) findViewById(R.id.progress_status);
	    
	    checkBluetooth();
	    
		/*
	     * Check if intent has come from ConnectBluetoothActivity
	     * If so, get bluetooth address and store in btAddress
	     */
	    if(getIntent().hasExtra(ConnectBluetoothActivity.EXTRA_BT_ADDRESS)) {	    	
	    	btAddress = getIntent().getExtras().
	    			getString(ConnectBluetoothActivity.EXTRA_BT_ADDRESS);
	    	}

        /*
	     * Start or rebind a BluetoothService and bind it to this activity via
	     * a ServiceConnection object: serviceConnection (implemented at bottom)
	     */
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
	
	/* 
	 * This method checks if the local BluetoothAdapter is enabled
	 * If not, it prompts the user for permission to enable it.
	 */
	protected void checkBluetooth() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!btAdapter.isEnabled()) {
			Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
		}
	}
	
	/*
	 * Method to update the MainActivity status bar
	 */	
	protected void updateStatus() {
		if(btService.getConnectionStatus()) {
            if(btAddress == null)
                btAddress = btService.getBtAddress();
			btDevice = btAdapter.getRemoteDevice(btAddress);
			Toast.makeText(MainActivity.this, "Connection successful! Connected to " 
				+ btDevice.getName() + " at address " + btDevice.getAddress(), 
			    Toast.LENGTH_LONG).show();
			statusText.setText("Connected to " + btDevice.getName() + " at address "
					+ btDevice.getAddress());
		}
		else {
			Toast.makeText(MainActivity.this, "Connection could not be made!",Toast.LENGTH_LONG).show();
			statusText.setText("     Not connected! To connect tap Connect Bluetooth");
		}
	}
	
	/*
	 * Testing function for use with an Arduino board with BT chip
	 */
	public void toggleLED(View view) {
		if(btService.getConnectionStatus() && isBound) {
			toggleLED = !toggleLED;
			btService.toggleLED(toggleLED);
		}
	}
	
	public void addCar(View view) {
		Intent intent = new Intent(this, AddCarActivity.class);
		startActivity(intent);
	}
	
	public void viewAbout(View view) {
		Intent intent = new Intent(this, ViewAboutActivity.class);
		startActivity(intent);
	}
	
	public void listCars(View view) {
		Intent intent = new Intent(this, ListCarsActivity.class);
		startActivity(intent);
	}
	
	public void connectBT(View view) {
		Intent intent = new Intent(this, ConnectBluetoothActivity.class);
		startActivity(intent);
	}
	
	/*
	 * Called when the MainActivity passes out of the foreground
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isBound) {
			unbindService(serviceConnection);
			isBound = false;
		}			
	}

    @Override
    protected void onResume() {
        super.onResume();
    }
	
	/* 
	 * Define custom ServiceConnection for handling BluetoothService
	 */
	private ServiceConnection serviceConnection = new ServiceConnection() {

		/*
		 * Wait until onServiceConnected() is called before attempting to
		 * access a BluetoothService method and updateStatus()
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			BTBinder binder = (BTBinder) service;
			btService = binder.getService();
			isBound = true;
			if(btAddress != null && !btService.getConnectionStatus())
	    		btService.tryConnect(btAddress);
			updateStatus();
			Log.d("MAIN_ACTIVITY", "onServiceConnected started...");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBound = false;		
			Log.d("MAIN_ACTIVITY", "onServiceDisconnected started...");
		}
	};
}
