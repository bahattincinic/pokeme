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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.pokeme.R;
import com.pokeme.models.Category;
import com.pokeme.service.CategoryService;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.NoteService;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CreateNoteFragment extends Fragment implements View.OnClickListener {
    NetworkManager queue;
    String token;
    Spinner categorySelect;
    EditText noteTitle;
    EditText noteText;
    EditText noteReminderDate;
    EditText noteReminderTime;
    Category[] categories;
    ArrayAdapter<Category> adapter;
    ProgressDialog dialog;

    public CreateNoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.create_new_note);
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.btnCreateNote);
        button.setOnClickListener(this);

        token = Session.getInstance(getActivity()).getApiToken();
        queue = NetworkManager.getInstance(getContext());
        dialog = new ProgressDialog(getContext());

        categorySelect = (Spinner)view.findViewById(R.id.categorySelect);
        noteTitle = (EditText)view.findViewById(R.id.txtNoteTitle);
        noteText = (EditText)view.findViewById(R.id.txtNoteText);
        noteReminderDate = (EditText)view.findViewById(R.id.txtReminderDate);
        noteReminderTime = (EditText)view.findViewById(R.id.txtReminderTime);

        fillCategorySpinner();
    }

    @Override
    public void onClick(View view) {
        String title = noteTitle.getText().toString();
        String text = noteText.getText().toString();
        String reminderDate = noteReminderDate.getText().toString();
        String reminderTime = noteReminderTime.getText().toString();
        String reminder = null;
        Integer category = null;

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(
                    getContext(),
                    getString(R.string.note_name_required_message),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(
                    getContext(),
                    getString(R.string.note_content_required_message),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        if (!TextUtils.isEmpty(reminderDate) && !TextUtils.isEmpty(reminderTime)) {
            reminder = reminderDate + " " + reminderTime;
        }

        Category selectedItem = (Category)categorySelect.getSelectedItem();
        if (selectedItem != null) {
            category = selectedItem.getId();
        }

        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        try {
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            JsonObjectRequest request = NoteService.createNote(title, text, category, reminder, token, deviceToken, new VolleyCallback() {
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

    public void fillCategorySpinner() {
        try {
            JsonObjectRequest request = CategoryService.getCategories(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    try {
                        JSONArray list = instance.getJSONArray("results");
                        categories = new Gson().fromJson(list.toString(), Category[].class);

                        adapter = new ArrayAdapter<Category>(getContext(),
                                android.R.layout.simple_spinner_item, categories);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySelect.setAdapter(adapter);
                    } catch (JSONException e) {
                        Log.w(this.getClass().getSimpleName(), e.toString());
                    }
                }
                @Override
                public void onError(VolleyError error) {
                    Log.w(this.getClass().getSimpleName(), error.toString());
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
