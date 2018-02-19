package com.pokeme;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (TextView) findViewById(R.id.txtLoginUsername);
        txtPassword = (TextView) findViewById(R.id.txtLoginPassword);
    }

    public void onSubmit(View view) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        Context context = getApplicationContext();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(context, getString(R.string.username_required_message), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, getString(R.string.password_required_message), Toast.LENGTH_LONG).show();
            return;
        }
    }
}