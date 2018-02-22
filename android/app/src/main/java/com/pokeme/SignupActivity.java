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
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.models.User;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.Session;
import com.pokeme.service.UserService;
import com.pokeme.service.VolleyCallback;

import org.json.JSONException;


public class SignupActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPassword;
    ProgressDialog dialog;
    NetworkManager queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtUsername = (TextView) findViewById(R.id.txtSignupUsername);
        txtPassword = (TextView) findViewById(R.id.txtSignupPassword);
        dialog = new ProgressDialog(SignupActivity.this);
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
            JsonObjectRequest request = UserService.signup(username, password, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
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
                    Toast.makeText(context, getString(R.string.username_used), Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            Log.w(this.getClass().getSimpleName(), e.toString());
        }
    }
}
