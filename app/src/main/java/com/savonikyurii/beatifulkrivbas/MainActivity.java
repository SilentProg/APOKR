package com.savonikyurii.beatifulkrivbas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.helpers.User;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements BottomSheetRoute.BottomSheetRouteListener{


    private AppBarConfiguration mAppBarConfiguration;
    private static long back_pressed;
    private Button btnLogout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView user_login;
    private TextView user_email;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;
    private final int CODE_LOCATION = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_catalog, R.id.nav_history, R.id.nav_map)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_catalog: Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_catalog); drawer.closeDrawer(GravityCompat.START); break;
                    case R.id.nav_home: Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_home); drawer.closeDrawer(GravityCompat.START); break;
                    case R.id.nav_history: Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_history); drawer.closeDrawer(GravityCompat.START); break;
                    case R.id.nav_map: Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_map); drawer.closeDrawer(GravityCompat.START); break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, CODE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_LOCATION){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Snackbar.make(btnLogout.getRootView(), "Деякі функції обмежено!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void init(){
        btnLogout = (Button) findViewById(R.id.btnLogout);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        user_login = (TextView) navHeader.findViewById(R.id.user_login);
        user_email = (TextView) navHeader.findViewById(R.id.user_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mRefData = FirebaseDatabase.getInstance().getReference();

        mRefData.child("userdata").child(user.getUid()).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Snackbar.make(user_login.getRootView(), "NO DATA!", BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userdata = task.getResult().getValue(User.class);
                    user_login.setText(getString(R.string.hello)+", "+userdata.getUsername()+"!");
                    user_email.setText(userdata.getEmail());
                    Snackbar.make(user_login.getRootView(), getString(R.string.hello)+", "+userdata.getUsername()+"!", BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        btnLogout.setOnClickListener(this::onBtnLogoutClick);
    }

    private void onBtnLogoutClick(View view){
        showBeautifulDialog(getString(R.string.exit),getString(R.string.logout_message));
    }

    private void showBeautifulDialog(String title, String description){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);  // заголовок
        builder.setMessage(description); // сообщение
        builder.setIcon(R.drawable.ic_logout);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Logout();
            }
        });
        builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setCancelable(true);
        AlertDialog lg_dialog = builder.create();
        lg_dialog.show();
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut(); //Отримання екземплеру адміністратору
        //створення новох активності
        Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
        startActivity(intent); //виклик вікна
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.nav_list ||
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.nav_imagedetails ||
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.nav_details ||
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.nav_catalog ||
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() == R.id.nav_map
        ){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                return;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else{
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                this.moveTaskToBack(true);
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onButtonClicked(int id) {
        switch (id){
            case R.id.btnBottomSheetStart:
                startActivity(new Intent(this, ActivityRouteController.class));
                break;
            case R.id.btnBottomSheetWholeRoute:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_route);
                break;
            case R.id.btnBottomSheetReturnToList:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigateUp();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }
}