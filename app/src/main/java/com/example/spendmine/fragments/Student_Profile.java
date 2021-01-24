package com.example.spendmine.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spendmine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Student_Profile extends Fragment {

    public Student_Profile() {
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
        return inflater.inflate(R.layout.fragment_student__profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final TextView tv_university, tv_city, tv_email, tv_expenditure, tv_frequency;


        // Access a Cloud Firestore instance from Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        tv_university = getActivity().findViewById(R.id.tv_university);
        tv_city = getActivity().findViewById(R.id.tv_city);
        tv_email = getActivity().findViewById(R.id.tv_email);
        tv_expenditure = getActivity().findViewById(R.id.tv_expenditure);
        tv_frequency = getActivity().findViewById(R.id.tv_frequency);

        db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String university,city,email,expenditure, frequency;
                                university = tv_university.getText().toString() +": "+ document.getData().get("university").toString();
                                city = tv_city.getText().toString() +": "+document.getData().get("city").toString();
                                email = tv_email.getText().toString() +": "+document.getData().get("email").toString();
                                expenditure = tv_expenditure.getText().toString() +": "+document.getData().get("expenditure").toString();
                                frequency = tv_expenditure.getText().toString() +": "+document.getData().get("frequency").toString();
                                tv_university.setText(university);
                                tv_city.setText(city);
                                tv_email.setText(email);
                                tv_expenditure.setText(expenditure);
                                tv_frequency.setText(frequency);

                            }
                        } else {
                            Log.d("Tag!!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
