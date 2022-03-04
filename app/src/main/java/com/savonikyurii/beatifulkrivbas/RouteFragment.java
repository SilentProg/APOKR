package com.savonikyurii.beatifulkrivbas;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.savonikyurii.beatifulkrivbas.databinding.FragmentRouteBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
import com.savonikyurii.beatifulkrivbas.ui.list.ListAllAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RouteFragment extends Fragment {
    private FragmentRouteBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private ListAllAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRouteBinding.inflate(inflater, container, false);

        init();



        return binding.getRoot();
    }

    private void init() {
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        initRoute();

        binding.btnWholerouteNext.setOnClickListener(this::onNextDestinationClick);
    }


    private void initRoute(){
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Route.setCurrentDestination(ds.getValue(Place.class));
                    }
                    binding.textWholerouteTitle.setText(Route.getCurrentDestination().getTitle());
                    Picasso.get().load(Route.getCurrentDestination().getImageuri()).into(binding.imageRouteCurrent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    if (Route.getRoute().size()>0) Route.getRoute().clear();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Route.addPlace(ds.getValue(Place.class));
                    }
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ListAllAdapter(getActivity(), Route.getRoute(),this);
        binding.routelist.setAdapter(adapter);
        binding.routelist.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUi(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();


        //Toast.makeText(getActivity(), String.valueOf(Route.getRoute().size()), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();
        if (adapter.getItemCount()>0) binding.routelist.setVisibility(View.VISIBLE);
        else binding.routelist.setVisibility(View.GONE);

    }

    private void initCurrentDestination(Place newCurrentDestination){
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").child(Route.getCurrentDestination().getTitle()).removeValue();
        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").child(newCurrentDestination.getTitle()).setValue(newCurrentDestination);
//        mRefData.child("userdata").child(mAuth.getUid()).child("route").child("currentDestination").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChildren()){
//                    for (DataSnapshot ds: snapshot.getChildren()) {
//                        Route.setCurrentDestination(ds.getValue(Place.class));
//                    }
//                    binding.textWholerouteTitle.setText(Route.getCurrentDestination().getTitle());
//                    Picasso.get().load(Route.getCurrentDestination().getImageuri()).into(binding.imageRouteCurrent);
//                    updateUi();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        initRoute();
    }

    private void onNextDestinationClick(View view){
        if (Route.getRoute().size()>0){
            initCurrentDestination(Route.getRoute().get(0));
            mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").child(Route.getRoute().get(0).getTitle()).removeValue();
            if (Route.getRoute().size()==1){
                Route.clear();
                adapter.notifyDataSetChanged();
                binding.routelist.setVisibility(View.GONE);
            }
            mRefData.child("userdata").child(mAuth.getUid()).child("route").child("allDestination").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        if (Route.getRoute().size()>0) Route.clear();
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            Route.addPlace(ds.getValue(Place.class));
                        }
                        updateUi();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}