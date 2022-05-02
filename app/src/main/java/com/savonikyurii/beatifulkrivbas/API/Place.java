package com.savonikyurii.beatifulkrivbas.API;

import java.io.Serializable;

public class Place extends GeoPosition implements Serializable {
    private String adres;
    private String imageuri;
    private String category;
    private String bigdescription;
    private int rate;

    public Place() {
    }

    public Place(String title, double latitude, double longtude, String adres, String imageuri, String category, String bigdescription, int rate) {
        super(title, latitude, longtude);
        this.adres = adres;
        this.imageuri = imageuri;
        this.category = category;
        this.bigdescription = bigdescription;
        this.rate = rate;
    }

    public Place(String adres, String imageuri, String category, String bigdescription, int rate) {
        this.adres = adres;
        this.imageuri = imageuri;
        this.category = category;
        this.bigdescription = bigdescription;
        this.rate = rate;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBigdescription() {
        return bigdescription;
    }

    public void setBigdescription(String bigdescription) {
        this.bigdescription = bigdescription;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return super.toString()+" " +
                "adres='" + adres + '\'' +
                ", imageuri='" + imageuri + '\'' +
                ", category='" + category + '\'' +
                ", bigdescription='" + bigdescription + '\'' +
                ", rate=" + rate +
                '}';
    }
}
