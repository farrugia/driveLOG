package com.design3.log.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.design3.log.model.JourneyLog;

import java.util.ArrayList;

/*  This class is intended for use as a data access object (DAO).
 *  The DAO is is responsible for providing an interface between the app and
 *  the database, allowing access and modification to the database ONLY
 *  through this interface.
 */

public class JourneyLogDataSource {

	// Database fields
	private SQLiteDatabase database;
	private JourneyLogSQLHelper dbHelper;
	private String[] allColumns =
		{ JourneyLogSQLHelper.COLUMN_JOURNEY_ID, JourneyLogSQLHelper.COLUMN_CAR_ID,
		  JourneyLogSQLHelper.COLUMN_TIME, JourneyLogSQLHelper.COLUMN_DISTANCE,
		  JourneyLogSQLHelper.COLUMN_FUEL_ECONOMY, JourneyLogSQLHelper.COLUMN_SPEED
		};

	// Constructor
	public JourneyLogDataSource(Context context) {
		dbHelper = new JourneyLogSQLHelper(context);
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
	
	/* Method to create a new JourneyData tuple */
	public void addJourneyData(JourneyLog journeyLog) {
		ContentValues values = new ContentValues();
        values.put(JourneyLogSQLHelper.COLUMN_JOURNEY_ID, journeyLog.getJourneyID());
		values.put(JourneyLogSQLHelper.COLUMN_CAR_ID, journeyLog.getCarID());
		values.put(JourneyLogSQLHelper.COLUMN_TIME, journeyLog.getTime());
		values.put(JourneyLogSQLHelper.COLUMN_DISTANCE, journeyLog.getDistance());
		values.put(JourneyLogSQLHelper.COLUMN_FUEL_ECONOMY, journeyLog.getFuelEconomy());
        values.put(JourneyLogSQLHelper.COLUMN_SPEED, journeyLog.getSpeed());
		
		database.insert(JourneyLogSQLHelper.TABLE_JOURNEY_LOG, null, values);
	}
	
	public void deleteJourneyDataForJourney(long journeyID) {
		database.delete(JourneyLogSQLHelper.TABLE_JOURNEY_LOG,
				JourneyLogSQLHelper.COLUMN_JOURNEY_ID + " = " + journeyID, null);
	}
	
	public ArrayList<JourneyLog> getJourneyDataForJourney(String selection) {
		ArrayList<JourneyLog> journeyLogList = new ArrayList<JourneyLog>();
		
		Cursor cursor = database.query(JourneyLogSQLHelper.TABLE_JOURNEY_LOG, allColumns,
				selection, null, null, null, null);
		cursor.moveToFirst();
		
		// iterate the cursor until finished
		while(!cursor.isAfterLast()) {
			JourneyLog journeyLog = cursorToJourneyData(cursor);
			journeyLogList.add(journeyLog);
			cursor.moveToNext();
		}
		cursor.close();
		return journeyLogList;
	}

    public double getJourneyAvgEconomy(long journeyID) {
        double sumEco = 0;
        int counter = 0;
        Cursor cursor = database.query(JourneyLogSQLHelper.TABLE_JOURNEY_LOG, allColumns,
                JourneyLogSQLHelper.COLUMN_JOURNEY_ID + " = " + journeyID, null, null, null, null);
        cursor.moveToFirst();

        // iterate the cursor until finished
        while(!cursor.isAfterLast()) {
            JourneyLog journeyLog = cursorToJourneyData(cursor);
            sumEco += journeyLog.getFuelEconomy();
            counter++;
            cursor.moveToNext();
        }
        cursor.close();
        return sumEco/counter;
    }

    public double getJourneyAvgSpeed(long journeyID) {
        double sumSpeed = 0;
        int counter = 0;
        Cursor cursor = database.query(JourneyLogSQLHelper.TABLE_JOURNEY_LOG, allColumns,
                JourneyLogSQLHelper.COLUMN_JOURNEY_ID + " = " + journeyID, null, null, null, null);
        cursor.moveToFirst();

        // iterate the cursor until finished
        while(!cursor.isAfterLast()) {
            JourneyLog journeyLog = cursorToJourneyData(cursor);
            sumSpeed += journeyLog.getSpeed();
            counter++;
            cursor.moveToNext();
        }
        cursor.close();
        return sumSpeed/counter;
    }

    public double getJourneyTotalDistance(long journeyID) {
        double maxDistance = 0;
        Cursor cursor = database.query(JourneyLogSQLHelper.TABLE_JOURNEY_LOG, allColumns,
                JourneyLogSQLHelper.COLUMN_JOURNEY_ID + " = " + journeyID, null, null, null, null);
        cursor.moveToFirst();

        // iterate the cursor until finished
        while(!cursor.isAfterLast()) {
            JourneyLog journeyLog = cursorToJourneyData(cursor);
            if(maxDistance < journeyLog.getDistance())
                maxDistance = journeyLog.getDistance();
            cursor.moveToNext();
        }
        cursor.close();
        return maxDistance;
    }
	
	// method to convert a cursor row location to a JourneyLog object
	private JourneyLog cursorToJourneyData(Cursor cursor) {
		/* index is as follows: 0: journeyID, 1: carID, 2: time
								3: distance, 4: fuelEconomy, 5: speed */
		
		JourneyLog journeyLog = new JourneyLog(cursor.getLong(0), cursor.getLong(1),
				cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5));
		
		return journeyLog;
	}
}
