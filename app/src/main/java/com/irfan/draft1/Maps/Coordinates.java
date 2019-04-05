package com.irfan.draft1.Maps;

import java.io.Serializable;

/**
 * Created by irfan on 27/07/2017.
 */


public class Coordinates implements Serializable {

    private Double latitude, longitude;
    private float bearing;
    private int speed;

    public Coordinates() {

    }

    public Coordinates(Double latitude, Double longitude, float bearing, int speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.speed = speed;

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public float getBearing() {
        return bearing;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
