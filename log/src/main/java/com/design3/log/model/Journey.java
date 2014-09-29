package com.design3.log.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Journey implements Parcelable {

	public static final String EXTRA_JOURNEY       = "com.design3.log.model.EXTRA_JOURNEY";
    public static final String EXTRA_JOURNEY_ID    = "com.design3.log.model.EXTRA_JOURNEY_ID";
	
	public enum UseType {BUSINESS, PERSONAL};
	
	private String startTime, stopTime;
	private long startOdometer, stopOdometer, carID, journeyID;
	private double fuelAvgEconomy, fuelTotalUsed, totalDistance, avgSpeed;
	private UseType useType;
	
	/* Primary Constructor */
	public Journey(long journeyID, long carID, UseType useType, 
			String startTime, long startOdometer) {
		this.journeyID = journeyID;
		this.carID = carID;
		this.useType = useType;
		this.startTime = startTime;
		this.startOdometer = startOdometer;
        this.stopOdometer = startOdometer;
        this.stopTime = startTime;
        this.fuelAvgEconomy = 0;
        this.fuelTotalUsed = 0;
        this.totalDistance = 0;
        this.avgSpeed = 0;
	}
	
	/* Parcel Constructor */
	@SuppressLint("SimpleDateFormat") 
	public Journey(Parcel parcel) {
		journeyID 	  = parcel.readLong();
		carID		  = parcel.readLong();
		startOdometer = parcel.readLong();
		stopOdometer  = parcel.readLong();
		startTime     = parcel.readString();
		stopTime	  = parcel.readString();
		useType 	  = UseType.valueOf(parcel.readString());
		fuelAvgEconomy= parcel.readDouble();
		fuelTotalUsed = parcel.readDouble();
        totalDistance = parcel.readDouble();
        avgSpeed      = parcel.readDouble();
	}
	
	protected void finishJourney(String stopTime, long stopOdometer) {
		this.stopTime = stopTime;
		this.stopOdometer = stopOdometer;
	}
	
	/* Getter and setters */
	
	public long getJourneyID() { return journeyID; }
	public long getCarID() { return carID; }
	public String getStartTime() { return startTime; }
	public long getStartOdometer() { return startOdometer; }
	
	public String getStopTime()  { return stopTime;  }
	public void setStopTime(String stopTime) { this.stopTime = stopTime; }
	
	public long getStopOdometer()  { return stopOdometer;  }
	public void setStopOdometer(long stopOdometer) 
		{ this.stopOdometer = stopOdometer; }
	
	public double getFuelAvgEconomy() { return fuelAvgEconomy; }
	public void setFuelAvgEconomy(double fuelAvgEconomy)
		{ this.fuelAvgEconomy = fuelAvgEconomy; }
	
	public double getFuelTotalUsed()  { return fuelTotalUsed;  }
	public void setFuelTotalUsed(double fuelTotalUsed) 
		{ this.fuelTotalUsed = fuelTotalUsed; }

    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }

    public double getAvgSpeed() { return avgSpeed; }
    public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }
	
	public UseType getUseType() { return useType; }

    public int getRunTimeMinutes() {
        String parts[] = startTime.split(" ")[1].split(":");
        int starthour = Integer.parseInt(parts[0]);
        int startminute = Integer.parseInt(parts[1]);

        parts = stopTime.split(" ")[1].split(":");
        int stophour = Integer.parseInt(parts[0]);
        int stopminute = Integer.parseInt(parts[1]);

        int hour, minute;

        if(stophour < starthour)
            hour = 12 - (starthour - stophour);
        else hour = stophour - starthour;
        if(stopminute < startminute)
            minute = 60 - (startminute - stopminute);
        else minute = stopminute - startminute;
        if(stophour < starthour && stopminute < startminute)
            hour--;

        return (hour*60)+minute;
    }

	@Override
	public String toString() {
		return String.format("ID# %04d  ", (int)journeyID) + getStartTime().split(" ")[0];
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
		out.writeString(startTime);
		out.writeString(stopTime);
		out.writeString(useType.toString());
		out.writeDouble(fuelAvgEconomy);
		out.writeDouble(fuelTotalUsed);
        out.writeDouble(totalDistance);
        out.writeDouble(avgSpeed);
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
