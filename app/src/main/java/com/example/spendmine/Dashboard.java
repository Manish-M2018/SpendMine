package com.example.spendmine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.spendmine.fragments.Business_Profile;
import com.example.spendmine.fragments.Fluffy_coins;
import com.example.spendmine.fragments.Leaderboard;
import com.example.spendmine.fragments.QR_scan;
import com.example.spendmine.fragments.Student_Profile;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        Fragment fragment = null;
        switch (item.getItemId()) {

            case R.id.profile:
                fragment = new Student_Profile();
                break;

            case R.id.scan:
                fragment = new QR_scan();
                break;

            case R.id.coins:
                fragment = new Fluffy_coins();
                break;

            case R.id.leaderboard:
                fragment = new Leaderboard();
                break;

            case R.id.logout:
                final Dialog myDialog;
                myDialog = new Dialog(this);
                myDialog.setContentView(R.layout.logout_popup);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                myDialog.show();
                Button yes = myDialog.findViewById(R.id.btn_yes);
                Button cancel = myDialog.findViewById(R.id.btn_cancel);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(Dashboard.this,Login.class));
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
