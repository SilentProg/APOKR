package com.savonikyurii.beatifulkrivbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.DataBaseHelper;
import com.savonikyurii.beatifulkrivbas.helpers.Place;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRefData = FirebaseDatabase.getInstance().getReference();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
                finish();
            }
        },1000);
        //FirebaseAuth.getInstance().signOut();

        //.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("other").child("Test").removeValue();
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("last").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("route").child("currentDestination").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("other").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
    }
}