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
import com.savonikyurii.beatifulkrivbas.databinding.FragmentCatalogBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.ui.list.ListFragment;


public class CatalogFragment extends Fragment {
    private FragmentCatalogBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCatalogBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init(){
        binding.all.setOnClickListener(this::onClickAllPlaces);
        binding.cardMilitaryPatriotic.setOnClickListener(this::onMilitaryPatrioticClick);
        binding.cardCulturalObjects.setOnClickListener(this::onCulturalClick);
        binding.cardReligiousObjects.setOnClickListener(this::onReligiousClick);
        binding.cardHistoricalObjects.setOnClickListener(this::onHistoricalClick);
        binding.cardArchitecturalObjects.setOnClickListener(this::onArchitecturalClick);
        binding.cardNaturalObjects.setOnClickListener(this::onNaturalClick);
        binding.cardUrbanObjects.setOnClickListener(this::onUrbanClick);
        binding.cardOther.setOnClickListener(this::onOtherClick);
    }

    private void onClickAllPlaces(View view){
        ListFragment.category = Categories.AllObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onMilitaryPatrioticClick(View view){
        ListFragment.category = Categories.MilitaryPatriotic;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onCulturalClick(View view){
        ListFragment.category = Categories.CulturalObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onReligiousClick(View view){
        ListFragment.category = Categories.ReligiousObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }
    private void onHistoricalClick(View view) {
        ListFragment.category = Categories.HistoricalObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }
    private void onArchitecturalClick(View view) {
        ListFragment.category = Categories.ArchitecturalObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onOtherClick(View view) {
        ListFragment.category = Categories.OtherObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onUrbanClick(View view) {
        ListFragment.category = Categories.UrbanObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

    private void onNaturalClick(View view) {
        ListFragment.category = Categories.NaturalObjects;
        try {
            NavHostFragment.findNavController(this).navigate(R.id.nav_list);
        }catch (Exception e){
            NavHostFragment.findNavController(this).navigate(R.id.nav_list_route);
        }
    }

}