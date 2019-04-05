package com.irfan.draft1.Maps;

import java.io.Serializable;

public class BusInfo implements Serializable {

    private String name;
    private String imageURL;

    private String plateNumber;
    private Coordinates coordinates;

    public BusInfo() {

    }

    public BusInfo(String name, String imageURL, String plateNumber, Coordinates coordinates) {
        this.name = name;
        this.imageURL = imageURL;
        this.plateNumber = plateNumber;
        this.coordinates = coordinates;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
}
