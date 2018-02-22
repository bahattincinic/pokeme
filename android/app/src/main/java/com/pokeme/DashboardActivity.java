package com.pokeme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.google.gson.Gson;
import com.pokeme.models.User;

public class DashboardActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        user = new Gson().fromJson(intent.getStringExtra("user"), User.class);
    }
}
