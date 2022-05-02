package com.savonikyurii.beatifulkrivbas.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.savonikyurii.beatifulkrivbas.API.Place;
import com.savonikyurii.beatifulkrivbas.R;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);
        ConstraintLayout container = view.findViewById(R.id.container);
        TextView title = view.findViewById(R.id.text_title_map);
        TextView category = view.findViewById(R.id.text_category_map);
        ImageView img = view.findViewById(R.id.image);


        int currentNightMode = Configuration.UI_MODE_NIGHT_YES & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                container.setBackground(context.getDrawable(R.drawable.bottomsheetbackground));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                container.setBackground(context.getDrawable(R.drawable.bottomsheetbackgrounddark));
                break;
        }


        Place infoWindowData = (Place) marker.getTag();
        title.setText(infoWindowData.getTitle());
        category.setText(infoWindowData.getCategory());

        Picasso.get().load(infoWindowData.getImageuri()).placeholder(R.drawable.image_placeholder).into(img);

        return view;
    }
}
