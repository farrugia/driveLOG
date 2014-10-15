package com.design3.log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.design3.log.adapter.CarArrayAdapter;
import com.design3.log.adapter.JourneyArrayAdapter;
import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.CarDataSource;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneyLogDataSource;
import com.design3.log.sql.JourneySQLHelper;

import java.util.ArrayList;

/**
 Inner static class that extends ListFragment
 */
public class ListJourneysFragment extends ListFragment {

    private JourneyArrayAdapter journeysAdapter; // dynamic array for journeys
    private int sectionNumber;
    private JourneyDataSource journeysDB;
    private JourneyLogDataSource journeyLogDB;
    private CarDataSource carsDB;
    private Car car;
    private long carID;
    /**
     * Need to store these variables so that we can refresh the list when needed
     */
    private LayoutInflater inflater;
    private ViewGroup container;
    private String selection;
    private Bundle savedInstanceState;

    public ListJourneysFragment() { }

    @Override
    public void onAttach(Activity activity) {
        this.journeysDB = ((ListJourneysActivity)activity).getJourneysDB();
        this.carsDB = ((ListJourneysActivity)activity).getCarsDB();
        this.journeyLogDB = ((ListJourneysActivity)activity).getJourneyLogDB();

        this.car = ((ListJourneysActivity)activity).getCar();
        carID = car.getCarID();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;
        Bundle args = getArguments();
        sectionNumber = args.getInt(ListJourneysActivity.EXTRA_SECTION_NUMBER);

        switch(sectionNumber) {
            case 1 :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID() + " AND " +
                        JourneySQLHelper.COLUMN_USE_TYPE + " = '" +
                        Journey.UseType.PERSONAL.toString() + "'";
                break;
            case 2 :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID() + " AND " +
                        JourneySQLHelper.COLUMN_USE_TYPE + " = '" +
                        Journey.UseType.BUSINESS.toString() + "'";
                break;
            case 3 :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID();
                break;
        }

        ArrayList<Journey> journeysArray = (ArrayList<Journey>) journeysDB.getJourneys(selection);

        if(!journeysArray.isEmpty()) {
            journeysAdapter = new JourneyArrayAdapter(inflater.getContext(), journeysArray);
            setListAdapter(journeysAdapter); // set view as list of cars
        }
        else return inflater.inflate(R.layout.activity_list_journeys_empty, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView listView = getListView();
        listView.setVerticalScrollBarEnabled(true);

        // When an item is long pressed a delete dialog props up
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeJourney(position);
                return true;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    // Function to remove a journey entry from the array adapter and database
    protected void removeJourney(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");
        alert.setMessage("Do you want to delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayAdapter<Journey> adapter = ((ArrayAdapter<Journey>) getListAdapter());
                car.subtractJourney(adapter.getItem(position).getUseType());
                carsDB.updateCar(car);
                journeysDB.deleteJourney(adapter.getItem(position));
                journeyLogDB.deleteJourneyDataForJourney(adapter.getItem(position).getJourneyID());
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Journey journey = (Journey)getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ViewJourneyActivity.class);
        intent.putExtra(Journey.EXTRA_JOURNEY, journey);
        intent.putExtra(Car.EXTRA_CAR, car);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
//        car = carsDB.getCar(carID);
//        onCreateView(inflater, container, savedInstanceState);
        super.onStart();
    }
}
