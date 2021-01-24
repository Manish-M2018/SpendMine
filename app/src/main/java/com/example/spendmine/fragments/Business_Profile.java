package com.example.spendmine.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spendmine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Business_Profile extends Fragment {


    public Business_Profile() {
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
        return inflater.inflate(R.layout.fragment_business__profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final TextView tv_company, tv_city, tv_email, tv_contact;


        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        tv_company = getActivity().findViewById(R.id.tv_company);
        tv_city = getActivity().findViewById(R.id.tv_city);
        tv_email = getActivity().findViewById(R.id.tv_email);
        tv_contact = getActivity().findViewById(R.id.tv_contact);

        db.collection("business")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String company,city,email,contact;
                                company = tv_company.getText().toString() +": "+ document.getData().get("company").toString();
                                city = tv_city.getText().toString() +": "+document.getData().get("city").toString();
                                email = tv_email.getText().toString() +": "+document.getData().get("email").toString();
                                contact = tv_contact.getText().toString() +": "+document.getData().get("contact").toString();
                                tv_company.setText(company);
                                tv_city.setText(city);
                                tv_email.setText(email);
                                tv_contact.setText(contact);

                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
