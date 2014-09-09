package com.design3.log.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {
	
	public static final String EXTRA_CAR = "com.design3.log.model.EXTRA_CAR";
	
	private long   carID, odometer, VIN;
	private double fuelCapacity, fuelCurrent, fuelCurrentEconomy, fuelAverageEconomy;
	private String make, model, year;
	
	// Basic constructor for the Car class
	public Car(long carID, String make, String model, String year) {
		this.carID = carID;
		this.make = make;
		this.model = model;
		this.year = year;
	}
	
	/* Constructor which caters for Parcel implementation */
	public Car(Parcel parcel) {
		carID = parcel.readLong();
		odometer = parcel.readLong();
		VIN = parcel.readLong();
		fuelCapacity = parcel.readDouble();
		fuelAverageEconomy = parcel.readDouble();
		fuelCurrentEconomy = parcel.readDouble();
		fuelCurrent = parcel.readDouble();
		make = parcel.readString();
		model = parcel.readString();
		year = parcel.readString();
	}
	
	@Override
	public String toString() {
		return this.make + " " + this.model;
	}

	/* Some getters and setters */
	public String getModel() {return model;}
	public String getMake() {return make;}
	public String getYear() {return year;}
	public long getCarID() {return carID;}
	
	public void setOdometer(long odometer) {this.odometer = odometer;}
	public long getOdometer() {return odometer; }
	
	public void setFuelCapacity(double fuelCap) {this.fuelCapacity = fuelCap;}
	public double getFuelCapacity() {return fuelCapacity;}
	
	public void setFuelCurrent(double fuelCurr) {this.fuelCurrent = fuelCurr;}
	public double getFuelCurrent() {return fuelCurrent;}
	
	public void setFuelAverageEconomy(double fuelAvEc) 
		{this.fuelAverageEconomy = fuelAvEc;}
	public double getFuelAverageEconomy() {return fuelAverageEconomy;}
	
	public void setFuelCurrentEconomy(double fuelCurrEc) 
		{this.fuelCurrentEconomy = fuelCurrEc;}
	public double getFuelCurrentEconomy() {return fuelCurrentEconomy;}
	
	public void setVIN(long VIN) {this.VIN = VIN;}
	public long getVIN() {return VIN;}

	/* Parcelable overridden methods are below */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(carID);
		out.writeLong(odometer);
		out.writeLong(VIN);
		out.writeDouble(fuelCapacity);
		out.writeDouble(fuelAverageEconomy);
		out.writeDouble(fuelCurrentEconomy);
		out.writeDouble(fuelCurrent);
		out.writeString(make);
		out.writeString(model);
		out.writeString(year);
	}
	
	public static final Parcelable.Creator<Car> CREATOR 
							= new Parcelable.Creator<Car>() {

		@Override
		public Car createFromParcel(Parcel source) {
			return new Car(source);
		}

		@Override
		public Car[] newArray(int size) {
			return new Car[size];
		}
	};
}