package com.savonikyurii.beatifulkrivbas.ui.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentHistoryBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.ui.list.ListAllAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    private List<Place> all_places;
    private Place place;
    DatabaseReference mRefData;
    ListHistoryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init(){
        mRefData = FirebaseDatabase.getInstance().getReference();
        all_places = new ArrayList<>();

        adapter = new ListHistoryAdapter(getActivity(), all_places,this);
        binding.listHistory.setAdapter(adapter);
        binding.listHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("visited").child("last").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    place = data.getValue(Place.class);
                    binding.placeTitleLast.setText(place.getTitle());
                    Picasso.get().load(place.getImageuri()).into(binding.imageLastplace);
                }
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

        mRefData.child("userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("visited").child("other").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    all_places.add(data.getValue(Place.class));
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(){
        adapter.notifyDataSetChanged();
    }
}