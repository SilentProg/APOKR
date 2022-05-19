package com.savonikyurii.beatifulkrivbas.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentHistoryBinding;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//клас контролер вікна історія перегляду
public class HistoryFragment extends Fragment {
    //оголошення полів
    FragmentHistoryBinding binding;
    private List<Place> all_places;
    private Place place;
    DatabaseReference mRefData;
    ListHistoryAdapter adapter;
    //метод створення вьб
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        //виклик інітіалізації
        init();
        //повернення вью
        return binding.getRoot();
    }
    //метод інітіалізації
    private void init(){
        //інітіалізація змінних
        mRefData = FirebaseDatabase.getInstance().getReference();
        all_places = new ArrayList<>();
        //створення та встановлення адаптера
        adapter = new ListHistoryAdapter(getActivity(), all_places,this);
        binding.listHistory.setAdapter(adapter);
        binding.listHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        //встановлення обробника натискання на кнопку: Деталі про останнє відвідане
        binding.btnDetailsLastDestinationDestination.setOnClickListener(this::onAboutLastDestination);
        //отримання останнього відвіданого місця
        mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("visited").child("last").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    place = data.getValue(Place.class);
                    binding.placeTitleLast.setText(place.getTitle());
                    Picasso.get().load(place.getImageuri()).into(binding.imageLastplace);
                }
                //якщо історія пуста повідомляємо про це
                if (place==null){
                    binding.cardNothinghere.setVisibility(View.VISIBLE);
                    binding.lastplaceContainer.setVisibility(View.GONE);
                    binding.listHistory.setVisibility(View.GONE);
                }else{
                    binding.cardNothinghere.setVisibility(View.GONE);
                    binding.lastplaceContainer.setVisibility(View.VISIBLE);
                    binding.listHistory.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //отримання іншилх місць історії
        mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("visited").child("other").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (all_places.size()>0) all_places.clear();
                for (DataSnapshot data: snapshot.getChildren()) {
                    all_places.add(data.getValue(Place.class));
                }
                //оновлення вью(інтерфейсу)
                updateUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    //метод натискання на кнопку про останнє відвідане
    private void onAboutLastDestination(View view) {
        //ховаємо кнопки
        DetailsFragment.isVisible = false;
        DetailsFragment.place = place;
        //переходимо до нового вікна
        NavHostFragment.findNavController(this).navigate(R.id.nav_details);
    }
    //оновлюємо список
    private void updateUI(){
        adapter.notifyDataSetChanged();
    }
}