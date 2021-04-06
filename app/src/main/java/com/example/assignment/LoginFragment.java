package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {


    Button btn_login;
    TextView txt_newUser;
    public NavController navController;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String email,password;
    EditText edt_email,edt_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        txt_newUser=view.findViewById(R.id.txt_loginRegister);
        btn_login=view.findViewById(R.id.btn_login);
        edt_email=view.findViewById(R.id.edit_loginEmail);
        edt_pass=view.findViewById(R.id.edit_loginPass);
        btn_login.setOnClickListener(loginUser);
        txt_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment_start);
                navController.navigate(R.id.registerFragment);
            }
        });
    }

    View.OnClickListener loginUser=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            email=edt_email.getText().toString().trim();
            password=edt_pass.getText().toString().trim();

            if (!checkEmptyField()){
                Log.d("Login","l");
                AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Intent intent=new Intent(getActivity(),DashActivity.class);
                            startActivity(intent);
                        }else {
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                Toast.makeText(getActivity().getApplicationContext(),"User doesn't exist!",Toast.LENGTH_LONG).show();
                                edt_email.getText().clear();
                                edt_pass.getText().clear();
                                edt_email.setError("User doesn't exist!");
                                edt_email.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                // Toast.makeText(getActivity().getApplicationContext(),"Wrong Password!",Toast.LENGTH_LONG).show();
                                edt_email.getText().clear();
                                edt_pass.getText().clear();
                                edt_email.setError("Please enter valid login credentials");
                                edt_email.requestFocus();
                            }catch (Exception e ){
                                Toast.makeText(getActivity().getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                            }
                        }
                        }

                });
            }

        }
    };

    public boolean checkEmptyField() {

        if (TextUtils.isEmpty(email)) {
            edt_email.setError("Email can't be Empty!");
            edt_email.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(password)) {
            edt_email.setError(null);
            //edt_email.setErrorEnabled(false);
            edt_pass.setError("Password can't be Empty!");
            edt_pass.requestFocus();
            return true;
        }else if (password.length()<6){
            edt_pass.getText().clear();
            edt_pass.setError("Password can't be less than 6 characters");
            edt_pass.requestFocus();
            return true;
        }
        //edt_pass.setErrorEnabled(false);
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            Intent intent=new Intent(getActivity(),DashActivity.class);
            startActivity(intent);
        }
    }
}