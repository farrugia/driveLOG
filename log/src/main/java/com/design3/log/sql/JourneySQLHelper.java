package com.design3.log.sql;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class JourneySQLHelper extends SQLiteOpenHelper {
	
	// Here defined are the table field constants
	public static final String TABLE_JOURNEYS  		= "journeys";
	public static final String COLUMN_ID   			= "_id";
	public static final String COLUMN_CAR_ID		= "carID";
	public static final String COLUMN_USE_TYPE 		= "useType";
	public static final String COLUMN_START_TIME	= "startTime";
	public static final String COLUMN_STOP_TIME 	= "stopTime";
	public static final String COLUMN_START_ODOMETER	= "startOdometer";
	public static final String COLUMN_STOP_ODOMETER 	= "stopOdometer";
	public static final String COLUMN_FUEL_AV_ECO 		= "fuelAvEco";
	public static final String COLUMN_FUEL_TOTAL_USED	= "fuelTotalUsed";
    public static final String COLUMN_TOTAL_DISTANCE    = "totalDistance";
    public static final String COLUMN_AVG_SPEED         = "avgSpeed";
	
	private static final String DATABASE_NAME = "journeys.db";
	private static final int 	DATABASE_VERSION = 1;
	
	// SQL database creation statement used in onCreate()
	private static final String DATABASE_CREATE 
		= "create table " + TABLE_JOURNEYS + "(" 
	/* COLUMN NO. */
	/* 0 */	+ COLUMN_ID    				+ " integer primary key autoincrement, "
	/* 1 */	+ COLUMN_CAR_ID  			+ " integer not null, "
	/* 2 */	+ COLUMN_USE_TYPE 			+ " text not null, "
	/* 3 */	+ COLUMN_START_TIME  		+ "	text not null, "
	/* 4 */ + COLUMN_STOP_TIME   		+ " text, "
	/* 5 */ + COLUMN_START_ODOMETER		+ " integer not null, "
	/* 6 */ + COLUMN_STOP_ODOMETER 		+ " integer, "
	/* 7 */ + COLUMN_FUEL_AV_ECO		+ " real, "
	/* 8 */ + COLUMN_FUEL_TOTAL_USED	+ " real, "
    /* 9 */ + COLUMN_TOTAL_DISTANCE     + " real, "
    /* 10*/ + COLUMN_AVG_SPEED          + " real);";
	
	public JourneySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(JourneySQLHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS); // delete table
		onCreate(db); // recreate table
	}
}
