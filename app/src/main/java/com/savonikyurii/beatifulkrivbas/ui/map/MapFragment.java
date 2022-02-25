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

import com.google.android.gms.maps.CameraUpdate;
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
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.CustomInfoWindowGoogleMap;
import com.savonikyurii.beatifulkrivbas.helpers.InfoWindowData;
import com.savonikyurii.beatifulkrivbas.helpers.Place;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback{


    private static final String TAG = "Map";
    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private DatabaseReference mRefData;
    public static List<Place> allMarkers;
    public static Place place;
    private Polyline currentPolyline;
    private GeoApiContext mGeoApiContext = null;

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
        CustomInfoWindowGoogleMap infoWindowGoogleMap = new CustomInfoWindowGoogleMap(getActivity());
        mMap.setInfoWindowAdapter(infoWindowGoogleMap);
        //mMap.setMyLocationEnabled(true);
        //new FetchURL(getActivity()).execute(getUrl(new LatLng(48.014027997319744, 33.484797886699965), new LatLng(47.89591530085526, 33.33274639636037), "driving"), "driving");
       // new FetchURL(getActivity()).execute(getUrl(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), new LatLng(47.89591530085526, 33.33274639636037), "driving"), "driving");

        calculateDirections(new LatLng(47.999628693296565, 33.44792163717202),new LatLng(47.89591530085526, 33.33274639636037));
    }


    private void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mRefData = FirebaseDatabase.getInstance().getReference();

        mapFragment.getMapAsync(this);

        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBR5UdpoDsXIU_jax39-Yo43qKVQ_XttHU")
                    .build();
        }
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

    private void calculateDirections(LatLng start, LatLng end){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                end.latitude,
                end.longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        start.latitude,
                        start.longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkgray));
                    polyline.setClickable(true);

                }
            }
        });
    }

    private void updateUI(List<Place> list){
        mMap.clear();
        for (Place place: list) {
            LatLng temp = new LatLng(place.getLatitude(), place.getLongtude());
            MarkerOptions marker = new MarkerOptions().position(temp);
            Marker m = mMap.addMarker(marker);
            m.setTag(new InfoWindowData(place.getTitle(),place.getImageuri(), place.getCategory()));

        }
    }
}