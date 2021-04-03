package com.example.assignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class RegisterFragment extends Fragment {

    Button btn_register;
    TextView txt_oldUser;
    public NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_oldUser=view.findViewById(R.id.txt_login);
        txt_oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment_start);
                navController.navigate(R.id.loginFragment);
            }
        });
    }
}