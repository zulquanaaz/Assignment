package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser curUser;
    TextView txt_name,txt_email,txt_date,txt_gender,txt_city;
    Button btn_logout;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        txt_name=view.findViewById(R.id.txt_name);
        txt_email=view.findViewById(R.id.txt_email);
        txt_gender=view.findViewById(R.id.txt_gender);
        txt_date=view.findViewById(R.id.txt_bdate);
        txt_city=view.findViewById(R.id.txt_city);
        btn_logout=view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent=new Intent(getContext().getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        loadUserData();
    }

    private void loadUserData() {
        curUser=auth.getCurrentUser();
        Toast.makeText(getActivity().getApplicationContext(), "Id="+curUser.getEmail(), Toast.LENGTH_SHORT).show();
        db.collection("Users").document(curUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                txt_name.setText(documentSnapshot.getString("Name"));
                txt_email.setText(documentSnapshot.getString("Email"));
                txt_gender.setText(documentSnapshot.getString("Gender"));
                txt_city.setText(documentSnapshot.getString("City"));
                txt_date.setText(documentSnapshot.getString("BirthDate"));
            }
        });

    }
}