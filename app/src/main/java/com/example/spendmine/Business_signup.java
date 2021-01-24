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

public class Business_signup extends AppCompatActivity {

    EditText et_company, et_email, et_pass, et_contact, et_city;
    TextView login, signup;
    Button submit;
    String email,pass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);

        et_company = findViewById(R.id.company);
        et_email = findViewById(R.id.email);
        et_pass = findViewById(R.id.pass);
        et_contact = findViewById(R.id.contact);
        et_city = findViewById(R.id.city);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        submit = findViewById(R.id.btn_submit);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to login activity
                Intent i = new Intent(Business_signup.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to signup activity
                Intent i = new Intent(Business_signup.this, Signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                business_signup();
            }
        });
    }

    public void business_signup() {
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("business signup", "createUserWithEmail:success");
                            FirebaseUser company = mAuth.getCurrentUser();
                            addData();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("business signup failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Business_signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void addData() {
        Map<String, Object> company = new HashMap<>();
        company.put("email", email);
        company.put("password", pass);
        company.put("company", et_company.getText().toString());
        company.put("city", et_city.getText().toString());
        company.put("contact", et_contact.getText().toString());
        company.put("category","business");

        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.collection("business").document(currentUser.getUid())
                .set(company)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        //Go to login activity
                        Intent i = new Intent(Business_signup.this,Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

//        db.collection("business")
//                .add(company)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("Success", "DocumentSnapshot written with ID: " + documentReference.getId());
//                        Toast.makeText(getApplicationContext(),"Logging in",Toast.LENGTH_LONG).show();
//                        //Go to login activity
//                        Intent i = new Intent(Business_signup.this,Login.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Failed", "Error adding document", e);
//                    }
//                });
    }
}
