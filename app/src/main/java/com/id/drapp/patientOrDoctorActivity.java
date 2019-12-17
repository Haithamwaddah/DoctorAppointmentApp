package com.id.drapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class patientOrDoctorActivity extends AppCompatActivity {

    private Button areYouDoctor;
    private Button areYouPatient;
//    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_or_doctor);
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        Hospital h = new Hospital(1,"ABC");
//        mDatabaseReference.child("hospitals").child("1").setValue(h);

        if(doctorPreference.getBooleanFromSP(this)){
            if(doctorPreference.getUsernameFromSP(this) != null){
                Intent intent = new Intent(this, patientsListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else if (doctorPreference.getPhoneNumberFromSP(this) != null){
                Intent intent = new Intent(this, hospitalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }

        areYouDoctor = findViewById(R.id.areYouDoctor);
        areYouPatient = findViewById(R.id.areYouPatient);

        areYouDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientOrDoctorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        areYouPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientOrDoctorActivity.this, verifyPatient.class);
                startActivity(intent);
            }
        });
    }
}
//class Hospital {
//
//    public int no;
//    public String name;
//
//    public Hospital() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }
//
//    public Hospital(int no, String name) {
//        this.no = no;
//        this.name = name;
//    }
//}
