package com.savonikyurii.beatifulkrivbas.ui.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;
import com.savonikyurii.beatifulkrivbas.ui.map.MapFragment;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment{
    private FragmentDetailsBinding binding;
    public static Place place;
    private BottomSheetRoute bottomSheetRoute;

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

        initplace();
        init();

        return binding.getRoot();
    }

    private void init(){
        binding.btnViewOnMap.setOnClickListener(this::onClickMapView);
        binding.btnAddToRoute.setOnClickListener(this::onClickAddToRoute);
    }

    private void initplace(){
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
        bottomSheetRoute = new BottomSheetRoute();
        bottomSheetRoute.show(getActivity().getSupportFragmentManager(), "bottomSheet");
    }
}