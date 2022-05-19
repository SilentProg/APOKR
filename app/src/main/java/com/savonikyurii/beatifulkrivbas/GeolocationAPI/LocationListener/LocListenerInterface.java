package com.savonikyurii.beatifulkrivbas.GeolocationAPI.LocationListener;
import android.location.Location;
/*Інтерфейс призначений для відстеження змінення координат пристрою*/
public interface LocListenerInterface {
    /*Метод зміни координат користувача*/
    public void onChangeLocation(Location loc);
}
