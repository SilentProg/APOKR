package com.savonikyurii.beatifulkrivbas.helpers;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private static List<Place> list = new ArrayList<>();

    public static void addPlace(Place place){
        list.add(place);
    }

    public static List<Place> getRoute(){
        return list;
    }
}
