package com.pokeme.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.R;
import com.pokeme.models.Note;
import com.pokeme.service.CategoryService;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListCategoryFragment extends Fragment {
    NetworkManager queue = NetworkManager.getInstance(getActivity());
    String token = Session.getInstance(getActivity()).getToken();
    String categoryName;
    Integer categoryId;
    Note[] notes;
    ListView listView;
    ArrayAdapter<Note> adapter;

    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryId = getArguments().getInt("categoryId");
        categoryName = getArguments().getString("categoryName");
    }

    public void deleteCategory() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.category_delete_title);
        dialog
                .setMessage(R.string.delete_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            JsonObjectRequest request = CategoryService.deleteCategory(token, categoryId, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject instance) {
                                    Fragment fragment = new ListFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(categoryName);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.category_notes_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = (Note) adapterView.getItemAtPosition(i);

                Fragment fragment = new NoteDetailFragment();
                Bundle args = new Bundle();
                args.putInt("noteId", note.getId());
                args.putString("noteTitle", note.getTitle());
                args.putString("noteText", note.getText());
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.listview_footer,listView,false);
        listView.addFooterView(footer,null,false);

        Button btn = (Button) view.findViewById(R.id.btnDeleteCategory);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory();
            }
        });

        fetchNotes();
    }

    public void fetchNotes() {
        try {
            JsonObjectRequest request = CategoryService.getCategoryNotes(token, categoryId, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    try {
                        JSONArray list = instance.getJSONArray("results");
                        notes = new Gson().fromJson(list.toString(), Note[].class);

                        adapter = new ArrayAdapter<Note>(getContext(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, notes);
                        listView.setAdapter(adapter);
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
