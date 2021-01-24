package com.example.spendmine.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.spendmine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;


public class QR_scan extends Fragment {

    Button scan;
    TextView acknowledgement;
    String qrcode,company, fluff;
    private FirebaseAuth mAuth;

    int amount, avg;


    public QR_scan() {
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
        return inflater.inflate(R.layout.fragment_q_r_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scan = getActivity().findViewById(R.id.btn_scan);
        acknowledgement = getActivity().findViewById(R.id.acknowledgement);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forSupportFragment(QR_scan.this).initiateScan();

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        qrcode = result.getContents(); //Data got from the QR scan
        mAuth = FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("qr")
                .whereEqualTo("qr_data", qrcode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                amount = Integer.parseInt(document.getData().get("amount_spent").toString());
                                String display = "Successfully paid $"+ amount +" to "+ document.getData().get("company").toString();
                                Toast.makeText(getContext(),display,Toast.LENGTH_LONG).show();
                                acknowledgement.setText(display);
                                company = document.getData().get("company").toString();
                                recordScan();
                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void recordScan() {
        //Add the scan data to db
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> QR = new HashMap<>();

        QR.put("email", user.getEmail());
        QR.put("qr_data", qrcode);
        QR.put("company", company);
        QR.put("amount_spent", amount);

        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.collection("scanned_qrs").document(qrcode)
                .set(QR)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Scanned data recorded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });


        getFluffyCoinsCount();

    }

    public void getFluffyCoinsCount() {
        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fluff = document.getData().get("fluff").toString();
                                getAverage();
                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getAverage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Also get the avg amount from the company
        db.collection("business")
                .whereEqualTo("company", company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                avg = Integer.parseInt(document.getData().get("average").toString());
                                updateFluff();
                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void updateFluff() {
        Log.d("Fluffy function","goood");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int f = Integer.parseInt(fluff);
        //Formula to get the updated fluffy coins
        if(amount<avg) {
            Log.d("Yayy Fluffy",amount+" "+avg);
            //Get fluffy coins!
            //Max of 10 fluffy coins per purchase and the additions are only 2,5 or 10
            if(amount < (avg/2)) {
                f += 10;
            }else if(amount < (0.75 * avg)) {
                f += 5;
            } else {
                f += 2;
            }
        }
        else {
            //No fluffy coins :(
            Toast.makeText(getContext(),"You didn't get any fluffy coins :(",Toast.LENGTH_SHORT).show();
        }

        //Update the fluffy coins
        db.collection("users").document(user.getUid()).update("fluff",String.valueOf(f));
    }
}
