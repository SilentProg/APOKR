package com.savonikyurii.beatifulkrivbas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentSendMailBinding;

import java.util.Objects;
import java.util.Random;

//клас контролер вікна зворотнього зв'зку
public class SendMailFragment extends Fragment {
    //оголошення змінних
    private FragmentSendMailBinding binding;

    @Override //створення вікна
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ініціалізація змінних
        binding = FragmentSendMailBinding.inflate(inflater, container, false);
        DatabaseReference mRefData = FirebaseDatabase.getInstance().getReference();
        //відправляємо лист
        mRefData.child("feedback").child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = snapshot.getValue(Integer.class);
                count++;
                binding.textMailTo.setText("savoniksteam@ukr.net");
                binding.textMailFrom.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                int finalCount = count;
                binding.btnSEND.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL , new String[]{Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Відгук №"+ finalCount);
                    i.putExtra(Intent.EXTRA_TEXT, binding.mailBody.getText().toString());
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), "No email client configured. Please check.", Toast.LENGTH_SHORT).show();
                    }
                });
                mRefData.child("feedback").child("count").setValue(count);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}