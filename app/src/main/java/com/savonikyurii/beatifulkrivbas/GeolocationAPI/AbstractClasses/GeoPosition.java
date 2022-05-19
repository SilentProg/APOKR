package com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses;
import java.io.Serializable;
/*Фундаментальний клас GeoPosition*/
/*Призначений для зберігання базової інформації про місце*/
public abstract class GeoPosition implements Serializable {
    /*Поля класу*/
    private String title;
    private double latitude;
    private double longtude;
    /*Конструктори*/
    public GeoPosition() {
    }
    public GeoPosition(String title, double latitude, double longtude) {
        this.title = title;
        this.latitude = latitude;
        this.longtude = longtude;
    }
    /*Методи встановлення та отримання полів класу*/
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
    /*Метод який дозволяє перетворити об'єкт класу в його рядковий аналог*/
    @Override
    public String toString() {
        return "GeoPosition{" +
                "title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longtude=" + longtude;
    }
}
