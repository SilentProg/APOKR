package com.savonikyurii.beatifulkrivbas.GeolocationAPI.LocationListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
/*Клас реалізації інтерфейсу LocationListener*/
public class MyLocListener implements LocationListener {
    /*Поля класу*/
    private LocListenerInterface locListenerInterface;
    /*Перевантажені методи інтерфейсу LocationListener*/
    @Override
    //метод зміни координат
    public void onLocationChanged(@NonNull Location location) {
        //викликаємо метод onChangeLocation розробленого нами інтерфейса
        locListenerInterface.onChangeLocation(location);
    }
    /*Метод встановлення інтерфейса*/
    public void setLocListenerInterface(LocListenerInterface locListenerInterface) {
        this.locListenerInterface = locListenerInterface;
    }
    /*Не використані методи інтерфейса LocationListener*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(@NonNull String provider) {}
    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}
