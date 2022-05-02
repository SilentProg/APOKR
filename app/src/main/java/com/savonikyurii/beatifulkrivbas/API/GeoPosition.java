package com.savonikyurii.beatifulkrivbas.API;

public abstract class GeoPosition {
    private String title;
    private double latitude;
    private double longtude;

    public GeoPosition() {
    }

    public GeoPosition(String title, double latitude, double longtude) {
        this.title = title;
        this.latitude = latitude;
        this.longtude = longtude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    @Override
    public String toString() {
        return "GeoPosition{" +
                "title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longtude=" + longtude;
    }
}
