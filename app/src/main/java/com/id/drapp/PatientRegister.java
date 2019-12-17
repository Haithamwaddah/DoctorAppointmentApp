package com.id.drapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.id.drapp.models.doctorInfo;
import com.id.drapp.models.patientsInfo;

public class PatientRegister extends AppCompatActivity {

    private Button createAccountButton;
    private EditText firstName;
    private EditText lastName;
    private EditText userPhone;
    private EditText userEmail;
    private EditText passwordField;
    private EditText confirmPasswordField;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private ProgressDialog progressDialog;

    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        setTitle("");
        getSupportActionBar().setElevation(0);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        createAccountButton = findViewById(R.id.createAccountButton);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPatientAccount();
            }
        });
    }

    public void createPatientAccount(){

        final String firstname = firstName.getText().toString().trim();
        final String lastname = lastName.getText().toString().trim();
        final String userphone = userPhone.getText().toString().trim();
        final String useremail = userEmail.getText().toString().trim();
        final String password = passwordField.getText().toString().trim();
        final String confirmpassword = passwordField.getText().toString().trim();

        if(TextUtils.isEmpty(firstname)){
            firstName.setError("Cannot be Empty");
        }else {
            if(TextUtils.isEmpty(lastname)){
                lastName.setError("Cannot be Empty");
            }else {
                if(TextUtils.isEmpty(userphone)){
                    userPhone.setError("Cannot be Empty");
                }else {
                    if(TextUtils.isEmpty(useremail)){
                        userEmail.setError("Cannot be Empty");
                    }else {
                        if(TextUtils.isEmpty(password)){
                            passwordField.setError("Cannot be Empty");
                        }else {
                            if(password.length() < 6){
                                passwordField.setError("Password Should be Greater than 6");
                            }else {
                                if(TextUtils.isEmpty(confirmpassword)){
                                    confirmPasswordField.setError("Cannot be Empty");
                                }else {
                                    if(passwordField.getText().toString().equals(confirmPasswordField.getText().toString())){
                                        final ContentValues cv = new ContentValues();
                                        cv.put(doctorContract.patientEntry.COLUMN_NAME, firstname.concat("@@@@").concat(lastname));
                                        cv.put(doctorContract.patientEntry.COLUMN_PHONE_NUMBER, userphone);
                                        cv.put(doctorContract.patientEntry.COLUMN_EMAIL, useremail);
//                                        cv.put(doctorContract.patientEntry.COLUMN_PASSWORD, password);

                                        final Uri[] uri = new Uri[1];

                                        firebaseAuth.createUserWithEmailAndPassword(useremail, password)
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {

                                                        mDatabaseReference = mFirebaseDatabase.getReference().push();
                                                        final String pushId = mDatabaseReference.getKey();

                                                        cv.put(doctorContract.doctorEntry.COLUMN_PUSHID, pushId);

                                                        Toast.makeText(PatientRegister.this, "Firebase Sucess", Toast.LENGTH_SHORT).show();
                                                        uri[0] = getContentResolver().insert(doctorContract.doctorEntry.CONTENT_URI, cv);
//                                                        createUserInLocalDb(uri[0]);


                                                        mDatabaseReference = mDatabaseReference.child(charUtility.filterString(useremail)).child("patientInfo");
//                                                        mDatabaseReference.setValue(new patientsInfo(firstname, lastname, userphone, useremail, false, pushId, null));
                                                        progressDialog.dismiss();

                                                        createUserInFirebase(useremail);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        String message = e.getMessage();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PatientRegister.this, message, Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                });
                                    }else {
                                        confirmPasswordField.setError("Password doesn't match");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createUserInFirebase(String useremail) {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
        Toast.makeText(this, "Email Verification Link has been Sent to: " + useremail, Toast.LENGTH_LONG).show();
    }
}
