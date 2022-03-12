package com.savonikyurii.beatifulkrivbas.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.User;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;
    private Place currentDestination;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init(){
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initUser();
        initRoute();
        initListeners();
    }

    private void initListeners(){
        binding.btnGoToCatalog.setOnClickListener(this::onClickGoToCatalog);
        binding.btnAllPlaceInCurrentRoute.setOnClickListener(this::onBtnWholeRouteClick);
        binding.btnAboutDestination.setOnClickListener(this::onBtnAboutDestinationClick);
        binding.btnOpenRouteController.setOnClickListener(this::onBtnOpenRouteController);
    }

    private void onBtnOpenRouteController(View view) {
        startActivity(new Intent(getActivity(), ActivityRouteController.class));
    }

    private void initUser(){
        mRefData.child("userdata").child(user.getUid()).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "NO DATA!", BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userdata = task.getResult().getValue(User.class);
                    binding.username.setText(userdata.getUsername());
                    binding.numberOfVisitedPlaces.setText(new StringBuilder().append(userdata.getCount()).toString());
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    private void initRoute(){
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    binding.cardNothinghere.setVisibility(View.GONE);
                    binding.cardCurrentroute.setVisibility(View.VISIBLE);
                    for (DataSnapshot  ds: snapshot.getChildren()) {
                        Place temp = ds.getValue(Place.class);

                        Picasso.get().load(temp.getImageuri()).into(binding.imageCurrentplace);
                        TextView title = binding.getRoot().findViewById(R.id.place_title_current);
                        TextView category = binding.getRoot().findViewById(R.id.place_category_current);
                        title.setText(temp.getTitle());
                        category.setText(temp.getCategory());
                        currentDestination = temp;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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



}