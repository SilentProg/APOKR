package com.savonikyurii.beatifulkrivbas.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentMapBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.Place;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private DatabaseReference mRefData;
    public static List<Place> allMarkers;
    public static Place place;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);

        init();
        initMarkers();

        return binding.getRoot();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (place!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatitude(),place.getLongtude()), 15));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.91032835703162, 33.39146400501618), 10));
        }
    }

    private void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mRefData = FirebaseDatabase.getInstance().getReference();


        mapFragment.getMapAsync(this);
    }

    private void initMarkers(){
       allMarkers = new ArrayList<>();
        mRefData.child("places").addValueEventListener(new ValueEventListener() {
            @Override
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(List<Place> list){
        mMap.clear();
        for (Place place: list) {
            LatLng temp = new LatLng(place.getLatitude(), place.getLongtude());
            mMap.addMarker(new MarkerOptions().position(temp).title(place.getTitle()));
        }
    }
}