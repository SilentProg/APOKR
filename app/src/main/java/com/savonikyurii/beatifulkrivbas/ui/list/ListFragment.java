package com.savonikyurii.beatifulkrivbas.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.API.Place;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    public static Place place;
    private RecyclerView list;
    private List<Place> all_places;
    private DatabaseReference mRefData;
    private ListAllAdapter adapter;
    private CardView cardEmpty;
    private Button btnReturnToCatalog;
    public static String category = Categories.AllObjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        init(root);

        ReadData(category);

        return root;
    }


    private void init(View root){
        list = root.findViewById(R.id.list);
        btnReturnToCatalog = (Button)root.findViewById(R.id.btnReturnToCatalog);
        all_places = new ArrayList<>();
        cardEmpty = root.findViewById(R.id.card_list_empty);
        mRefData = FirebaseDatabase.getInstance().getReference();
        adapter = new ListAllAdapter(getActivity(), all_places, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnReturnToCatalog.setOnClickListener(view -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
        //all_places.add(new Place("Гданцевский парк", "Центрально-Міський район, Кривий Ріг, 50000", "https://99px.ru/sstorage/53/2015/11/tmb_150152_4865.jpg", Categories.CULTURAL_HERITAGE, "none", "none", 47.89788844983029, 33.33293223353166));
    }

    private void ReadData(String category){
        if (category.equals(Categories.AllObjects)) {
            ReadAll();
        }else if (category.equals(Categories.MilitaryPatriotic)){
            ReadAllCategory(Categories.MilitaryPatriotic);
        }else if (category.equals(Categories.CulturalObjects)){
            ReadAllCategory(Categories.CulturalObjects);
        }else if (category.equals(Categories.ReligiousObjects)){
            ReadAllCategory(Categories.ReligiousObjects);
        }else if (category.equals(Categories.HistoricalObjects)){
            ReadAllCategory(Categories.HistoricalObjects);
        }else if (category.equals(Categories.ArchitecturalObjects)){
            ReadAllCategory(Categories.ArchitecturalObjects);
        }else if (category.equals((Categories.NaturalObjects))){
            ReadAllCategory(Categories.NaturalObjects);
        }else if (category.equals(Categories.UrbanObjects)){
            ReadAllCategory(Categories.UrbanObjects);
        }else{
            ReadAllCategory(Categories.OtherObjects);
        }


    }

    private void ReadAllCategory(String category){
        mRefData.child("places").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (all_places.size() > 0) all_places.clear();
                if (!snapshot.hasChildren()) cardEmpty.setVisibility(View.VISIBLE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Place place = ds.getValue(Place.class);
                    assert place != null;
                    all_places.add(place);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ReadAll(){
        mRefData.child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (all_places.size() > 0) all_places.clear();
                if (!snapshot.hasChildren()) cardEmpty.setVisibility(View.VISIBLE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot data : ds.getChildren()) {
                        Place place = data.getValue(Place.class);
                        assert place != null;
                        all_places.add(place);
                        updateUI();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        adapter.notifyDataSetChanged();
    }


}