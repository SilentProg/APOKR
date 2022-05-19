package com.savonikyurii.beatifulkrivbas.ui.catalog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentCatalogBinding;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AdditionalClasses.Categories;
import com.savonikyurii.beatifulkrivbas.ui.list.ListFragment;

//клас контролер каталогу
public class CatalogFragment extends Fragment {
    //оголошення полів
    private FragmentCatalogBinding binding;

    @Override //метод створення
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //створення binding змінної
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        //виклик методу ініціалізації
        init();
        //повертаємо інтерфейс
        return binding.getRoot();
    }
    //метод інітіалізації
    private void init(){
        //встановлення обробників натискання на категорії
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
    /*методи натискання на категорії*/
    private void onClickAllPlaces(View view){
        ListFragment.category = Categories.AllObjects;
        //переходимо на вікно зі списком
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