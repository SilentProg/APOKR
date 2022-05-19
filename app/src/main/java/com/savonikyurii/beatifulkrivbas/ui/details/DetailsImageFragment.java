package com.savonikyurii.beatifulkrivbas.ui.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsImageBinding;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.squareup.picasso.Picasso;

//клас перегляду зображення
public class DetailsImageFragment extends Fragment {
    //оголошення полів
    public static Place place;
    private FragmentDetailsImageBinding binding;

    @Override // створення вью
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //створення binding змінної
        binding = FragmentDetailsImageBinding.inflate(inflater, container, false);
        //відображаємо зображення
        initimage(binding);
        //повертаємо вью
        return binding.getRoot();
    }
    //метож завантаження зображення
    private void initimage(FragmentDetailsImageBinding binding){
        Picasso.get().load(place.getImageuri()).into(binding.imageMain);
        Picasso.get().load(place.getImageuri()).into(binding.imageBlur);
    }
}