package com.savonikyurii.beatifulkrivbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private Button btnLogin;
    private TextView singup;
    private TextView forgotpass;
    private EditText text_email;
    private EditText text_password;
    private String email;
    private String password;

    private FirebaseAuth mAuth;
    // ...
    // Initialize Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        mAuth = FirebaseAuth.getInstance();
        singup.setOnClickListener(view -> {
            startActivity(new Intent(ActivityLogin.this, ActivityRegistration.class));
        });

        forgotpass.setOnClickListener(view -> {
            startActivity(new Intent(ActivityLogin.this, PasswordRecovery.class));
        });

        btnLogin.setOnClickListener(this::onLoginClick);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
            }
        }
    }

    private void init(){
        singup = (TextView)findViewById(R.id.singup);
        forgotpass = (TextView)findViewById(R.id.forgotpass);
        btnLogin = (Button)findViewById(R.id.singin);
        text_email = (EditText)findViewById(R.id.text_email);
        text_password = (EditText)findViewById(R.id.text_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) { //перевірка на порожність
            //виведенння пвімлення
            //Snackbar.make(Login.this, view, "User not null", BaseTransientBottomBar.LENGTH_LONG).show();
            int index = currentUser.getEmail().toString().indexOf("@");//пошук номеру символу
            String admin = currentUser.getEmail().toString().substring(0, index); //обрізання рядка для отримання
            //створення переходу на нову активність
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            //додавання ектра даних для передачі на активність що запантажуемо
            /*intent.putExtra(Constant.ADMIN, admin);
            intent.putExtra(Constant.EMAIL, user.getEmail().toString());
            intent.putExtra(Constant.UID, user.getUid());*/
            //запуск вікна
            startActivity(intent);
            finish();
        }
    }

    private void onLoginClick(View view){
        FirebaseAuth.getInstance().signOut();

        email = text_email.getText().toString().trim();
        password = text_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            text_email.setError(getResources().getString(R.string.email_error));
            //Snackbar.make(ActivityLogin.this, view, getString(R.string.email_error), BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }else
        if (TextUtils.isEmpty(password)){
            text_email.setError(getResources().getString(R.string.password_error));
            //Snackbar.make(ActivityLogin.this, view, getString(R.string.password_error), BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                intent.putExtra("user", authResult.getUser().getDisplayName());
                intent.putExtra("email",email);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(ActivityLogin.this, view,getString(R.string.login_error), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }
}