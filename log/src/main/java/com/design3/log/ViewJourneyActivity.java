package com.design3.log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.sql.CarDataSource;
import com.design3.log.sql.JourneyDataSource;
import com.design3.log.sql.JourneyLogDataSource;
import com.design3.log.sql.JourneySQLHelper;

public class ViewJourneyActivity extends Activity {

    private Journey journey;
    private Car car;
    private JourneyDataSource journeysDB;
    private JourneyLogDataSource journeyLogDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        journeysDB = new JourneyDataSource(this);
        journeysDB.open();
        journeyLogDB = new JourneyLogDataSource(this);
        journeyLogDB.open();

        setContentView(R.layout.activity_view_journey);

        // Check intent contents
        if(getIntent().hasExtra(Journey.EXTRA_JOURNEY)) {
        	journey = getIntent().getExtras().getParcelable(Journey.EXTRA_JOURNEY);
            car = getIntent().getExtras().getParcelable(Car.EXTRA_CAR);
        	setTitle(car.toString());
            fillView();
        }

        else if(savedInstanceState != null) {
            journey = savedInstanceState.getParcelable(Journey.EXTRA_JOURNEY);
            journey = journeysDB.getJourney(journey.getJourneyID());
            fillView();
        }

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
	


    protected void fillView() {

        TextView journeyIdText,  startText, stopText, useTypeText,
                avgEconomyText, totalKMText, totalRunMinutesText, avgSpeedText;

        journeyIdText = (TextView)findViewById(R.id.text_journeyid);
        startText = (TextView)findViewById((R.id.text_start));
        stopText = (TextView)findViewById(R.id.text_stop);
        useTypeText = (TextView)findViewById(R.id.text_use_type);
        avgEconomyText = (TextView)findViewById(R.id.text_avg_economy);
        avgSpeedText = (TextView)findViewById(R.id.text_avg_speed);
        totalKMText = (TextView)findViewById(R.id.text_total_km);
        totalRunMinutesText = (TextView)findViewById(R.id.text_total_run_minutes);

        journeyIdText.setText(String.format("# %04d",(int)journey.getJourneyID()));
        startText.setText(journey.getStartTime());
        stopText.setText(journey.getStopTime());
        useTypeText.setText(String.valueOf(journey.getUseType()));
        avgEconomyText.setText(String.format("%2.1f  L/100km", journeyLogDB.getJourneyAvgEconomy(journey.getJourneyID())));
        avgSpeedText.setText(String.format("%2.1f  km/h", journeyLogDB.getJourneyAvgSpeed(journey.getJourneyID())));
        totalKMText.setText(String.format("%d  km", (int)journeyLogDB.getJourneyTotalDistance(journey.getJourneyID())));
        totalRunMinutesText.setText(String.valueOf(journey.getRunTimeMinutes()) + "  mins");
    }

    public void graphJourney(View view) {
        Intent intent = new Intent(this, JourneyGraphActivity.class);
        intent.putExtra(Journey.EXTRA_JOURNEY, journey);
        intent.putExtra(Car.EXTRA_CAR, car);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(Journey.EXTRA_JOURNEY, journey);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        journeysDB.close();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        journeysDB.open();
        journey = journeysDB.getJourney(journey.getJourneyID());
        fillView();
        super.onRestart();
    }
}
