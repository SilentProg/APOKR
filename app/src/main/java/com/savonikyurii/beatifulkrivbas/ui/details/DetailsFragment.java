package com.savonikyurii.beatifulkrivbas.ui.details;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onClickAddToRoute(View view){
        Route.addPlace(place);
        bottomSheetRoute = new BottomSheetRoute(place);
        bottomSheetRoute.show(getActivity().getSupportFragmentManager(), "bottomSheet");

//        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BotomSheetDialogTheme);
//        View d = LayoutInflater.from(getActivity().getApplicationContext())
//                .inflate(
//                        R.layout.bottomsheet_route,
//                        (ConstraintLayout)binding.getRoot().findViewById(R.id.bottomSheetContainer)
//                );
//
//        d.findViewById(R.id.bottomSheetContainer).setBackgroundResource(backgroundId());
//        d.findViewById(R.id.btnBottomSheetStart).setOnClickListener(view1 -> {
//            Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        d.findViewById(R.id.btnBottomSheetWholeRoute).setOnClickListener(view1 -> {
//            Toast.makeText(getActivity(), "Whole route", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        d.findViewById(R.id.btnBottomSheetReturnToList).setOnClickListener(view1 -> {
//            NavHostFragment.findNavController(this).navigateUp();
//            dialog.dismiss();
//        });
//
//        dialog.setContentView(d);
//        dialog.show();
    }
    private int backgroundId(){
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO : return R.drawable.bottomsheetbackground;
            case Configuration.UI_MODE_NIGHT_YES: return R.drawable.bottomsheetbackgrounddark;
                default: return R.drawable.bottomsheetbackgrounddark;
        }
    }
}