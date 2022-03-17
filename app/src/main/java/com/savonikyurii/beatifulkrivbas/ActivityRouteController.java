package com.savonikyurii.beatifulkrivbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.databinding.ActivityRouteControllerBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
import com.savonikyurii.beatifulkrivbas.helpers.loclistener.LocListenerInterface;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.savonikyurii.beatifulkrivbas.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ActivityRouteController extends AppCompatActivity implements BottomSheetRoute.BottomSheetRouteListener {
    private ActivityRouteControllerBinding binding;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRouteControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarRouteController);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_route_controller, R.id.nav_details_route)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_route);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.toolbarRouteController, navController);
    }

    @Override
    public void onButtonClicked(int id) {
        switch (id){
            case R.id.btnBottomSheetStart:
                Navigation.findNavController(this, R.id.nav_host_fragment_route).popBackStack(R.id.nav_route_controller, true);
                Navigation.findNavController(this, R.id.nav_host_fragment_route).navigate(R.id.nav_route_controller);

                break;
            case R.id.btnBottomSheetWholeRoute:
                Navigation.findNavController(this, R.id.nav_host_fragment_route).navigate(R.id.nav_allPoints);
                break;
            case R.id.btnBottomSheetReturnToList:
                Navigation.findNavController(this,R.id.nav_host_fragment_route).navigateUp();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }
}