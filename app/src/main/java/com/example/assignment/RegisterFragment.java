package com.example.assignment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class RegisterFragment extends Fragment {

    Button btn_register;
    TextView txt_oldUser;
    EditText edt_name,edt_email,edt_pass,edt_cpass,edt_city,edt_date;
    RadioGroup select_gender;
    public NavController navController;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String name,email,pass,cPass,gender,city,date;
    final Calendar myCalendar = Calendar.getInstance();


    public RegisterFragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        txt_oldUser=view.findViewById(R.id.txt_login);
        edt_name=view.findViewById(R.id.edit_registerName);
        edt_email=view.findViewById(R.id.edit_registerEmail);
        edt_pass=view.findViewById(R.id.edit_registerPass);
        edt_cpass=view.findViewById(R.id.edit_registerCPass);
        edt_city=view.findViewById(R.id.edit_city);
        edt_date=view.findViewById(R.id.edit_bdate);
        setCalenderDatePicker(view);
        btn_register=view.findViewById(R.id.btn_register);
        select_gender=view.findViewById(R.id.radio_gender);
        btn_register.setOnClickListener(registerUser);
        txt_oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment_start);
                navController.navigate(R.id.loginFragment);
            }
        });
    }

    private void setCalenderDatePicker(View view) {

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEditText();
            }

        };

        edt_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void updateLabelEditText() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date=sdf.format(myCalendar.getTime()).toString().trim();
        edt_date.setText(sdf.format(myCalendar.getTime()));
    }

    View.OnClickListener registerUser=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name=edt_name.getText().toString().trim();
            email=edt_email.getText().toString().trim();
            pass=edt_pass.getText().toString().trim();
            cPass=edt_cpass.getText().toString().trim();
            city=edt_city.getText().toString().trim();

            int selectedRadio=select_gender.getCheckedRadioButtonId();
            if (selectedRadio==R.id.radio_male){
                gender="Male";
            }else{
                gender="Female";
            }

            final Map<String,Object> usermap=new HashMap<>();
            usermap.put("Name",name);
            usermap.put("Email",email);
            usermap.put("Gender",gender);
            usermap.put("City",city);
            usermap.put("BirthDate",date);
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        db.collection("Users").document(user.getUid()).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getApplicationContext(), "Successfully Registered!", Toast.LENGTH_LONG).show();
                                NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment_start);
                                navController.navigate(R.id.loginFragment);
                            }
                        });
                    }
                }
            });

        }
    };
}