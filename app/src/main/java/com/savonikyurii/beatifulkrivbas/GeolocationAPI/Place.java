package com.savonikyurii.beatifulkrivbas.GeolocationAPI;
import com.google.android.gms.maps.model.LatLng;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses.GeoPosition;

import java.io.Serializable;
/*Клас нащадок GeoPosition*/
/*Слугує для розширення полями класу предка та зберігання інформації про місце*/
public class Place extends GeoPosition implements Serializable {
    /*Поля класу*/
    private String adres; //адреса
    private String imageuri; //посилання на фото
    private String category; //категорія
    private String bigdescription; //Опис місця
    private int rate; //рейтинг місця за шкалою від 0 до 10
    /*Конструктори*/
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
    /*Геттери та сеттери*/
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
    public LatLng getLatLng(){
        return new LatLng(getLatitude(), getLongtude());
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    /*Метод для перетворення класу в рядок*/
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
