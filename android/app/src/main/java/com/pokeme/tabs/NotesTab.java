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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.R;
import com.pokeme.models.Note;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.NoteService;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NotesTab extends Fragment {
    NetworkManager queue = NetworkManager.getInstance(getActivity());
    String token = Session.getInstance(getActivity()).getToken();
    Note[] notes;
    ListView listView;
    ArrayAdapter<Note> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_notes, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchNotes();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.notes_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = (Note) adapterView.getItemAtPosition(i);
                deleteNote(note);
            }
        });
    }

    public void deleteNote(final Note note) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.note_delete_title);
        dialog
                .setMessage(R.string.delete_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            JsonObjectRequest request = NoteService.deleteNote(token, note.getId(), new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject instance) {
                                    fetchNotes();
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

    public void fetchNotes() {
        try {
            JsonObjectRequest request = NoteService.getNotes(token, new VolleyCallback() {
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