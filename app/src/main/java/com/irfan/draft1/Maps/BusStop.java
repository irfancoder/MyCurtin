package com.irfan.draft1.Maps;

import java.io.Serializable;

public class BusStop implements Serializable {

    private Double latitude,longitude;
    private String name;


    public BusStop()
    {

    }

    public BusStop(Double latitude, Double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object obj) {

        if(obj instanceof BusStop)
        {
            BusStop temp = (BusStop) obj;
            return this.name.equals(temp.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}


