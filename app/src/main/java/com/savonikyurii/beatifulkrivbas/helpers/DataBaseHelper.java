package com.savonikyurii.beatifulkrivbas.helpers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataBaseHelper {

    public static DatabaseReference mRefData;

    {
        mRefData = FirebaseDatabase.getInstance().getReference();
    }

    public DataBaseHelper(DatabaseReference mRefData) {
        this.mRefData = mRefData;
    }


    public static List<Place> ReadAllPlaces(){

        return null;
    }


    public static boolean addNewPlace(Place newPlace){

        mRefData = FirebaseDatabase.getInstance().getReference();
        mRefData.child("places").child(newPlace.getCategory()).child(newPlace.getTitle()).setValue(newPlace);

        return true;
    }

    public List<Place> firstRead(){
        List<Place> list = new ArrayList<>();
        mRefData = FirebaseDatabase.getInstance().getReference();
        mRefData.child("places").child(Categories.CULTURAL_HERITAGE).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for (DataSnapshot ds:task.getResult().getChildren()) {
                        Place place = ds.getValue(Place.class);
                        assert place != null;
                        list.add(place);
                    }
                }else {
                    System.out.println("Task Failed!");
                }
            }
        });
        mRefData.child("places").child(Categories.QUARRY).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for (DataSnapshot ds:task.getResult().getChildren()) {
                        Place place = ds.getValue(Place.class);
                        assert place != null;
                        list.add(place);
                    }
                }else {
                    System.out.println("Task Failed!");
                }
            }
        });

        return list;
    }

    public List<Place> getAllFromQuarry(){
        mRefData = FirebaseDatabase.getInstance().getReference();
        mRefData.child("places").child(Categories.QUARRY).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Places.quarry.size()>0)Places.quarry.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    Place place = ds.getValue(Place.class);
                    assert place != null;
                    Places.quarry.add(place);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Places.quarry;
    }

    public List<Place> getAllFromCultural(){
        List<Place> list_of_cultural = new ArrayList<>();
        mRefData = FirebaseDatabase.getInstance().getReference();
        mRefData.child("places").child(Categories.CULTURAL_HERITAGE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list_of_cultural.size()>0)list_of_cultural.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    Place place = ds.getValue(Place.class);
                    assert place != null;
                    list_of_cultural.add(place);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return list_of_cultural;
    }
}
