package com.savonikyurii.beatifulkrivbas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.DataBaseHelper;
import com.savonikyurii.beatifulkrivbas.helpers.Place;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRefData = FirebaseDatabase.getInstance().getReference();

        //DataBaseHelper.addNewPlace(new Place("gavno", "Кривий Ріг, Дніпропетровська область, 500000", "https://lh5.googleusercontent.com/p/AF1QipMArM0dHsE-7ynZUPhy6boe8ChSooitTquNO33E=w427-h240-k-no", Categories.CULTURAL_HERITAGE, "none", "none", 47.98853918814209, 33.463203869511624));
        DataBaseHelper.addNewPlace(new Place("Гданцевский парк", "Центрально-Міський район, Кривий Ріг, 50000", "https://99px.ru/sstorage/53/2015/11/tmb_150152_4865.jpg", Categories.NaturalObjects, "none", "none", 47.89788844983029, 33.33293223353166));
        DataBaseHelper.addNewPlace(new Place("Східний водоспад", "Карачуны, 10, Кривий Ріг, Дніпропетровська область, 50000", "https://lh5.googleusercontent.com/p/AF1QipPRxgMuDxzmErlywfoRJIamaUkc-GVHttbieEcy=w408-h306-k-no", Categories.NaturalObjects, "none", "none", 47.906243711493445, 33.283804226854514));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ActivityLogin.class));
                finish();
            }
        },1000);
        //FirebaseAuth.getInstance().signOut();
    }
}