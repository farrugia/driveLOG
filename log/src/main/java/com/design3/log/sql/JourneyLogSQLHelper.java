package com.design3.log.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class JourneyLogSQLHelper extends SQLiteOpenHelper {

	// Here defined are the table field constants
	public static final String TABLE_JOURNEY_LOG  	= "journeyLog";
	public static final String COLUMN_JOURNEY_ID   	= "journeyID";
	public static final String COLUMN_CAR_ID		= "carID";
	public static final String COLUMN_TIME       	= "timeStamp";
	public static final String COLUMN_DISTANCE      = "distanceStamp";
	public static final String COLUMN_FUEL_ECONOMY = "fuelCurrEco";
    public static final String COLUMN_SPEED         = "speed";

	private static final String DATABASE_NAME = "journeyLog.db";
	private static final int 	DATABASE_VERSION = 1;

	// SQL database creation statement used in onCreate()
	private static final String DATABASE_CREATE
		= "create table " + TABLE_JOURNEY_LOG + "("
	/* COLUMN NO. */
	/* 0 */	+ COLUMN_JOURNEY_ID    		+ " integer not null, "
	/* 1 */	+ COLUMN_CAR_ID  			+ " integer not null, "
	/* 2 */ + COLUMN_TIME          		+ " text not null, "
	/* 3 */ + COLUMN_DISTANCE    		+ " real not null, "
	/* 4 */ + COLUMN_FUEL_ECONOMY		+ " real not null, "
    /* 5 */ + COLUMN_SPEED              + " real not null);";

	public JourneyLogSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(JourneyLogSQLHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEY_LOG); // delete table
		onCreate(db); // recreate table
	}
}
