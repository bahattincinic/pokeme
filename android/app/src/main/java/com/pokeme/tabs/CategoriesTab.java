package com.pokeme.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pokeme.R;
import com.pokeme.models.Category;
import com.pokeme.service.CategoryService;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategoriesTab extends Fragment {
    NetworkManager queue = NetworkManager.getInstance(getActivity());
    String token = Session.getInstance(getActivity()).getToken();
    Category[] categories;
    ListView listView;
    ArrayList<String> values;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.category_list);
        values = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Category Delete");
                dialog
                        .setMessage("Are you sure really want to delete it ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                try {
                                    JsonObjectRequest request = CategoryService.deleteCategory(token, 1, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject instance) {
                                            adapter.clear();
                                            fetchCategories();
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Log.w(this.getClass().getSimpleName(), error.toString());
                                        }
                                    });
                                    queue.add(request);
                                } catch (JSONException e) {
                                    Log.w(this.getClass().getSimpleName(), e.toString());
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchCategories();
    }

    public void fetchCategories() {
        try {
            JsonObjectRequest request = CategoryService.getCategories(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    try {
                        JSONArray list = instance.getJSONArray("results");
                        categories = new Gson().fromJson(list.toString(), Category[].class);
                        fillListView();
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

    public void fillListView() {
        for (int i=0; i < categories.length; i++) {
            values.add(categories[i].getName());
        }
        adapter.notifyDataSetChanged();
    }
}