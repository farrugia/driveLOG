package com.design3.log.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BluetoothService extends Service {

	private BluetoothAdapter btAdapter;
	private BluetoothDevice  btDevice;
	private BluetoothSocket  btSocket;
	private InputStream		 istream;
	private OutputStream	 ostream;
	
	private final IBinder binder = new BTBinder();
	
	private static final UUID BT_UUID // SPP UUID service
	= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final String EXTRA_BT_SUCCESS 
		= "com.design3.log.service.EXTRA_BT_SUCCESS";
	public static final String EXTRA_BT_FAILURE 
		= "com.design3.log.service.EXTRA_BT_FAILURE";
	public static final String EXTRA_BT_ADDRESS 
		= "com.design3.log.service.EXTRA_BT_ADDRESS";
	public static final String EXTRA_BT_CONNECTING 
		= "com.design3.log.service.EXTRA_BT_CONNECTING"; 
	public static final String NOTIFICATION  // ID that MainActivity will look for
		= "com.design3.log.service.NOTIFICATION"; 

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("BLUETOOTH_SERVICE","BluetoothService starting...");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    return START_STICKY; /* This flag indicates that this service is not to
	    					  * finish until explicity indicated 		  */
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	/*	 
	 * This will attempt to connect to the address supplied in the given
	 * intent, if successful, will store a BluetoothSocket for future use
	 */
	public boolean tryConnect(String address) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btDevice = (BluetoothDevice)btAdapter.getRemoteDevice(address); 
		
		try {			
    		btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID);
    		btSocket.connect();
    		
    		istream = btSocket.getInputStream();
    		ostream = btSocket.getOutputStream();
    		 		 		
    	} catch (IOException e) { return false; }
		return true;  
	}
	
	/* Function for test purposes */
	public void toggleLED(boolean i) {
		for(int j=0;j<100;j++) {
			try {
				if(i)
					 ostream.write('b');
				else ostream.write('o');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/* Extended version of Binder class catered for BluetoothService class */
	public class BTBinder extends Binder {
		public BluetoothService getService() {
			return BluetoothService.this;
		};
	}
}
