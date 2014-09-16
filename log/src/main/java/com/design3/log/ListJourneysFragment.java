package com.design3.log;

import java.util.ArrayList;

import com.design3.log.adapter.JourneyArrayAdapter;
import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneySQLHelper;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListJourneysFragment extends ListFragment {

    private JourneyArrayAdapter journeysAdapter; // dynamic array for journeys
    private JourneyDataSource journeysDB;
    private Car car;
    private int sectionNumber;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListJourneysFragment newInstance(int sectionNumber, Car car,
                                                   JourneyDataSource journeysDB) {
        ListJourneysFragment fragment = new ListJourneysFragment(sectionNumber, car, journeysDB);
        return fragment;
    }

    public ListJourneysFragment(int sectionNumber, Car car, JourneyDataSource journeysDB) {
        this.sectionNumber = sectionNumber;
        this.car = car;
        this.journeysDB = journeysDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_journeys, container, false);

        String selection;

        switch(sectionNumber) {
            case 1 :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID() + " AND " +
                        JourneySQLHelper.COLUMN_USE_TYPE + " = " + Journey.UseType.PERSONAL.toString();
                break;
            case 2 :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID() + " AND " +
                        JourneySQLHelper.COLUMN_USE_TYPE + " = " + Journey.UseType.BUSINESS.toString();
                break;
            default :
                selection = JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID();
                break;
        }
        ArrayList<Journey> journeysArray = (ArrayList<Journey>) journeysDB.getJourneys(selection);

        if(!journeysArray.isEmpty()) {
            journeysAdapter = new JourneyArrayAdapter(getActivity(), journeysArray);
            setListAdapter(journeysAdapter); // set view as list of cars
        }
        //else setContentView(R.layout.activity_list_journeys); // set view as empty
        return rootView;
    }
}
