package com.savonikyurii.beatifulkrivbas.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsBinding;
import com.savonikyurii.beatifulkrivbas.API.Place;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;
import com.savonikyurii.beatifulkrivbas.ui.map.MapFragment;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment{
    private FragmentDetailsBinding binding;
    public static Place place;
    private BottomSheetRoute bottomSheetRoute;
    private DatabaseReference mRedfData;
    private FirebaseAuth mAuth;
    public static boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        binding.detailsMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsImageFragment.place = place;
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.nav_imagedetails);
            }
        });

        init();

        return binding.getRoot();
    }

    private void init(){
        binding.btnViewOnMap.setOnClickListener(this::onClickMapView);
        binding.btnAddToRoute.setOnClickListener(this::onClickAddToRoute);

        initplace();

        mRedfData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initplace(){
        if (place==null) return;
        if (!isVisible) {
            binding.btnViewOnMap.setVisibility(View.GONE);
            binding.btnAddToRoute.setVisibility(View.GONE);
        }
        binding.textTitle.setText(place.getTitle());
        binding.textCategory.setText(place.getCategory());
        binding.textAddress.setText(place.getAdres());
        binding.textDescription.setText(place.getBigdescription());
        Picasso.get().load(place.getImageuri()).into(binding.detailsMainImage);
        Picasso.get().load(place.getImageuri()).into(binding.detailsBlurImage);
    }

    private void onClickMapView(View view){
        MapFragment.place = place;
        NavHostFragment.findNavController(this).navigate(R.id.nav_map);
    }

    private void onClickAddToRoute(View view){

        mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()){
                    mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").child(place.getTitle()).setValue(place);
                }else{
                    mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(place.getTitle()).setValue(place);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomSheetRoute = new BottomSheetRoute(place);
        bottomSheetRoute.show(getActivity().getSupportFragmentManager(), "bottomSheet");
    }
}