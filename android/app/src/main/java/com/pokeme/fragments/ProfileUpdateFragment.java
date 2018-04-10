package com.pokeme.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.R;
import com.pokeme.models.User;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.UserService;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileUpdateFragment extends Fragment implements View.OnClickListener {
    ProgressDialog dialog;
    NetworkManager queue;
    String token;
    EditText username;
    EditText password;
    User user;

    public ProfileUpdateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.profile_update);
        return inflater.inflate(R.layout.fragment_profile_update, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        token = Session.getInstance(getActivity()).getApiToken();
        queue = NetworkManager.getInstance(getContext());
        dialog = new ProgressDialog(getContext());

        Button button = (Button) view.findViewById(R.id.btnProfileUpdate);
        button.setOnClickListener(this);

        username = (EditText)view.findViewById(R.id.txtUsername);
        password = (EditText)view.findViewById(R.id.txtPassword);

        fetchProfile();
    }

    public void fetchProfile() {
        try {
            JsonObjectRequest request = UserService.getProfile(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    dialog.hide();
                    user = new Gson().fromJson(instance.toString(), User.class);
                    // set initial data
                    username.setText(user.getUsername());
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

    @Override
    public void onClick(View view) {
        String changedUsername = username.getText().toString();
        String newPassword = password.getText().toString();

        if (TextUtils.isEmpty(changedUsername)) {
            Toast.makeText(
                    getContext(),
                    getString(R.string.username_required_message),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        try {
            JsonObjectRequest request = UserService.updateProfile(token, changedUsername, newPassword, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    dialog.hide();
                    Toast.makeText(
                            getContext(),
                            getString(R.string.profile_update_success),
                            Toast.LENGTH_LONG
                    ).show();
                }

                @Override
                public void onError(VolleyError error) {
                    dialog.hide();
                    Log.w(this.getClass().getSimpleName(), error.toString());
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
