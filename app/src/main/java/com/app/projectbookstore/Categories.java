package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Categories extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView imgBook, imgUniforms, imgMerchendise;
    private TextView txtCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        imgBook = findViewById(R.id.books);
        imgUniforms = findViewById(R.id.uniforms);
        imgMerchendise = findViewById(R.id.others);

        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Categories.this, StudentHomeScreenActivity.class);
                intent.putExtra("category", "Books");
                startActivity(intent);
            }
        });
        imgUniforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Categories.this, StudentHomeScreenActivity.class);
                intent.putExtra("category", "Uniforms");
                startActivity(intent);
            }
        });


        imgMerchendise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Categories.this, StudentHomeScreenActivity.class);
                intent.putExtra("category", "Others");
                startActivity(intent);
            }
        });

        txtCurrentUser = findViewById(R.id.txtUser);
        String[] splitCurrentUser = Prevalent.currentOnlineUser.getUserFullName().split(" ");
        txtCurrentUser.setText(splitCurrentUser[0]);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        Intent intentHome = new Intent(Categories.this, Categories.class);
                        startActivity(intentHome);
                        finish();
                        break;

                    case R.id.menuCart:
                        Intent intentCart = new Intent(Categories.this, CartActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.menuProfile:
                        Intent intentProfile = new Intent(Categories.this, ProfileActivity.class);
                        startActivity(intentProfile);
                        break;

                    default:
                }
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}