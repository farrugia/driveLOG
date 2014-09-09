package com.design3.log.model;

import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Journey implements Parcelable {

	public static final String EXTRA_JOURNEY    = "com.design3.log.model.EXTRA_JOURNEY";
	
	public enum UseType {BUSINESS, PERSONAL};
	
	private SimpleDateFormat startTime, stopTime;
	private long startOdometer, stopOdometer, carID, journeyID;
	private double fuelAvgEconomy, fuelTotalUsed;
	private UseType useType;
	
	/* Primary Constructor */
	public Journey(long journeyID, long carID, UseType useType, 
			SimpleDateFormat startTime, long startOdometer) {
		this.journeyID = journeyID;
		this.carID = carID;
		this.useType = useType;
		this.startTime = startTime;
		this.startOdometer = startOdometer;
	}
	
	/* Parcel Constructor */
	@SuppressLint("SimpleDateFormat") 
	public Journey(Parcel parcel) {
		journeyID 	  = parcel.readLong();
		carID		  = parcel.readLong();
		startOdometer = parcel.readLong();
		stopOdometer  = parcel.readLong();
		startTime     = new SimpleDateFormat(parcel.readString());
		stopTime	  = new SimpleDateFormat(parcel.readString());
		useType 	  = UseType.valueOf(parcel.readString());
		fuelAvgEconomy= parcel.readDouble();
		fuelTotalUsed = parcel.readDouble();
	}
	
	protected void finishJourney(SimpleDateFormat stopTime, long stopOdometer) {
		this.stopTime = stopTime;
		this.stopOdometer = stopOdometer;
	}
	
	/* Getter and setters */
	
	public long getJourneyID() { return journeyID; }
	public long getCarID() { return carID; }
	public SimpleDateFormat getStartTime() { return startTime; }
	public long getStartOdometer() { return startOdometer; }
	
	public SimpleDateFormat getStopTime()  { return stopTime;  }
	public void setStopTime(SimpleDateFormat stopTime) { this.stopTime = stopTime; }
	
	public long getStopOdometer()  { return stopOdometer;  }
	public void setStopOdometer(long stopOdometer) 
		{ this.stopOdometer = stopOdometer; }
	
	public double getFuelAvgEconomy() { return fuelAvgEconomy; }
	public void setFuelAvgEconomy(double fuelAvgEconomy)
		{ this.fuelAvgEconomy = fuelAvgEconomy; }
	
	public double getFuelTotalUsed()  { return fuelTotalUsed;  }
	public void setFuelTotalUsed(double fuelTotalUsed) 
		{ this.fuelTotalUsed = fuelTotalUsed; }
	
	public String getUseType() { return useType.toString(); }
	protected void setUseType(UseType useType) { this.useType = useType; }
	
	@Override
	public String toString() {
		return carID + ": Journey : " + useType.toString() + " : "
				+ startTime.toPattern() + " -> " + stopTime.toPattern();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeLong(journeyID);
		out.writeLong(carID);
		out.writeLong(startOdometer);
		out.writeLong(stopOdometer);
		out.writeString(startTime.toString());
		out.writeString(stopTime.toString());
		out.writeString(useType.toString());
		out.writeDouble(fuelAvgEconomy);
		out.writeDouble(fuelTotalUsed);
	}
	
	public static final Parcelable.Creator<Journey> CREATOR 
	= new Parcelable.Creator<Journey>() {

		@Override
		public Journey createFromParcel(Parcel source) {
			return new Journey(source);
		}

		@Override
		public Journey[] newArray(int size) {
			return new Journey[size];
		}
	};
}
