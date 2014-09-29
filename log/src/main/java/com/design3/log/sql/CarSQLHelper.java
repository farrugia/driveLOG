package com.design3.log.sql;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class CarSQLHelper extends SQLiteOpenHelper {
	
	// Here defined are the table field constants
	public static final String TABLE_CARS  		= "cars";
	public static final String COLUMN_ID   		= "_id";
	public static final String COLUMN_MAKE 		= "make";
	public static final String COLUMN_MODEL		= "model";
	public static final String COLUMN_YEAR 		= "year";
	public static final String COLUMN_VIN 		= "VIN";
	public static final String COLUMN_ODOMETER 	= "odometer";
	public static final String COLUMN_FUEL_CAPACITY = "fuelCapacity";
	public static final String COLUMN_FUEL_CURRENT	= "fuelCurrent";
	public static final String COLUMN_FUEL_AV_ECO 	= "fuelAvEco";
    public static final String COLUMN_PERSONAL_JOURNEYS = "personalJourneys";
    public static final String COLUMN_BUSINESS_JOURNEYS = "businessJourneys";
	
	private static final String DATABASE_NAME = "cars.db";
	private static final int 	DATABASE_VERSION = 1;
	
	// SQL database creation statement used in onCreate()
	private static final String DATABASE_CREATE 
		= "create table " + TABLE_CARS + "(" 
	/* COLUMN NO. */
	/* 0 */	+ COLUMN_ID    			+ " integer primary key autoincrement, "
	/* 1 */	+ COLUMN_MAKE  			+ " text not null, "
	/* 2 */	+ COLUMN_MODEL 			+ " text not null, "
	/* 3 */	+ COLUMN_YEAR  			+ "	text not null, "
	/* 4 */ + COLUMN_VIN   			+ " integer, "
	/* 5 */ + COLUMN_ODOMETER		+ " integer, "
	/* 6 */ + COLUMN_FUEL_CAPACITY 	+ " real, "
	/* 7 */ + COLUMN_FUEL_CURRENT	+ " real, "
	/* 8 */ + COLUMN_FUEL_AV_ECO	+ " real, "
    /* 9 */ + COLUMN_PERSONAL_JOURNEYS + " integer, "
    /* 10 */+ COLUMN_BUSINESS_JOURNEYS + " integer);";
	
	public CarSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CarSQLHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to " +
				newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS); // delete table
		onCreate(db); // recreate table
	}
}
