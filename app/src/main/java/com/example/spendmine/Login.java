package com.example.spendmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    Button submit;
    EditText et_email, et_pass;
    String email,pass;
    TextView signup, business_signup;
    RadioGroup category;
    RadioButton category_selected;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef;
        if(currentUser != null){
//            Toast.makeText(getApplicationContext(),"Existing user",Toast.LENGTH_LONG).show();
            final String uid = currentUser.getUid();
            Log.d("Current user", uid);
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

                docRef = db.collection("users").document(uid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //student account exists
                                Toast.makeText(getApplicationContext(),"Student",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this,Dashboard.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                //Business account exists
                                Toast.makeText(getApplicationContext(),"Business",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this,Business_Dashboard.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(getApplicationContext(),"Login to continue",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.email);
        et_pass = findViewById(R.id.pass);
        signup = findViewById(R.id.signup);
        business_signup = findViewById(R.id.business_signup);
        submit = findViewById(R.id.btn_submit);
        category = findViewById(R.id.category);

        business_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to business signup activity
                Intent i = new Intent(Login.this,Business_signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to signup activity
                Intent i = new Intent(Login.this,Signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });



    }

    void login() {
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("signin", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            moveToDashboard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signin failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void moveToDashboard(){

        int selectedID = category.getCheckedRadioButtonId();
        category_selected = findViewById(selectedID);
        String cat = category_selected.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        switch (cat) {
            case "Student":
                db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                        Intent mainIntent = new Intent(Login.this, Dashboard.class);
                                        Login.this.startActivity(mainIntent);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_LONG).show();
                                    Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                break;
            case "Business":
                db.collection("business")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                        Intent mainIntent = new Intent(Login.this, Business_Dashboard.class);
                                        Login.this.startActivity(mainIntent);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_LONG).show();
                                    Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                break;
        }



    }


}
