package com.pokeme;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPassword;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtUsername = (TextView) findViewById(R.id.txtSignupUsername);
        txtPassword = (TextView) findViewById(R.id.txtSignupPassword);
        dialog = new ProgressDialog(SignupActivity.this);
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

        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
    }
}
