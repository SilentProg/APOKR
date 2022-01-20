package com.savonikyurii.beatifulkrivbas.ui.catalog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savonikyurii.beatifulkrivbas.R;


public class CatalogFragment extends Fragment {
    private CardView allPLaces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);
        allPLaces = root.findViewById(R.id.all);
        allPLaces.setOnClickListener(this::onClickAllPlaces);


        return root;
    }

    private void onClickAllPlaces(View view){
        //NavHostFragment.findNavController(this).navigate(R.id.nav_details);
        NavHostFragment.findNavController(this).navigate(R.id.nav_list);
    }

}