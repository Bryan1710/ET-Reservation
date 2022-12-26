package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Models.Users;
import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText fStudentID, fNewPassword, fConfirmPassword;
    private Button btnConfirm;
    private TextView backToLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fStudentID = findViewById(R.id.forgotStudentID);
        fNewPassword = findViewById(R.id.etNewPassword);
        fConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnConfirm = findViewById(R.id.btnConfirm);
        progressDialog = new ProgressDialog(this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(fStudentID.getText().toString().isEmpty())
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            //check if the student id is registered
                            if (dataSnapshot.child("Users").child(fStudentID.getText().toString()).exists())
                            {
                                // check if the length of the password is equal to or more than 8
                                if(fNewPassword.getText().toString().length() >= 8)
                                {
                                    //check if new password and confirm password match
                                    if(fNewPassword.getText().toString().equals(fConfirmPassword.getText().toString()))
                                    {
                                        //update the user password in the database
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                        HashMap<String, Object> userMap = new HashMap<>();
                                        userMap. put("userPassword", fNewPassword.getText().toString());
                                        ref.child(fStudentID.getText().toString()).updateChildren(userMap);
                                        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                                        Toast.makeText(ForgotPasswordActivity.this, "Profile Information was Update Successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(ForgotPasswordActivity.this, "Password don't match", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                Toast.makeText(ForgotPasswordActivity.this, "Account does not exist. Please try again.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        backToLogin = findViewById(R.id.txtBackToLogin);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}