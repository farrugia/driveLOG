package com.design3.log;

import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Mark Farrugia on 10/09/14.
 */
public class BluetoothWatcher extends Thread {

    private TextView textView;
    private InputStream istream;

    public BluetoothWatcher(TextView textView, InputStream istream) {
        this.textView = textView;
        this.istream = istream;
        start();
    }

    public void run() {
        byte buffer[] = new byte[8];
        while(true) {

            try {
                istream.read(buffer);
                textView.setText(Integer.toHexString(buffer[0]));
                textView.refreshDrawableState();
                Log.d("RECEIVED", Integer.toHexString(buffer[0]));
            } catch (Exception e) {

            }
        }
    }
}

