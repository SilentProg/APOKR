package com.savonikyurii.beatifulkrivbas.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentMapBinding;
import com.savonikyurii.beatifulkrivbas.helpers.CustomInfoWindowGoogleMap;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;


import java.util.ArrayList;
import java.util.List;
//клас контролер мапи
public class MapFragment extends Fragment implements OnMapReadyCallback{
    //оголошення полів
    private static final String TAG = "Map";
    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private DatabaseReference mRefData;
    public static List<Place> allMarkers;
    public static Place place;
    private Polyline currentPolyline;
    private GeoApiContext mGeoApiContext = null;
    //створення вью
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        //виклик ініціалізація
        init();
        initMarkers();
        //поверення вью
        return binding.getRoot();
    }


    @Override //метод, який відбувається при створення мапи
    public void onMapReady(GoogleMap googleMap) {
        //ініціалізовуємо зміну мапи
        mMap = googleMap;
        //якщо місце не нул то переводимо камеру на нього
        if (place!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatitude(),place.getLongtude()), 15));
        }else{
            //інакше на центр міста
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.91032835703162, 33.39146400501618), 10));
        }
        //створення адаптера інформаційного вікна мапи та його встановлення
        CustomInfoWindowGoogleMap infoWindowGoogleMap = new CustomInfoWindowGoogleMap(getActivity());
        mMap.setInfoWindowAdapter(infoWindowGoogleMap);
        //перевіряємо дозволи на відстеження геоданних
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
        //натискання на інформаційне вікно
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(@NonNull Marker marker) {
                Place info = (Place) marker.getTag();
                Log.d("Info", info.toString());
                Log.d("Map", "Click");
                InfoWindowClick(info);
            }
        });
    }
    //метод ініціалізації
    private void init(){
        //знаходимо мапу
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //отримуємо посилання на джерело даних
        mRefData = FirebaseDatabase.getInstance().getReference();
        //отримуємо мапу
        mapFragment.getMapAsync(this);
        //ініціалізація гео контексту
        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBR5UdpoDsXIU_jax39-Yo43qKVQ_XttHU")
                    .build();
        }
    }
    //метод заповення мапи маркерами
    private void initMarkers(){
       allMarkers = new ArrayList<>();
       //зчитуємо дані з джерела
        mRefData.child("places").addValueEventListener(new ValueEventListener() {
            @Override //додаємо маркери до списку
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (allMarkers.size()>0) allMarkers.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    for (DataSnapshot data: ds.getChildren()){
                        Place place = data.getValue(Place.class);
                        assert place != null;
                        allMarkers.add(place);
                        updateUI(allMarkers);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    //оновлюємо інтерфейс
    private void updateUI(List<Place> list){
        mMap.clear();
        //додаємо маркери на карту
        for (Place place: list) {
            LatLng temp = new LatLng(place.getLatitude(), place.getLongtude());
            MarkerOptions marker = new MarkerOptions().position(temp);
            Marker m = mMap.addMarker(marker);
            m.setTag(place);
        }
    }
    //при натисканні на інформаційне вікна маркера
    private void InfoWindowClick(Place place){
        Log.d("place", place.toString());
        DetailsFragment.place = place;
        //відкриваємо деталі уього місця
        NavHostFragment.findNavController(this).navigate(R.id.nav_details);
    }
}