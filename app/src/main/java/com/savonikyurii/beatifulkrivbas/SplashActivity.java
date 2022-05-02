package com.savonikyurii.beatifulkrivbas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRefData = FirebaseDatabase.getInstance().getReference();

        if (isOnline(this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
                    finish();
                }
            },1000);
        }


        //FirebaseAuth.getInstance().signOut();

        //.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("other").child("Test").removeValue();
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("last").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("route").child("currentDestination").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
        //mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("other").child("test").setValue(new Place("test", "test", "https://ichef.bbci.co.uk/news/640/cpsprodpb/15B7B/production/_106555988_kryviy_rig_landscape.jpg","test", 5 , "test", 5.0,5.0));
    }

    public boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setMessage(this.getResources().getString(R.string.NetworkError));
            dialog.setNeutralButton(this.getResources().getString(R.string.wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent( Settings.ACTION_WIFI_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                    finish();
                }
            });

            dialog.setPositiveButton(this.getResources().getString(R.string.network), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                    finish();
                }
            });

            dialog.setNegativeButton(getBaseContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            AlertDialog d = dialog.create();
            d.show();
        }
        return false;
    }
}