package com.savonikyurii.beatifulkrivbas.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
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

        TextView title = view.findViewById(R.id.text_title_map);
        TextView category = view.findViewById(R.id.text_category_map);
        ImageView img = view.findViewById(R.id.image);

        Place infoWindowData = (Place) marker.getTag();
        title.setText(infoWindowData.getTitle());
        category.setText(infoWindowData.getCategory());
        Picasso.get().load(infoWindowData.getImageuri()).placeholder(R.drawable.image_placeholder).into(img);

        return view;
    }
}
