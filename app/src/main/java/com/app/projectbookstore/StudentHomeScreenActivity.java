package com.app.projectbookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.app.projectbookstore.Adapter.ProductAdapter;
import com.app.projectbookstore.Interface.RecyclerViewInterface;
import com.app.projectbookstore.Models.Products;
import com.app.projectbookstore.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StudentHomeScreenActivity extends AppCompatActivity {

    private TextView txtCurrentUser;
    private BottomNavigationView bottomNavigationView;
    private ImageView imgMenu;
    private ImageView imgBook, imgUniforms, imgMerchendise;

    //about us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);

        imgMenu = findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentHomeScreenActivity.this, AboutUsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBook = findViewById(R.id.books_user);
        imgUniforms = findViewById(R.id.uniforms_user);
        imgMerchendise = findViewById(R.id.others_user);

        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(StudentHomeScreenActivity.this, UserCategories.class);
                intent.putExtra("category", "Books");
                startActivity(intent);
            }
        });
        imgUniforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(StudentHomeScreenActivity.this, UserCategories.class);
                intent.putExtra("category", "Uniforms");
                startActivity(intent);
            }
        });


        imgMerchendise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(StudentHomeScreenActivity.this, UserCategories.class);
                intent.putExtra("category", "Others");
                startActivity(intent);
            }
        });

        txtCurrentUser = findViewById(R.id.txtUser);
        String[] splitCurrentUser = Prevalent.currentOnlineUser.getUserFullName().split(" ");
        txtCurrentUser.setText(splitCurrentUser[0]);


        bottomNavigationView = findViewById(R.id.bottomNavView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        Intent intentHome = new Intent(StudentHomeScreenActivity.this, StudentHomeScreenActivity.class);
                        startActivity(intentHome);
                        finish();
                        break;

                    case R.id.menuCart:
                        Intent intentCart = new Intent(StudentHomeScreenActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.menuProfile:
                        Intent intentProfile = new Intent(StudentHomeScreenActivity.this, ProfileActivity.class);
                        startActivity(intentProfile);
                        break;

                    default:
                }
                return true;
            }
        });
    }
}