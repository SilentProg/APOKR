package com.savonikyurii.beatifulkrivbas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//клас контролер вікна про розрорбника
public class AboutFragment extends Fragment {

    @Override//ствроненн вікна
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        //встановлення обробника натискання на посиляння на GitHub
        root.findViewById(R.id.btnGitBub2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //відкриваємо посилання в браузері
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SilentProg")));
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}