package com.design3.log.adapter;

import java.util.ArrayList;
import com.design3.log.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BTArrayAdapter extends ArrayAdapter<BluetoothDevice> {

	private final Context context;
	private final ArrayList<BluetoothDevice> values;
	
	public BTArrayAdapter(Context context, ArrayList<BluetoothDevice>values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;
	}
	
	// By overriding this method, we can create a custom row view
	// This will be customised for BluetoothDevice objects
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
		imageView.setImageResource(R.drawable.image_bt);
		TextView textView1 = (TextView) rowView.findViewById(R.id.list_text1);
		textView1.setText(values.get(position).getName());
		TextView textView2 = (TextView) rowView.findViewById(R.id.list_text2);
		textView2.setText(values.get(position).getAddress());
		return rowView;
	}
}
