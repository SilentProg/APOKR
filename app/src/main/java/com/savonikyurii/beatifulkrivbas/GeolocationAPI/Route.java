package com.savonikyurii.beatifulkrivbas.GeolocationAPI;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses.GeoPosition;

import java.util.ArrayList;
import java.util.List;

/*Клас контролю за мандрівкою*/
public class Route {
    /*Поля класу*/
    private static Place currentDestination; // поточна ціль
    private static List<Place> list = new ArrayList<>(); // список місць мандрівки
    private static DatabaseReference mRefData; //посилання на джерело даних
    private static PolylineData mPolyLinesData; //поточний маршрут
    //отримання контексту геолокації
    private static GeoApiContext mGeoApiContext =
            new GeoApiContext.Builder().apiKey("AIzaSyBR5UdpoDsXIU_jax39-Yo43qKVQ_XttHU").build();
    private static String TAG = "ROUTE";

    /*Метод отримання поточної цілі мантривки*/
    public static Place getCurrentDestination(){
        return currentDestination;
    }
    /*Метод додавання місця до мандріки*/
    public static void addPlace(Place place){
        list.add(place);
    }
    /*Отримання списку місць поточної мандрівки*/
    public static List<Place> getRoute(){
        return list;
    }

    /*Метод встановлення поточної цілі мандрівки*/
    public static void setCurrentDestination(Place currentDestination) {
        Route.currentDestination = currentDestination;
    }
    /*Видалення місця за індексом*/
    public static void removeByIndex(int i){
        list.remove(i);
    }
    /*Очистка списка мандрівки*/
    public static void clear(){
        list.clear();
    }

    /*Медод розрахунку відстані між координатами*/
    public static double calculateDistance(LatLng start, LatLng end) {
        //оголошення змінної результат
        float[] results = new float[1];
        //рахуємо відстань
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        //повертаємо результат
        return results[0];
    }
    public static <T extends GeoPosition> double calculateDistance(T start, T end) {
        //оголошення змінної результат
        float[] results = new float[1];
        //рахуємо відстань
        Location.distanceBetween(start.getLatitude(), start.getLongtude(), end.getLatitude(), end.getLongtude(), results);
        //повертаємо результат
        return results[0];
    }
    /*Метод розрахунку маршруту між двома місцями*/
    public static DirectionsResult calculateDirections(LatLng start, LatLng end, boolean alt, boolean isClear) {
        final DirectionsResult[] directionsResult = {null};
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(end.latitude,end.longitude);//Створюємо змінну координат цілі
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);//Створюємо запит на розрахунок маршруту
        directions.alternatives(alt);//встановлюємо параметри для запиту
        directions.origin(new com.google.maps.model.LatLng(start.latitude,start.longitude));//встановлюємо початкову точку
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {//відправляємо запит
            @Override public void onResult(DirectionsResult result) {//при отриманому результаті
                directionsResult[0] = result; //повертаємо результат
            }
            @Override public void onFailure(Throwable e) {//при помилці
                System.out.println("calculateDirections: Failed to get directions: " + e.getMessage());//виводимо помилку в консоль
            }});
        try {Thread.sleep(3000);} catch (Exception e) {}
        //повертаємо результат
        return directionsResult[0];
    }
    /*Метод зуму на побудований маршрут*/
    public static void zoomRoute(List<LatLng> lstLatLngRoute, GoogleMap mMap) {
        //перевіряємо чи передані змінні не пусті
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        // створюємо LatLngBounds
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        //заповнюємо LatLngBounds даними
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);
        //встановлюємо відступи
        int routePadding = 120;
        //будуємо LatLngBounds
        LatLngBounds latLngBounds = boundsBuilder.build();
        //переміщюємо камеру на потрібну позицію
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }
}
