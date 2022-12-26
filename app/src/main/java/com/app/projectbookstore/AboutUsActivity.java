package com.app.projectbookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutUsActivity.this, StudentHomeScreenActivity.class);
        startActivity(intent);
        finish();
    }
}