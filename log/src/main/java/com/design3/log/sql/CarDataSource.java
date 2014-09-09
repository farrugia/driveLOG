package com.design3.log.sql;

import java.util.ArrayList;
import java.util.List;

import com.design3.log.model.Car;

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

public class CarDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private CarSQLHelper dbHelper;
	private String[] allColumns = 
		{ CarSQLHelper.COLUMN_ID, CarSQLHelper.COLUMN_MAKE, 
		  CarSQLHelper.COLUMN_MODEL, CarSQLHelper.COLUMN_YEAR,
		  CarSQLHelper.COLUMN_VIN, CarSQLHelper.COLUMN_ODOMETER,
		  CarSQLHelper.COLUMN_FUEL_CAPACITY, CarSQLHelper.COLUMN_FUEL_CURR_ECO,
		  CarSQLHelper.COLUMN_FUEL_CURR_ECO, CarSQLHelper.COLUMN_FUEL_AV_ECO
		};
	
	// Constructor
	public CarDataSource(Context context) {
		dbHelper = new CarSQLHelper(context);
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
	
	public Car createCar(Car car) {
		ContentValues values = new ContentValues();
		values.put(CarSQLHelper.COLUMN_MAKE, car.getMake());
		values.put(CarSQLHelper.COLUMN_MODEL, car.getModel());
		values.put(CarSQLHelper.COLUMN_YEAR, car.getYear());
		values.put(CarSQLHelper.COLUMN_VIN, car.getVIN());
		values.put(CarSQLHelper.COLUMN_ODOMETER, car.getOdometer());
		values.put(CarSQLHelper.COLUMN_FUEL_CAPACITY, car.getFuelCapacity());
		values.put(CarSQLHelper.COLUMN_FUEL_CURRENT, car.getFuelCurrent());
		values.put(CarSQLHelper.COLUMN_FUEL_AV_ECO, car.getFuelAverageEconomy());
		values.put(CarSQLHelper.COLUMN_FUEL_CURR_ECO, car.getFuelCurrentEconomy());
		
		long insertId = database.insert(CarSQLHelper.TABLE_CARS, null, values);
		
		Cursor cursor = database.query(CarSQLHelper.TABLE_CARS, 
						allColumns, CarSQLHelper.COLUMN_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst(); // moves the cursor to the first row
		Car newCar = cursorToCar(cursor);
		cursor.close();
		return newCar;
	}
	
	public void deleteCar(Car car) {
		long id = car.getCarID();
		database.delete(CarSQLHelper.TABLE_CARS, 
				CarSQLHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Car> getAllCars() {
		List<Car> cars = new ArrayList<Car>();
		
		Cursor cursor = database.query(CarSQLHelper.TABLE_CARS, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		
		// iterate the cursor until finished
		while(!cursor.isAfterLast()) {
			Car car = cursorToCar(cursor);
			cars.add(car);
			cursor.moveToNext();
		}
		cursor.close();
		return cars;
	}
	
	// method to convert a cursor row location to a Car object
	private Car cursorToCar(Cursor cursor) {
		/* index is as follows: 0: id, 1: make, 2: model, 3: year, 4: VIN,
								5: odometer, 6: fuelCap, 7: fuelCurr, 
								8: fuelAvEco, 9: fuelCurrEco */
		Car car = new Car(cursor.getLong(0), cursor.getString(1),
						  cursor.getString(2), cursor.getString(3));
		car.setVIN(cursor.getLong(4));
		car.setOdometer(cursor.getLong(5));
		car.setFuelCapacity(cursor.getDouble(6));
		car.setFuelCurrent(cursor.getDouble(7));
		car.setFuelAverageEconomy(cursor.getDouble(8));
		car.setFuelCurrentEconomy(cursor.getDouble(9));
		return car;
	}
}
