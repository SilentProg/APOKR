package com.savonikyurii.beatifulkrivbas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.os.Bundle;

import com.savonikyurii.beatifulkrivbas.databinding.ActivityRouteControllerBinding;
import com.savonikyurii.beatifulkrivbas.ui.BottomSheetRoute;
//клас контроллер вікна мандрівки
public class ActivityRouteController extends AppCompatActivity implements BottomSheetRoute.BottomSheetRouteListener {
    //оголошення полів
    private ActivityRouteControllerBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    public static DrawerLayout drawer;


    @Override // створення вікна
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRouteControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //ініціалізація змінних
        setSupportActionBar(binding.toolbarRouteController);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_route_controller, R.id.nav_details_route)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_route);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.toolbarRouteController, navController);
    }

    @Override // перенавантаження кнопки назад
    public void onBackPressed() {
        //якщо відкрите меню закриваємо його
        if (drawer.isDrawerOpen(GravityCompat.END)) drawer.closeDrawer(GravityCompat.END);
        else{
            //інакше якщо поточне вікно це контролер мандрівки то запитуємо чи бажаєте ви завершити мандрівку
            if (Navigation.findNavController(this, R.id.nav_host_fragment_route).getCurrentDestination().getId() == R.id.nav_route_controller){

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(getString(R.string.whole_route))
                        .setMessage(R.string.ask_pause_route)
                        .setIcon(R.drawable.warning)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            super.onBackPressed();
                            finish();
                        })
                        .setNegativeButton(R.string.NO, (dialog, which) -> {
                            dialog.cancel();
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }else{
                //інакше виконуємо super.onBackPressed();
                super.onBackPressed();
            }
        }
    }

    @Override // перенавантажуємо методи татискання на кнопи BottomSheetDialog
    public void onButtonClicked(int id) {
        switch (id){
            case R.id.btnBottomSheetStart:
                Navigation.findNavController(this, R.id.nav_host_fragment_route).popBackStack(R.id.nav_route_controller, true);
                Navigation.findNavController(this, R.id.nav_host_fragment_route).navigate(R.id.nav_route_controller);
                break;
            case R.id.btnBottomSheetWholeRoute:
                Navigation.findNavController(this, R.id.nav_host_fragment_route).navigate(R.id.nav_allPoints);
                break;
            case R.id.btnBottomSheetReturnToList:
                Navigation.findNavController(this,R.id.nav_host_fragment_route).navigateUp();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }
}