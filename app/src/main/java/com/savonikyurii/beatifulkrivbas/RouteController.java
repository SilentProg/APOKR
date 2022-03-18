package com.savonikyurii.beatifulkrivbas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.PlaceDetectionClient;
import com.google.android.libraries.places.compat.Places;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentRouteControllerBinding;
import com.savonikyurii.beatifulkrivbas.helpers.CustomInfoWindowGoogleMap;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.PolylineData;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
import com.savonikyurii.beatifulkrivbas.helpers.loclistener.LocListenerInterface;
import com.savonikyurii.beatifulkrivbas.helpers.loclistener.MyLocListener;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteController extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnPolylineClickListener, LocListenerInterface {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 45;
    private GoogleMap mMap;
    private FragmentRouteControllerBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private Place current;
    private List<Marker> list_markers;
    private List<Place> list_places;
    private Polyline currentPolyline;
    private GeoApiContext mGeoApiContext = null;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    private LocationManager locationManager;
    private MyLocListener locListener;

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private boolean mLocationPermissionsGranted;
    private Location currentLocation = null;
    private Duration currentDuration;
    private boolean allRoute = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRouteControllerBinding.inflate(inflater, container, false);

        init();
        getLocationPermission();

        return binding.getRoot();
    }

    private void sort_points() {
        /*mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Route.getRoute().size()>0) Route.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Route.addPlace(ds.getValue(Place.class));
                }

                List<Place> sortedList = new ArrayList<>();

                LatLng current = new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude());

                List<Double> dfkhgl = new ArrayList<>();
                Double min = calculateDistance(current, new LatLng(Route.getRoute().get(0).getLatitude(),Route.getRoute().get(0).getLongtude()));
                for (int i = 0; i < Route.getRoute().size()-1; i++){
                   // for (int j = 0; j < Route.getRoute().size()-1; j++){
                        if (calculateDistance(current, new LatLng(Route.getRoute().get(i).getLatitude(),Route.getRoute().get(i).getLongtude()))<min){
                            min = calculateDistance(current, new LatLng(Route.getRoute().get(i).getLatitude(),Route.getRoute().get(i).getLongtude()));
                            Place temp = Route.getRoute().get(i);
                            Route.getRoute().set(i, Route.getRoute().get(i+1));
                            Route.getRoute().set(i+1, temp);
                            dfkhgl.add(calculateDistance(current, new LatLng(Route.getRoute().get(i).getLatitude(),Route.getRoute().get(i).getLongtude())));
                        }
                   // }
                }
                Log.d("DDDDD", min.toString());
                Log.d("DDDDD", dfkhgl.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void init() {
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ActivityRouteController.drawer = binding.routeDrawer;
        createNotificationChannel();
        init_gps();

        //change_current();


        list_markers = new ArrayList<>();
        list_places = new ArrayList<>();
        binding.navViewRoute.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnCloseDrawer:
                        binding.routeDrawer.closeDrawer(GravityCompat.END);
                        break;
                    case R.id.btnChange:
                        changeCurrentDestination();
                        break;
                    case R.id.btnSkip:
                        skipDestination();
                        break;
                    case R.id.btnSkipAndMarkForVisited:
                        skipDestinationAndMarkByVisited();
                        break;
                    case R.id.btnAllRoute:
                        allRouteVisible();
                        break;
                    case R.id.btnGoogleMap:
                        OpenGoogleMap();
                        break;
                    case R.id.btnEndRoute:
                        endRoute();
                        break;
                    case R.id.btnViewPoints:
                        NavHostFragment.findNavController(RouteController.this).navigate(R.id.nav_allPoints);
                        break;
                    case R.id.btnAddPoint:
                        NavHostFragment.findNavController(RouteController.this).navigate(R.id.nav_catalog_route);
                        break;
                    case R.id.btnDelPoint:
                        deletePoint();
                        break;
                }
                return true;
            }
        });

        init_listeners();

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyBR5UdpoDsXIU_jax39-Yo43qKVQ_XttHU")
                    .build();
        }
        sort_points();
    }

    private void deletePoint() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    arrayAdapter.add(ds.getValue(Place.class).getTitle());
                }
                if (arrayAdapter.isEmpty()){
                    builder
                            .setTitle(R.string.warning)
                            .setMessage(R.string.no_point_for_delete)
                            .setIcon(R.drawable.warning)
                            .setCancelable(true)
                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }else {
                    builder.setTitle(getActivity().getString(R.string.delete))
                            .setIcon(R.drawable.trashcan)
                            .setCancelable(true)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Place del = Route.getRoute().get(which);

                            final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setTitle(getActivity().getString(R.string.delete))
                                    .setIcon(R.drawable.trashcan)
                                    .setMessage(R.string.delete_ask)
                                    .setPositiveButton(R.string.yes, (dialog1, which1) -> {
                                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(del.getTitle()).removeValue();
                                        Route.removeByIndex(which);
                                    })
                                    .setNegativeButton(R.string.NO, (dialog1, which1) -> {
                                        dialog.cancel();
                                        dialog1.cancel();
                                    });
                            AlertDialog d = b.create();
                            d.show();
                        }
                    });
                }

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void endRoute() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.warning)
                .setMessage(R.string.ask_endr_route)
                .setCancelable(true)
                .setIcon(R.drawable.warning)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").removeValue();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Objects.requireNonNull(getActivity()).finish();
                    }
                })
                .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void allRouteVisible() {
        if (allRoute) {
            if (mPolyLinesData.size() > 0) {
                for (PolylineData polylineData : mPolyLinesData) {
                    polylineData.getPolyline().remove();
                }
                mPolyLinesData.clear();
                mPolyLinesData = new ArrayList<>();
            }
            try {
                calculateDirections(new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude()), new LatLng(Route.getRoute().get(0).getLatitude(), Route.getRoute().get(0).getLongtude()), false, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            for (int i = 0; i < Route.getRoute().size(); i++) {
                try {

                    calculateDirections(new LatLng(Route.getRoute().get(i).getLatitude(), Route.getRoute().get(i).getLongtude()), new LatLng(Route.getRoute().get(i + 1).getLatitude(), Route.getRoute().get(i + 1).getLongtude()), false, false);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            try {
                calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude()), false, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            allRoute = false;
            binding.navViewRoute.getMenu().findItem(R.id.btnAllRoute).setTitle(R.string.route_to_destination);
            binding.routeDrawer.closeDrawer(GravityCompat.END);
        } else {
            try {
                allRoute = true;
                binding.navViewRoute.getMenu().findItem(R.id.btnAllRoute).setTitle(R.string.all_route);
                calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude()), true, true);
                binding.routeDrawer.closeDrawer(GravityCompat.END);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void skipDestinationAndMarkByVisited() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Objects.requireNonNull(getActivity()).getString(R.string.ask_skip))
                .setCancelable(true)
                .setIcon(R.drawable.visited)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        if (Route.getRoute().size() > 0) {

                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {
                                        Place place = new Place();
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            place = ds.getValue(Place.class);
                                        }
                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(place.getTitle()).removeValue();
                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("other").child(place.getTitle()).setValue(place);
                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                                    } else {
                                        mRefData.child("userdata").child(mAuth.getUid()).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                                    }

                                    mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                                    mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getRoute().get(0).getTitle()).setValue(Route.getRoute().get(0));
                                    mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("allDestination").child(Route.getRoute().get(0).getTitle()).removeValue();
                                    Route.removeByIndex(0);
                                    mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                Place temp = data.getValue(Place.class);
                                                calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(temp.getLatitude(), temp.getLongtude()), true, true);
                                                binding.routeDrawer.closeDrawer(GravityCompat.END);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            updateCounter();
                        } else {
                            final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setTitle(getActivity().getString(R.string.warning))
                                    .setMessage(R.string.end_route_warning)
                                    .setIcon(R.drawable.warning)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChildren()) {
                                                        Place place = new Place();
                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                            place = ds.getValue(Place.class);
                                                        }
                                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(place.getTitle()).removeValue();
                                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("other").child(place.getTitle()).setValue(place);
                                                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                                                    } else {
                                                        mRefData.child("userdata").child(mAuth.getUid()).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                                                    }

                                                    mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                                    Objects.requireNonNull(getActivity()).finish();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            updateCounter();
                                        }
                                    })
                                    .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog a = b.create();
                            a.show();
                        }

                    }
                })
                .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void skipDestination() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Objects.requireNonNull(getActivity()).getString(R.string.ask_skip))
                .setCancelable(true)
                .setIcon(R.drawable.skip)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if (Route.getRoute().size() > 0) {
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getRoute().get(0).getTitle()).setValue(Route.getRoute().get(0));
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("allDestination").child(Route.getRoute().get(0).getTitle()).removeValue();
                            Route.removeByIndex(0);
                            mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        Place temp = data.getValue(Place.class);
                                        calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(temp.getLatitude(), temp.getLongtude()), true, true);
                                        binding.routeDrawer.closeDrawer(GravityCompat.END);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setTitle(getActivity().getString(R.string.warning))
                                    .setMessage(R.string.end_route_warning)
                                    .setIcon(R.drawable.warning)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                            Objects.requireNonNull(getActivity()).finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog a = b.create();
                            a.show();
                        }

                    }
                })
                .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void changeCurrentDestination() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    arrayAdapter.add(ds.getValue(Place.class).getTitle());
                }

                if(arrayAdapter.isEmpty()){
                    builder
                            .setTitle(R.string.warning)
                            .setMessage(R.string.no_points)
                            .setCancelable(true)
                            .setIcon(R.drawable.warning)
                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                }else {
                    builder
                        .setTitle(R.string.chopse_destination)
                        .setIcon(R.drawable.chose)
                        .setCancelable(true)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Place oldP = Route.getCurrentDestination();
                        Place newP = Route.getRoute().get(which);

                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").removeValue();
                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").child(newP.getTitle()).setValue(newP);

                        /*Snackbar.make(binding.getRoot(), Route.getRoute().get(which).getTitle(), BaseTransientBottomBar.LENGTH_SHORT).show();*/

                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(oldP.getTitle()).setValue(oldP);
                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(newP.getTitle()).removeValue();
                        Route.setCurrentDestination(newP);
                        calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(newP.getLatitude(), newP.getLongtude()), true, true);

                        allRoute = true;
                        binding.navViewRoute.getMenu().findItem(R.id.btnAllRoute).setTitle(R.string.all_route);

                        binding.routeDrawer.closeDrawer(GravityCompat.END);
                    }
                });
                }


                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init_gps() {
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocListener();
        locListener.setLocListenerInterface(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, locListener);
        } else {
            getLocationPermission();
        }
    }


    private void init_listeners() {
        binding.btnAboutCurrentRouteController.setOnClickListener(this::onBtnAboutClick);
        binding.btnOpenControlMenu.setOnClickListener(this::onBtnMenuClick);
        binding.btnAboutNext.setOnClickListener(this::onBtnAboutNextClick);
    }

    private void onBtnAboutNextClick(View view) {
        DetailsFragment.isVisible = false;
        DetailsFragment.place = Route.getRoute().get(0);
        NavHostFragment.findNavController(this).navigate(R.id.nav_details_route);
    }

    private void onBtnMenuClick(View view) {
        binding.routeDrawer.openDrawer(GravityCompat.END);
    }

    private void OpenGoogleMap() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.open_google_map)
                .setCancelable(true)
                .setIcon(R.drawable.ic_baseline_map)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        String latitude = String.valueOf(Route.getCurrentDestination().getLatitude());
                        String longitude = String.valueOf(Route.getCurrentDestination().getLongtude());
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        try {
                            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage());
                            Toast.makeText(getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void onBtnAboutClick(View view) {
        Log.d("Click", "CLick");
        DetailsFragment.place = Route.getCurrentDestination();
        DetailsFragment.isVisible = false;
        NavHostFragment.findNavController(this).navigate(R.id.nav_details_route);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        init_current();
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setOnPolylineClickListener(this);
            currentLocation = mMap.getMyLocation();
        }


        CustomInfoWindowGoogleMap infoWindowGoogleMap = new CustomInfoWindowGoogleMap(getActivity());
        mMap.setInfoWindowAdapter(infoWindowGoogleMap);

        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Place temp = ds.getValue(Place.class);
                        Route.setCurrentDestination(temp);
                        list_places.add(temp);
                        current = temp;
                    }
                    binding.placeTitleCurrentroutecontroller.setText(Route.getCurrentDestination().getTitle());
                    Picasso.get().load(Route.getCurrentDestination().getImageuri()).into(binding.imageRoutecontrollcurrent);
                    LatLng temp = new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude());
                    MarkerOptions marker = new MarkerOptions().position(temp);
                    Marker m = mMap.addMarker(marker);
                    m.setTag(Route.getCurrentDestination());
                    list_markers.add(0, m);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude()), 15));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude()), true, true);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapCurrentRoute);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void init_current() {

        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    if (Route.getRoute().size() > 0) Route.getRoute().clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Place temp = ds.getValue(Place.class);
                        Route.addPlace(temp);
                    }
                    init_markers();
                    try {
                        binding.textTitleNext.setText(Route.getRoute().get(0).getTitle());
                        Picasso.get().load(Route.getRoute().get(0).getImageuri()).placeholder(R.drawable.image_placeholder).into(binding.imgNext);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        binding.containerTop.setVisibility(View.GONE);
                    }
                } else {
                    binding.containerTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init_markers() {
        list_places.clear();
        list_places.add(current);
        list_places.addAll(Route.getRoute());
        for (Place place : Route.getRoute()) {
            LatLng temp = new LatLng(place.getLatitude(), place.getLongtude());
            MarkerOptions marker = new MarkerOptions().position(temp);
            Marker m = mMap.addMarker(marker);
            m.setTag(place);
            list_markers.add(m);
        }
    }

    private void calculateDirections(LatLng start, LatLng end, boolean alt, boolean isClear) {

        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                end.latitude,
                end.longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(alt);
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
                addPolylinesToMap(result, isClear);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result, boolean isClear) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if (isClear) {
                    if (mPolyLinesData.size() > 0) {
                        for (PolylineData polylineData : mPolyLinesData) {
                            polylineData.getPolyline().remove();
                        }
                        mPolyLinesData.clear();
                        mPolyLinesData = new ArrayList<>();
                    }
                }
                double duration = 999999999;
                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkgray));
                    polyline.setClickable(true);
                    mPolyLinesData.add(new PolylineData(polyline, route.legs[0]));

                    double tempDuration = route.legs[0].duration.inSeconds;
                    if (tempDuration < duration) {
                        duration = tempDuration;
                        onPolylineClick(polyline);
                    }
                }
            }
        });

    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        for (PolylineData polylineData : mPolyLinesData) {
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if (polyline.getId().equals(polylineData.getPolyline().getId())) {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.highlight));
                polylineData.getPolyline().setZIndex(1);

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng
                );
                currentDuration = polylineData.getLeg().duration;
                //Toast.makeText(getActivity(), currentDuration.toString(), Toast.LENGTH_SHORT).show();
                zoomRoute(polyline.getPoints());
            } else {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.darkgray));
                polylineData.getPolyline().setZIndex(0);
            }
        }

    }

    public static double calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return results[0];
    }

    private void updateCounter() {
        mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("userInfo").child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = snapshot.getValue(Integer.class);
                ++count;
                mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("userInfo").child("count").setValue(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String CHANNEL_ID = "channelID";
    private String CHANNEL_NAME = "channelNAME";
    private String CHANNEL_DESC = "channelDESC";
    private int NOTIFICATION_ID = 0;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESC;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onChangeLocation(Location loc) {
        currentLocation = loc;
        if (calculateDistance(new LatLng(loc.getLatitude(), loc.getLongitude()), new LatLng(Route.getCurrentDestination().getLatitude(), Route.getCurrentDestination().getLongtude())) <= 200) {
            updateCounter();

            //PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, getActivity().getIntent(), PendingIntent.FLAG_NO_CREATE);

            try {
                NotificationCompat.Builder builder =
                        null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify)
                            .setContentTitle(getActivity().getString(R.string.destination) + Route.getCurrentDestination().getTitle())
                            .setContentText(getString(R.string.destination_close))
                            //.addAction(R.drawable.notify, "lOx", pendingIntent)
                            .setAutoCancel(true)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(getActivity().getString(R.string.ask_about_current_point) + "\n" + Route.getCurrentDestination().getBigdescription()))
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                }


                Notification notification = builder.build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                notificationManager.notify(NOTIFICATION_ID, notification);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            if (Route.getRoute().size() > 0) {

                mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            Place place = new Place();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                place = ds.getValue(Place.class);
                            }
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(place.getTitle()).removeValue();
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("other").child(place.getTitle()).setValue(place);
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                        } else {
                            mRefData.child("userdata").child(mAuth.getUid()).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                        }

                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getRoute().get(0).getTitle()).setValue(Route.getRoute().get(0));
                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("allDestination").child(Route.getRoute().get(0).getTitle()).removeValue();
                        Route.removeByIndex(0);
                        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Place temp = data.getValue(Place.class);
                                    calculateDirections(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(temp.getLatitude(), temp.getLongtude()), true, true);
                                    binding.routeDrawer.closeDrawer(GravityCompat.END);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            Place place = new Place();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                place = ds.getValue(Place.class);
                            }
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(place.getTitle()).removeValue();
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("other").child(place.getTitle()).setValue(place);
                            mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                        } else {
                            mRefData.child("userdata").child(mAuth.getUid()).child("visited").child("last").child(Route.getCurrentDestination().getTitle()).setValue(Route.getCurrentDestination());
                        }

                        mRefData.child("userdata").child(Objects.requireNonNull(mAuth.getUid())).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
                        //startActivity(new Intent(getActivity(), MainActivity.class));
                        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER,false);
                        try {
                            Objects.requireNonNull(getActivity()).finish();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
}
