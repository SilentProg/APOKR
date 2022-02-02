package com.savonikyurii.beatifulkrivbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.savonikyurii.beatifulkrivbas.databinding.ActivityPasswordRecoveryBinding;

import java.util.Objects;

public class PasswordRecovery extends AppCompatActivity {
    private ActivityPasswordRecoveryBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordRecoveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();

        binding.btnRestore.setOnClickListener(this::onRestoreClick);
    }

    private void onRestoreClick(View view){
        mAuth.sendPasswordResetEmail(Objects.requireNonNull(binding.editEmail.getEditText()).getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    AlertDialog dialog = builder(PasswordRecovery.this).create();
                    dialog.show();
                }else{
                    Snackbar.make(binding.imageView2.getRootView(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private AlertDialog.Builder builder(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Password reset!")
                .setMessage(getString(R.string.password_reset_label))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        return builder;
    }
}