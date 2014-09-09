package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.service.BluetoothService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class AddJourneyActivity extends Activity {

	private Car car;
    private BluetoothService btService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
        	car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString() + ": New Journey");
        }
        
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /*
	     * Start or rebind a BluetoothService and bind it to this activity via
	     * a ServiceConnection object: serviceConnection (implemented at bottom)
	     */
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
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
            BluetoothService.BTBinder binder = (BluetoothService.BTBinder) service;
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
