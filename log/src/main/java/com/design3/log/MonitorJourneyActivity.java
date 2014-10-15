package com.design3.log;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.model.JourneyLog;
import com.design3.log.service.Monitor;
import com.design3.log.sql.CarDataSource;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneyLogDataSource;
import com.jjoe64.graphview.GraphView;

import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;

public class MonitorJourneyActivity extends Activity {

    public static String SPEED = "speed";
    public static String ECONOMY = "economy";

    private Car car;
    private Journey journey;
    private JourneyDataSource journeysDB;
    private JourneyLogDataSource journeyLogDB;
    private CarDataSource carsDB;
    private GraphView graphView;
    private GraphViewSeries speedSeries, economySeries;
    private Runnable mTimer;
    private final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monitor_journey);

        // check intent contents
        if(getIntent().hasExtra(Car.EXTRA_CAR)) {
            car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
            setTitle(car.toString() + ": New Journey");
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);

        journeysDB = new JourneyDataSource(this);
        journeysDB.open();
        journeyLogDB = new JourneyLogDataSource(this);
        journeyLogDB.open();
        carsDB = new CarDataSource(this);
        carsDB.open();

        //String start = DateFormat.getDateTimeInstance().format(new Date());
        String start = "07/10/2014 1:30:00";
        journey = new Journey(0, car.getCarID(), Journey.UseType.PERSONAL, start, 101456);
        journey = journeysDB.createJourney(journey);
        final Journey thread_journey = journey;

        car.addJourney(Journey.UseType.PERSONAL);
        carsDB.updateCar(car);

        speedSeries = new GraphViewSeries(new GraphView.GraphViewData[] {new GraphView.GraphViewData(0,0)});
        economySeries = new GraphViewSeries(new GraphView.GraphViewData[] {new GraphView.GraphViewData(0,0)});

        setGraph(SPEED);

        BufferedReader reader;
        final JourneyLog journeyLog[] = new JourneyLog[480];
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("debugjourney.txt")));

            for(int i = 0; i < 480; i++){
                String line = reader.readLine();
                String parts[] = line.split(" ");
                String timePart = parts[0] + " " + parts[1];
                journeyLog[i] = new JourneyLog(thread_journey.getJourneyID(), thread_journey.getCarID(),
                        timePart, 2, Double.valueOf(parts[3]), Double.valueOf(parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTimer = new Monitor(0) {
            @Override
            public void run() {
                try {
                    i++;
                    journeyLogDB.addJourneyData(journeyLog[i]);
                    speedSeries.appendData(new GraphView.GraphViewData(journeyLog[i].getTimeAsDouble()
                            - thread_journey.getStartMinuteAsDouble(), journeyLog[i].getSpeed()), true, 480);
                    economySeries.appendData(new GraphView.GraphViewData(journeyLog[i].getTimeAsDouble()
                            - thread_journey.getStartMinuteAsDouble(), journeyLog[i].getFuelEconomy()), true, 480);
                    if(i < 480) mHandler.postDelayed(mTimer, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mHandler.postDelayed(mTimer, 1000);
    }

    public void setGraph(String graphType) {
        TextView xaxis = (TextView) findViewById(R.id.label_xaxis);
        TextView yaxis = (TextView) findViewById(R.id.label_yaxis);
        TextView startlabel = (TextView) findViewById(R.id.label_start);
        if (graphType == SPEED) {
            graphView = new LineGraphView(this, "Journey Speed");
            graphView.setScalable(true);
            graphView.setScrollable(true);
            graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.text_regular));
            graphView.getGraphViewStyle().setNumHorizontalLabels(5);
            graphView.getGraphViewStyle().setNumVerticalLabels(6);
            graphView.addSeries(speedSeries);
            yaxis.setText("km/h");
        } else {
            graphView = new LineGraphView(this, "Journey Economy");
            graphView.setScalable(true);
            graphView.setScrollable(true);
            graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.text_regular));
            graphView.getGraphViewStyle().setNumHorizontalLabels(5);
            graphView.getGraphViewStyle().setNumVerticalLabels(6);
            graphView.addSeries(economySeries);
            yaxis.setText("L/100km");
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
        layout.removeAllViews();
        layout.addView(graphView);

        xaxis.setText("minutes");
        startlabel.setText("Start:  " + journey.getStartTime());
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
}
