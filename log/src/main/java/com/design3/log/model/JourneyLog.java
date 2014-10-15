package com.design3.log.model;

/**
 * CLASS: JOURNEYLOG
 *
 * Models the instance data used for a Journey. Journey has many instances of JourneyLog
 * objects which make up the Journey's data.  This class is often grouped with others in a List.
 */
public class JourneyLog {

    private long journeyID, carID;
    private String time;
    private double distance, speed, fuelEconomy;

    /* Primary Constructor */
    public JourneyLog(long journeyID, long carID, String time, double distance,
                   double fuelEconomy, double speed) {
        this.journeyID = journeyID;
        this.carID = carID;
        this.time = time;
        this.distance = distance;
        this.fuelEconomy = fuelEconomy;
        this.speed = speed;
    }

    public long getJourneyID() { return journeyID; }
    public long getCarID() { return carID; }
    public double getDistance() { return distance; }
    public double getSpeed() { return (speed); }
    public double getFuelEconomy() { return fuelEconomy; }
    public String getTime() { return time; }
    public double getTimeAsDouble() {
        return Double.valueOf(time.split(" ")[1].split(":")[1]) // minute +
                + Double.valueOf(time.split(" ")[1].split(":")[2])/60; // second
    }
}
