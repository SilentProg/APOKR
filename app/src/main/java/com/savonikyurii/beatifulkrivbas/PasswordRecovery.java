package com.savonikyurii.beatifulkrivbas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordRecovery extends AppCompatActivity {
    private static boolean hide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);


        Button btnSend = (Button)findViewById(R.id.btnSend);
        TextInputLayout code = (TextInputLayout)findViewById(R.id.editCode);
        if(hide){
            btnSend.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
        }


        Button btnRestore = (Button)findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener(view -> {
            if(hide){
                hide = false;
                btnSend.setVisibility(View.VISIBLE);
                code.setVisibility(View.VISIBLE);
            }else{
                hide = true;
                btnSend.setVisibility(View.GONE);
                code.setVisibility(View.GONE);
            }

        });
    }
}