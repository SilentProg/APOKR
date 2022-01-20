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
import com.savonikyurii.beatifulkrivbas.helpers.User;

public class HomeFragment extends Fragment {

    Button buttonMap;
    View root;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;
    private TextView user_login;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        buttonMap = (Button) root.findViewById(R.id.btnMap);
//        buttonMap.setOnClickListener(this::onClickBtnMap);
        ImageView userlogo = (ImageView)root.findViewById(R.id.image_currentplace);
        user_login = (TextView) root.findViewById(R.id.username);
        userlogo.setImageResource(R.drawable.logotemp);



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
                    user_login.setText(userdata.getUsername());
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        return root;
    }

    private void onClickBtnMap(View v){
        Snackbar.make(root, "Hello, snackbar", BaseTransientBottomBar.LENGTH_LONG).show();
    }


}