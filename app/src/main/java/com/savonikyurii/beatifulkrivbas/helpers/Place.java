package com.savonikyurii.beatifulkrivbas.helpers;

import java.io.Serializable;

public class Place implements Serializable{
    private String title;
    private String adres;
    private String imageuri;
    private String category;
    private String bigdescription;
    private double latitude;
    private double longtude;
    private int rate;

    public Place() {
    }

    public Place(String title, String ad, String imuri, String category, int rate, String bigdescription, double latitude, double longtude) {
        this.title = title;
        this.adres = ad;
        this.rate = rate;
        this.imageuri = imuri;
        this.category = category;
        this.bigdescription = bigdescription;
        this.latitude = latitude;
        this.longtude = longtude;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
