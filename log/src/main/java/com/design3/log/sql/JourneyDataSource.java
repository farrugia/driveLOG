package com.design3.log.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.design3.log.model.Car;
import com.design3.log.model.Journey;
import com.design3.log.model.Journey.UseType;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*  This class is intended for use as a data access object (DAO).
 *  The DAO is is responsible for providing an interface between the app and
 *  the database, allowing access and modification to the database ONLY
 *  through this interface.
 */

public class JourneyDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private JourneySQLHelper dbHelper;
	private String[] allColumns = 
		{ JourneySQLHelper.COLUMN_ID, JourneySQLHelper.COLUMN_CAR_ID, 
		  JourneySQLHelper.COLUMN_USE_TYPE, JourneySQLHelper.COLUMN_START_TIME,
		  JourneySQLHelper.COLUMN_STOP_TIME, JourneySQLHelper.COLUMN_START_ODOMETER,
		  JourneySQLHelper.COLUMN_STOP_ODOMETER, JourneySQLHelper.COLUMN_FUEL_AV_ECO,
		  JourneySQLHelper.COLUMN_FUEL_TOTAL_USED, JourneySQLHelper.COLUMN_TOTAL_DISTANCE,
          JourneySQLHelper.COLUMN_AVG_SPEED
		};
	
	// Constructor
	public JourneyDataSource(Context context) {
		dbHelper = new JourneySQLHelper(context);
	}
	
	// open method initializes database class variable
	public void open() throws SQLException {
		// creates or opens a database
		database = dbHelper.getWritableDatabase();
//		dbHelper.onUpgrade(database, 1, 1); // Use this statement to reset database
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/* Method to create a new journey tuple */
	public Journey createJourney(Journey journey) {
		ContentValues values = new ContentValues();
		values.put(JourneySQLHelper.COLUMN_CAR_ID, journey.getCarID());
		values.put(JourneySQLHelper.COLUMN_USE_TYPE, journey.getUseType().toString());
		values.put(JourneySQLHelper.COLUMN_START_TIME, journey.getStartTime());
		values.put(JourneySQLHelper.COLUMN_START_ODOMETER, journey.getStartOdometer());
        values.put(JourneySQLHelper.COLUMN_STOP_TIME, journey.getStopTime());
		values.put(JourneySQLHelper.COLUMN_STOP_ODOMETER, journey.getStopOdometer());
		values.put(JourneySQLHelper.COLUMN_FUEL_AV_ECO, journey.getFuelAvgEconomy());
		values.put(JourneySQLHelper.COLUMN_FUEL_TOTAL_USED, journey.getFuelTotalUsed());
        values.put(JourneySQLHelper.COLUMN_TOTAL_DISTANCE, journey.getTotalDistance());
        values.put(JourneySQLHelper.COLUMN_AVG_SPEED, journey.getAvgSpeed());
		
		long insertId = database.insert(JourneySQLHelper.TABLE_JOURNEYS, null, values);
		
		Cursor cursor = database.query(JourneySQLHelper.TABLE_JOURNEYS, 
						allColumns, JourneySQLHelper.COLUMN_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst(); // moves the cursor to the first row
		Journey newJourney = cursorToJourney(cursor);
		cursor.close();
		return newJourney;
	}
	
	public void deleteJourney(Journey journey) {
		database.delete(JourneySQLHelper.TABLE_JOURNEYS, 
				JourneySQLHelper.COLUMN_ID + " = " + journey.getJourneyID(), null);
	}

    public void deleteCarsJourneys(Car car) {
        database.delete(JourneySQLHelper.TABLE_JOURNEYS,
                JourneySQLHelper.COLUMN_CAR_ID + " = " + car.getCarID(), null);
    }
	
	public List<Journey> getJourneys(String selection) {
		List<Journey> journeys = new ArrayList<Journey>();
		
		Cursor cursor = database.query(JourneySQLHelper.TABLE_JOURNEYS, allColumns,
				selection, null, null, null, null);
		cursor.moveToFirst();
		
		// iterate the cursor until finished
		while(!cursor.isAfterLast()) {
			Journey journey = cursorToJourney(cursor);
			journeys.add(journey);
			cursor.moveToNext();
		}
		cursor.close();
		return journeys;
	}

    public Journey getJourney(long id) {
        Cursor cursor = database.query(JourneySQLHelper.TABLE_JOURNEYS, allColumns,
                JourneySQLHelper.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            return cursorToJourney(cursor);
        else return null;
    }
	
	// method to convert a cursor row location to a Journey object
	private Journey cursorToJourney(Cursor cursor) {
		/* index is as follows: 0: id, 1: carID, 2: useType, 3: startTime
								4: stopTime, 5:startOdo, 6: stopOdo,
								7: fuelAvgEco, 8: fuelTotalUsed
								9: totalDistance, 10: averageSpeed */
		
		Journey journey = new Journey(cursor.getLong(0), cursor.getLong(1),
				UseType.valueOf(cursor.getString(2)), cursor.getString(3),
				cursor.getLong(5));
		
		journey.setStopTime(cursor.getString(4));
		journey.setStopOdometer(cursor.getLong(6));
		journey.setFuelAvgEconomy(cursor.getDouble(7));
		journey.setFuelTotalUsed(cursor.getDouble(8));
        journey.setTotalDistance(cursor.getDouble(9));
        journey.setAvgSpeed(cursor.getDouble(10));
		
		return journey;
	}
}
