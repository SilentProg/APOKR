package com.savonikyurii.beatifulkrivbas.ui.home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.ActivityRouteController;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentHomeBinding;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.User;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;
//клас контролер головного вікна
public class HomeFragment extends Fragment {
    //оголошення полів
    private FragmentHomeBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;
    private Place currentDestination;
    //створення вью
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //виклик метода інітіалізації
        init();
        //повернення вью
        return binding.getRoot();
    }
    //метод інітіалізації
    private void init(){
        //інітіалізація змінних
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //виклик методів
        initUser();
        initRoute();
        initListeners();
    }
    //метод інітіалізації обробників подій
    private void initListeners(){
        binding.btnGoToCatalog.setOnClickListener(this::onClickGoToCatalog);
        binding.btnAllPlaceInCurrentRoute.setOnClickListener(this::onBtnWholeRouteClick);
        binding.btnAboutDestination.setOnClickListener(this::onBtnAboutDestinationClick);
        binding.btnOpenRouteController.setOnClickListener(this::onBtnOpenRouteController);
    }
    //обробник натискання на кнопку переходу на вікно мандрівки
    private void onBtnOpenRouteController(View view) {
        //якщо gps ввімкнений то переходимо на головне вікно контролю мандрівкою
        if (GPSChecker()) startActivity(new Intent(getActivity(), ActivityRouteController.class));
    }
    //інітіалізація користувача
    private void initUser(){
        //отримуємо дані з джерела про поточного користувача
        mRefData.child("userdata").child(user.getUid()).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //якщо отримання не успішне повідомляємо про це користувача
                if (!task.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "NO DATA!", BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else { //інакше виводимо інформацію про користувача на свої місця
                    userdata = task.getResult().getValue(User.class);
                    binding.username.setText(userdata.getUsername());
                    binding.numberOfVisitedPlaces.setText(new StringBuilder().append(userdata.getCount()).toString());
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
    //метод інітіалізації мандрівки
    private void initRoute(){
        //отримуємо дані мандрівки з джерела
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //якщо мандрівка існує відображаємо користувачеві
                if (snapshot.hasChildren()) {
                    binding.cardNothinghere.setVisibility(View.GONE);
                    binding.cardCurrentroute.setVisibility(View.VISIBLE);
                    for (DataSnapshot  ds: snapshot.getChildren()) {
                        //отримані дані переваодимо в потрібний нам формат
                        Place temp = ds.getValue(Place.class);
                        //виводимо на екран
                        Picasso.get().load(temp.getImageuri()).placeholder(R.drawable.image_placeholder).into(binding.imageCurrentplace);
                        TextView title = binding.getRoot().findViewById(R.id.place_title_current);
                        TextView category = binding.getRoot().findViewById(R.id.place_category_current);
                        title.setText(temp.getTitle());
                        category.setText(temp.getCategory());
                        currentDestination = temp;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    /*Методи обробки натискання на кнопки*/
    private void onClickGoToCatalog(View view){
        NavHostFragment.findNavController(this).navigate(R.id.nav_catalog);
    }

    private void onBtnWholeRouteClick(View view){
        NavHostFragment.findNavController(this).navigate(R.id.nav_route);
    }

    private void onBtnAboutDestinationClick(View view){
        DetailsFragment.place = currentDestination;
        NavHostFragment.findNavController(this).navigate(R.id.nav_details);
    }
    //метод перевірки ввімнутого gps, якщо він вивкнений пропонуємо ввімкнути
    private boolean GPSChecker(){
        boolean result = false;
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            AlertDialog d = dialog.create();
            d.show();
        }else{
            result = true;
        }
        return result;
    }

}