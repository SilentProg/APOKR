package com.savonikyurii.beatifulkrivbas.ui.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsImageBinding;
import com.savonikyurii.beatifulkrivbas.API.Place;
import com.squareup.picasso.Picasso;


public class DetailsImageFragment extends Fragment {

    public static Place place;
    private static FragmentDetailsImageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsImageBinding.inflate(inflater, container, false);

        initimage(binding);

        return binding.getRoot();
    }

    private void initimage(FragmentDetailsImageBinding binding){
        Picasso.get().load(place.getImageuri()).into(binding.imageMain);
        Picasso.get().load(place.getImageuri()).into(binding.imageBlur);
    }
}