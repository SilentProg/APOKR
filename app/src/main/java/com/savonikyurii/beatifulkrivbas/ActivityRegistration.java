package com.savonikyurii.beatifulkrivbas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.API.User;

public class ActivityRegistration extends AppCompatActivity {

    private Button btnReg;
    private DatabaseReference mRefData;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userdata;
    private TextView editLogin, editPassword, editConfirm_password, editEmail;
    private String login, email, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void init(){
        btnReg = (Button) findViewById(R.id.btnSingUP);
        editLogin = (TextView) findViewById(R.id.textreg_login);
        editEmail = (TextView) findViewById(R.id.textreg_email);
        editPassword = (TextView) findViewById(R.id.textreg_pass);
        editConfirm_password = (TextView) findViewById(R.id.textreg_confirmpass);

        mAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(this::register);
    }

    private void register(View view){

        login = editLogin.getText().toString();
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        confirm_password = editConfirm_password.getText().toString();

        if(TextUtils.isEmpty(login)){
            editLogin.setError(getString(R.string.not_login));
            return;
        }

        if(TextUtils.isEmpty(email)){
            editEmail.setError(getString(R.string.not_email));
            return;
        }

        if(TextUtils.isEmpty(password)){
            editPassword.setError(getString(R.string.not_password));
            return;
        }

        if(TextUtils.isEmpty(confirm_password)){
            editConfirm_password.setError(getString(R.string.not_confirmpassword));
            return;
        }

        if(!password.equals(confirm_password)){
            editPassword.setError(getString(R.string.password_mismatch));
            editConfirm_password.setError(getString(R.string.password_mismatch));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userdata = new User();
                            userdata.setEmail(email);
                            userdata.setUsername(login);
                            mRefData = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = mAuth.getCurrentUser();
                            mRefData.child("userdata").child(user.getUid()).child("userInfo").setValue(userdata);
                            Snackbar.make(editConfirm_password.getRootView(), "Registration successful!", BaseTransientBottomBar.LENGTH_LONG).show();
                            updateUI();
                        } else {

                            Toast.makeText(ActivityRegistration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(ActivityRegistration.this, ActivityLogin.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }
}