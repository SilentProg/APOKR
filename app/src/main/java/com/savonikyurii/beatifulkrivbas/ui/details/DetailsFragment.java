package com.savonikyurii.beatifulkrivbas.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentDetailsBinding;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;
import com.savonikyurii.beatifulkrivbas.ui.map.MapFragment;
import com.squareup.picasso.Picasso;
//клас контролер вікна деталі
public class DetailsFragment extends Fragment{
    //оголошення полів класу
    private FragmentDetailsBinding binding;
    public static Place place;
    private BottomSheetRoute bottomSheetRoute;
    private DatabaseReference mRedfData;
    private FirebaseAuth mAuth;
    public static boolean isVisible;

    @Override//метод створення інтерфейсу
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //створення binding змінної
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        //встановлення обробника натискання на зображення
        binding.detailsMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsImageFragment.place = place;
                //перехід на нове вікно
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.nav_imagedetails);
            }
        });
        //виклик інітіалізації
        init();
        //повернення інтерфейсу
        return binding.getRoot();
    }
    //метод інітіалізації
    private void init(){
        //встановлення обробників натискання на кнопки
        binding.btnViewOnMap.setOnClickListener(this::onClickMapView);
        binding.btnAddToRoute.setOnClickListener(this::onClickAddToRoute);
        //виклик функції initplace
        initplace();
        //інітіалізація змінних
        mRedfData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    //метод інітіалізації місця
    private void initplace(){
        //якщо місце пусто, то припиняємо виконання метода
        if (place==null) return;
        if (!isVisible) {
            binding.btnViewOnMap.setVisibility(View.GONE);
            binding.btnAddToRoute.setVisibility(View.GONE);
        }
        //виводимо інформації про місце
        binding.textTitle.setText(place.getTitle());
        binding.textCategory.setText(place.getCategory());
        binding.textAddress.setText(place.getAdres());
        binding.textDescription.setText(place.getBigdescription());
        Picasso.get().load(place.getImageuri()).into(binding.detailsMainImage);
        Picasso.get().load(place.getImageuri()).into(binding.detailsBlurImage);
    }
    //метод при натисанні на кнопку: переглянути на мапі
    private void onClickMapView(View view){
        //переходимо на вікно з мапою
        MapFragment.place = place;
        NavHostFragment.findNavController(this).navigate(R.id.nav_map);
    }
    //метод натискання на кнопку: додати в мандрівку
    private void onClickAddToRoute(View view){
        //додаємо місце в мандрівку
        mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //якщо це перше місце то записуємо його як поточну ціль
                if (!snapshot.hasChildren()){
                    mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").child(place.getTitle()).setValue(place);
                }else{ //інакше записуємо до списку місць мандрівки
                    mRedfData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(place.getTitle()).setValue(place);
                }}
            @Override public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show(); } //припомилці виводимо помилку
        });
        bottomSheetRoute = new BottomSheetRoute(place); //відображаємо діалог
        bottomSheetRoute.show(getActivity().getSupportFragmentManager(), "bottomSheet");
    }}