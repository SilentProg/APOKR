package com.savonikyurii.beatifulkrivbas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentRouteControllerBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RouteController extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FragmentRouteControllerBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private Place current;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRouteControllerBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

      @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void init(){
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        init_current();
        init_listeners();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapCurrentRoute);
        mapFragment.getMapAsync(this);
    }

    private void init_listeners(){
        binding.btnAboutCurrentRouteController.setOnClickListener(this::onBtnAboutClick);
    }

    private void onBtnAboutClick(View view) {
        Log.d("Click", "CLick");
        DetailsFragment.place = Route.getCurrentDestination();
        NavHostFragment.findNavController(this).navigate(R.id.nav_details_route);
    }

    private void init_current(){
        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Route.setCurrentDestination(ds.getValue(Place.class));
                    }
                    binding.placeTitleCurrentroutecontroller.setText(Route.getCurrentDestination().getTitle());
                    Picasso.get().load(Route.getCurrentDestination().getImageuri()).into(binding.imageRoutecontrollcurrent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}