package com.savonikyurii.beatifulkrivbas.helpers;

import com.google.firebase.database.DatabaseReference;
import com.savonikyurii.beatifulkrivbas.API.Place;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private static Place currentDestination;
    private static List<Place> list = new ArrayList<>();
    private static DatabaseReference mRefData;



    public static Place getCurrentDestination(){
        return currentDestination;
    }

    public static void addPlace(Place place){
        list.add(place);
    }

    public static List<Place> getRoute(){
        return list;
    }

    public static void setRoute(List<Place> temp){
        list.clear();
        list.addAll(temp);
    }

    public static void setCurrentDestination(Place currentDestination) {
        Route.currentDestination = currentDestination;
    }
    public static void removeByIndex(int i){
        list.remove(i);
    }

    public static void clear(){
        list.clear();
    }
}
