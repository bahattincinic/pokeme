package com.pokeme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.UserService;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {
    ProgressDialog dialog;
    NetworkManager queue;

    public void redirectToWelcome() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        finish();
        startActivity(intent);
    }

    public void redirectToDashboard(String user) {
        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
        intent.putExtra("user", user);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        queue = NetworkManager.getInstance(this);
        String token = Session.getInstance(this).getToken();

        if (token == null || token.isEmpty()) {
            redirectToWelcome();
            return;
        }

        dialog = new ProgressDialog(SplashActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        try {
            JsonObjectRequest request = UserService.getProfile(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    dialog.hide();
                    redirectToDashboard(instance.toString());
                }

                @Override
                public void onError(VolleyError error) {
                    dialog.hide();
                    redirectToWelcome();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( dialog!=null && dialog.isShowing() ){
            dialog.cancel();
        }
    }
}
