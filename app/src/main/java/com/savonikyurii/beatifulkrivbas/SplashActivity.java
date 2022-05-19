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
//клас контроллер вікна заставки
public class SplashActivity extends AppCompatActivity {
    //оголошення змінних
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override//створення вікна
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //ініціалізація змінних
        mRefData = FirebaseDatabase.getInstance().getReference();
        //перевіряємо чи є підключення до мережі інтернет
        if (isOnline(this)){ // якщо так
            new Handler().postDelayed(new Runnable() { //відкриважємо вікно входу
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
                    finish();
                }
            },1000);
        }
    }
    //метод перевірки підключення до інтернету, якщо ні пропонуємо під'єднатися
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