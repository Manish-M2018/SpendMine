package com.example.spendmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText et_email, et_pass, et_expenditure, et_city, et_university, et_frequency;
    Button submit;
    TextView login, business_signup;

    String email,pass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_email = findViewById(R.id.email);
        et_pass = findViewById(R.id.pass);
        et_expenditure = findViewById(R.id.expenditure);
        et_city = findViewById(R.id.city);
        et_university = findViewById(R.id.university);
        et_frequency = findViewById(R.id.frequency);
        business_signup = findViewById(R.id.business_signup);

        submit = findViewById(R.id.btn_submit);
        login = findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        business_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to business signup activity
                Intent i = new Intent(Signup.this,Business_signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to login activity
                Intent i = new Intent(Signup.this,Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    public void signup() {
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addData();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signup failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void addData() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", pass);
        user.put("expenditure", et_expenditure.getText().toString());
        user.put("city", et_city.getText().toString());
        user.put("university", et_university.getText().toString());
        user.put("frequency", et_frequency.getText().toString());
        user.put("category","student");

        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Success", "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(),"Logging in",Toast.LENGTH_LONG).show();
                        //Go to login activity
                        Intent i = new Intent(Signup.this,Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failed", "Error adding document", e);
                    }
                });
    }
}
