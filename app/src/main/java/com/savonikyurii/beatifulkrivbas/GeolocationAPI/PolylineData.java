package com.savonikyurii.beatifulkrivbas.GeolocationAPI;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.model.DirectionsLeg;
/*Клас PolylineData*/
/*Слугує для зберігання точок кривої маршрута*/
public class PolylineData {
    /*Поля класу*/
    private Polyline polyline;
    private DirectionsLeg leg;
    /*Конструктор*/
    public PolylineData(Polyline polyline, DirectionsLeg leg) {
        this.polyline = polyline;
        this.leg = leg;
    }
    /*Геттери та сеттери*/
    public Polyline getPolyline() {
        return polyline;
    }
    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }
    public DirectionsLeg getLeg() {
        return leg;
    }
    public void setLeg(DirectionsLeg leg) {
        this.leg = leg;
    }
    /*Метод для перетворення класу в рядок*/
    @Override
    public String toString() {
        return "PolylineData{" +
                "polyline=" + polyline +
                ", leg=" + leg +
                '}';
    }
}