package com.design3.log;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.design3.log.adapter.JourneyArrayAdapter;
import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneySQLHelper;

import java.util.ArrayList;

/**
 Inner static class that extends ListFragment
 */
public class ListJourneysFragment extends ListFragment {

    private JourneyArrayAdapter journeysAdapter; // dynamic array for journeys
    private int sectionNumber;
    private JourneyDataSource journeysDB;
    private Car car;

    public ListJourneysFragment() { }

    @Override
    public void onAttach(Activity activity) {
        this.journeysDB = ((ListJourneysActivity)activity).getJourneysDB();
        this.car = ((ListJourneysActivity)activity).getCar();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        sectionNumber = args.getInt(ListJourneysActivity.EXTRA_SECTION_NUMBER);

        String selection;

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
            default :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID();
                break;
        }
        ArrayList<Journey> journeysArray = (ArrayList<Journey>) journeysDB.getJourneys(selection);

        if(!journeysArray.isEmpty()) {
            journeysAdapter = new JourneyArrayAdapter(inflater.getContext(), journeysArray);
            setListAdapter(journeysAdapter); // set view as list of cars
        }
        //else setContentView(R.layout.activity_list_journeys); // set view as empty
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
