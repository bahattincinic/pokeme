package com.pokeme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.models.User;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.Session;
import com.pokeme.service.UserService;
import com.pokeme.service.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPassword;
    ProgressDialog dialog;
    NetworkManager queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (TextView) findViewById(R.id.txtLoginUsername);
        txtPassword = (TextView) findViewById(R.id.txtLoginPassword);
        dialog = new ProgressDialog(LoginActivity.this);
        queue = NetworkManager.getInstance(this);
    }

    public void onSubmit(View view) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        final Context context = getApplicationContext();

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

        try {
            JsonObjectRequest request = UserService.authenticate(username, password, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("user", instance.toString());

                    User user = new Gson().fromJson(instance.toString(), User.class);
                    Session.getInstance(context).setToken(user.getToken());

                    dialog.hide();
                    finish();
                    startActivity(intent);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.w(this.getClass().getSimpleName(), error.toString());
                    Toast.makeText(context, getString(R.string.login_invalid), Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
