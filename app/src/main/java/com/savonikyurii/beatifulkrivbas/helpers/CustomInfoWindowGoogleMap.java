package com.savonikyurii.beatifulkrivbas.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.R;
import com.squareup.picasso.Picasso;
//клас призначений для формування інформаційного вікна на мапі
public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    //оголошення змінних
    private Context context;
    //конструктор
    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Nullable
    @Override //метод отримання інформації
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override//метод отримання інформаційного вікна
    public View getInfoWindow(@NonNull Marker marker) {
        //створення інтерфейсу інфо вікна
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);
        ConstraintLayout container = view.findViewById(R.id.container);
        TextView title = view.findViewById(R.id.text_title_map);
        TextView category = view.findViewById(R.id.text_category_map);
        ImageView img = view.findViewById(R.id.image);
        //встановлення тегу
        Place infoWindowData = (Place) marker.getTag();
        title.setText(infoWindowData.getTitle());
        category.setText(infoWindowData.getCategory());
        //завантаження жображення
        Picasso.get().load(infoWindowData.getImageuri()).placeholder(R.drawable.image_placeholder).into(img);
        //повернення інтерфейсу інфо вікна
        return view;
    }
}
