package com.example.spendmine.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.spendmine.Business_signup;
import com.example.spendmine.Login;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Display_QR extends Fragment {

    Button generate;
    ImageView QR_code;
    private FirebaseAuth mAuth;
    String email, city, company, contact;
    String data_qr;


    public Display_QR() {
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
        return inflater.inflate(R.layout.fragment_display__q_r, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generate = getActivity().findViewById(R.id.btn_generate);
        QR_code = getActivity().findViewById(R.id.qr_code);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Date currentTime = Calendar.getInstance().getTime();
                data_qr = currentUser.getUid() + currentTime;
                getData(); //Fetch the company details from firestore

                //Display the QR code on the app
                QRGEncoder qrgEncoder = new QRGEncoder(data_qr, null, QRGContents.Type.TEXT,500);
                Bitmap qrBits = qrgEncoder.getBitmap();
                QR_code.setImageBitmap(qrBits);
            }
        });
    }


    public void getData() {
        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("business")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                email = document.getData().get("email").toString();
                                Toast.makeText(getContext(),email,Toast.LENGTH_LONG).show();
                                city = document.getData().get("city").toString();
                                company = document.getData().get("company").toString();
                                contact = document.getData().get("contact").toString();
                                addData(); //Add that data along with QR data to the qr collection in firestore
                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void addData() {
        Map<String, Object> QR = new HashMap<>();

        QR.put("email", email);
        QR.put("company", company);
        QR.put("city", city);
        QR.put("contact", contact);
        QR.put("qr_data", data_qr);
        QR.put("category", "business");

        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.collection("qr").document(data_qr)
                .set(QR)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "QR Stored!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
