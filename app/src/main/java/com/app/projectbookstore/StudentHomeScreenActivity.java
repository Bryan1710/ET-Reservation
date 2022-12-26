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

public class StudentHomeScreenActivity extends AppCompatActivity implements RecyclerViewInterface {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private TextView txtCurrentUser;
    private BottomNavigationView bottomNavigationView;
    private ImageView imgMenu;
    ProductAdapter productAdapter;
    ArrayList<Products> productsArrayList, sortedProductsByPrice;
    private SearchView searchView;
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


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        imgBook = findViewById(R.id.books);
        imgUniforms = findViewById(R.id.uniforms);
        imgMerchendise = findViewById(R.id.others);

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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productsArrayList, this);
        recyclerView.setAdapter(productAdapter);


        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    productsArrayList.add(products);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        searchView = findViewById(R.id.student_search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    //search
        private void filterList(String text) {
            try {
                //sort the products by price
                //since binary search works best on sorted items
                Collections.sort(productsArrayList, Comparator.comparing(o -> Integer.valueOf(o.getProductPrice())));

                //calling filteredByPrice function
                //sets the returned arraylist to productAdapter
                productAdapter.setFilteredList(filteredByPrice(productsArrayList, text));

            } catch (NumberFormatException ex) {

                //naive algo
                ArrayList<Products> filteredList = new ArrayList<>();
                for (Products products : productsArrayList) {
                    if (products.getProductName().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(products);
                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(this, "Item does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    productAdapter.setFilteredList(filteredList);
                }
            }
        }

        private static int binarySearch(ArrayList<Products> arr, int l, int h, int x) {
            int mid = 0;
            mid = (l + h) /2;

            //while there are elements to check, proceed
            while(l <= h)
            {
                // cuts the arraylist to half and checks if the element at middle
                // is equal to the one we are looking
                if(Integer.parseInt(arr.get(mid).getProductPrice()) < x)
                {
                    //if it is less than the one we are looking
                    // look at the right of the array
                    l = mid + 1;
                }
                //check if the middle element is equal to the one we are looking for
                else if(Integer.parseInt(arr.get(mid).getProductPrice()) == x)
                {
                    // if yes, return its index
                    return mid;
                }
                //else then the check the left side
                else
                {
                    h = mid - 1;
                }
                mid = (l+h) / 2;
            }
            //return -1 if its not present in the arraylist
            return -1;
        }

        private ArrayList<Products> filteredByPrice(ArrayList<Products> arr, String text)
        {
            //initialize temporary list to hold all the products
            ArrayList<Products> newArray = new ArrayList<>();
            //this is necessary to not unintentionally delete items from the arraylist
            for(Products p : arr)
            {
                newArray.add(p);
            }

            //declare a new arraylist to be returned whenever the one we are looking for is present
            ArrayList<Products> toReturn = new ArrayList<>();

            // check first if the price we are looking for is present in the array
            //if not, then we are already done with time complexity of O(log n)
            int i = binarySearch(newArray, 0, newArray.size(), Integer.parseInt(text));

            //this will check if there are multiple entries of the same price
            while(i >= 0)
            {
                // if yes, then we will add the product to the arraylist we will return
                toReturn.add(newArray.get(i));
                // we will remove it from our temporary arraylist that holds all the products
                newArray.remove(i);
                // check again in the temporary arraylist if there are multiple entries of the price we are looking for
                // if no, then we are done with time complexity of O(log n)
                // if yes, we will proceed until we get none
                i = binarySearch(newArray, 0, newArray.size(), Integer.parseInt(text));
            }
            // return the list regardless if its empty or not
            return toReturn;
        }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(StudentHomeScreenActivity.this, ProductDetailsActivity.class);

        intent.putExtra("productName", productsArrayList.get(position).getProductName());
        intent.putExtra("productPrice", productsArrayList.get(position).getProductPrice());
        intent.putExtra("productCategory", productsArrayList.get(position).getProductCategory());
        intent.putExtra("productImage", productsArrayList.get(position).getProductImage());
        intent.putExtra("productID", productsArrayList.get(position).getProductID());
        startActivity(intent);
    }

        //@Override
        //public void onBackPressed() {
            // do nothing
        //}
}