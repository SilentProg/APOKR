package com.savonikyurii.beatifulkrivbas.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentHomeBinding;
import com.savonikyurii.beatifulkrivbas.helpers.User;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init(){
        mRefData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        binding.btnGoToCatalog.setOnClickListener(this::onClickGoToCatalog);

        initUser();
    }

    private void initUser(){
        mRefData.child("userdata").child(user.getUid()).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "NO DATA!", BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userdata = task.getResult().getValue(User.class);
                    binding.username.setText(userdata.getUsername());
                    binding.numberOfVisitedPlaces.setText(new StringBuilder().append(userdata.getCount()).toString());
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    private void onClickGoToCatalog(View view){
        NavHostFragment.findNavController(this).navigate(R.id.nav_catalog);
    }



}