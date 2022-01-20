package com.savonikyurii.beatifulkrivbas.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.DataBaseHelper;
import com.savonikyurii.beatifulkrivbas.helpers.Place;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView list;
    private List<Place> all_places;
    private List<Place> list_of_cultural;
    private List<Place> list_of_quarry;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListAllAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        init(root);
        ReadData();
        //all_places = DataBaseHelper.getAllFromQuarry();
        //updateUI();


        return root;
    }

    private void init(View root){
        list = root.findViewById(R.id.list);
        all_places = new ArrayList<>();
        list_of_cultural = new ArrayList<>();
        list_of_quarry = new ArrayList<>();
        mRefData = FirebaseDatabase.getInstance().getReference();
        adapter = new ListAllAdapter(getActivity(), all_places);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //all_places.add(new Place("Гданцевский парк", "Центрально-Міський район, Кривий Ріг, 50000", "https://99px.ru/sstorage/53/2015/11/tmb_150152_4865.jpg", Categories.CULTURAL_HERITAGE, "none", "none", 47.89788844983029, 33.33293223353166));
    }

    private void ReadData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DataBaseHelper helper = new DataBaseHelper(ref);
//        ref.child("places").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list_of_quarry.addAll(helper.getAllFromQuarry());
//                list_of_cultural.addAll(helper.getAllFromCultural());
//                System.out.println("size list of quarry: "+list_of_quarry.size());
//                System.out.println("size list of cultural: "+list_of_cultural.size());
//                updateUI();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        mRefData.child("places").child(Categories.QUARRY).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list_of_quarry.size()>0)list_of_quarry.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    Place place = ds.getValue(Place.class);
                    assert place != null;
                    list_of_quarry.add(place);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRefData.child("places").child(Categories.CULTURAL_HERITAGE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list_of_cultural.size()>0)list_of_cultural.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    Place place = ds.getValue(Place.class);
                    assert place != null;
                    list_of_cultural.add(place);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI() {
        all_places.clear();
        all_places.addAll(list_of_cultural);
        all_places.addAll(list_of_quarry);
        adapter.notifyDataSetChanged();
    }

}