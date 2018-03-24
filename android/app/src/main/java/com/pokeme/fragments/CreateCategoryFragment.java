package com.pokeme.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.pokeme.LoginActivity;
import com.pokeme.R;
import com.pokeme.service.CategoryService;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONException;
import org.json.JSONObject;


public class CreateCategoryFragment extends Fragment implements View.OnClickListener {
    NetworkManager queue;
    ProgressDialog dialog;

    public CreateCategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.create_new_category);

        queue = NetworkManager.getInstance(getContext());
        dialog = new ProgressDialog(getContext());

        return inflater.inflate(R.layout.fragment_create_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.btnCreateCategory);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText input = (EditText) getView().findViewById(R.id.txtCategoryName);
        String name = input.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(
                getContext(),
                getString(R.string.category_name_required_message),
                Toast.LENGTH_LONG
            ).show();
            return;
        }

        String token = Session.getInstance(getActivity()).getToken();

        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        try {
            JsonObjectRequest request = CategoryService.createCategory(name, token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    dialog.hide();
                    Fragment fragment = new ListFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
