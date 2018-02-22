package com.pokeme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.Session;
import com.pokeme.service.UserService;
import com.pokeme.service.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class WelcomeActivity extends AppCompatActivity {
    ProgressDialog dialog;
    NetworkManager queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        queue = NetworkManager.getInstance(this);
        String token = Session.getInstance(this).getToken();

        if (token == null || token.isEmpty()) {
            return;
        }

        dialog = new ProgressDialog(WelcomeActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        try {
            JsonObjectRequest request = UserService.profile(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                    intent.putExtra("user", instance.toString());

                    dialog.hide();
                    finish();
                    startActivity(intent);
                }

                @Override
                public void onError(VolleyError error) {
                    dialog.hide();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
