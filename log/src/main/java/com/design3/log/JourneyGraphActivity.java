package com.design3.log;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.model.JourneyLog;
import com.design3.log.sql.JourneyLogDataSource;
import com.design3.log.sql.JourneyLogSQLHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;


public class JourneyGraphActivity extends Activity {

    public static String SPEED = "speed";
    public static String ECONOMY = "economy";

    private Journey journey;
    private Car car;
    private JourneyLogDataSource journeyLogDB;
    private ArrayList<JourneyLog> journeyLogList;
    private GraphView graphView;
    private GraphViewSeries graphViewSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_graph);

        // open JourneyData database
        journeyLogDB = new JourneyLogDataSource(this);
        journeyLogDB.open();

        // get intent
        if(getIntent().hasExtra(Journey.EXTRA_JOURNEY)) {
            journey = getIntent().getExtras().getParcelable(Journey.EXTRA_JOURNEY);
            car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
            setTitle(car.toString());
        }

        journeyLogList = journeyLogDB.getJourneyDataForJourney(JourneyLogSQLHelper.COLUMN_JOURNEY_ID
                + " = " + journey.getJourneyID());
        setGraph(SPEED);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.journey_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_speed :
                if(checked) setGraph(SPEED);
                break;
            case R.id.radio_economy :
                if(checked) ; setGraph(ECONOMY);
                break;
        }
    }

    public void setGraph(String graphType) {
        TextView xaxis = (TextView) findViewById(R.id.label_xaxis);
        TextView yaxis = (TextView) findViewById(R.id.label_yaxis);
        TextView start = (TextView) findViewById(R.id.label_start);
        if(journeyLogList != null) {
            if (graphType == SPEED) {
                graphView = new LineGraphView(this, "Journey Speed");
                graphViewSeries = getSpeedVsTime(journeyLogList);
                yaxis.setText("km/h");
            } else {
                graphView = new LineGraphView(this, "Journey Economy");
                graphViewSeries = getEconomyVsTime(journeyLogList);
                yaxis.setText("L/100km");
            }
            graphView.removeAllSeries();
            graphView.addSeries(graphViewSeries); // data
            graphView.setScalable(true);
            graphView.setScrollable(true);
            graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.text_regular));
            graphView.getGraphViewStyle().setNumHorizontalLabels(5);
            graphView.getGraphViewStyle().setNumVerticalLabels(6);

            LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(graphView);

            xaxis.setText("minutes");
            start.setText("Start:  " + journey.getStartTime());
        }
    }

    protected GraphViewSeries getSpeedVsTime(ArrayList<JourneyLog> journeyLogList) {
        GraphViewData[] graphViewData = new GraphViewData[journeyLogList.size()];
        for(int i = 0; i < journeyLogList.size(); i++) {
            graphViewData[i] = new GraphViewData(journeyLogList.get(i).getTimeAsDouble()
                    -journey.getStartMinuteAsDouble(),journeyLogList.get(i).getSpeed());
        }
        GraphViewSeries graphViewSeries = new GraphViewSeries(graphViewData);
        return graphViewSeries;
    }

    protected GraphViewSeries getEconomyVsTime(ArrayList<JourneyLog> journeyLogList) {
        GraphViewData[] graphViewData = new GraphViewData[journeyLogList.size()];
        for(int i = 0; i < journeyLogList.size(); i++) {
            graphViewData[i] = new GraphViewData(journeyLogList.get(i).getTimeAsDouble()
                    -journey.getStartMinuteAsDouble(), journeyLogList.get(i).getFuelEconomy());
        }
        GraphViewSeries graphViewSeries = new GraphViewSeries(graphViewData);
        return graphViewSeries;
    }
}
