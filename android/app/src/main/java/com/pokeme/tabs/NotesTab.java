package com.pokeme.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pokeme.R;
import com.pokeme.components.NoteRecyclerViewAdapter;
import com.pokeme.components.OnItemClickListener;
import com.pokeme.fragments.NoteDetailFragment;
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
    String token = Session.getInstance(getActivity()).getApiToken();
    Note[] notes;
    RecyclerView mRecyclerView;
    NoteRecyclerViewAdapter adapter;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_notes_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void fetchNotes() {
        try {
            JsonObjectRequest request = NoteService.getNotes(token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject instance) {
                    try {
                        JSONArray list = instance.getJSONArray("results");
                        notes = new Gson().fromJson(list.toString(), Note[].class);

                        adapter = new NoteRecyclerViewAdapter(getContext(), notes);
                        mRecyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Note item) {
                                Fragment fragment = new NoteDetailFragment();
                                Bundle args = new Bundle();
                                args.putInt("noteId", item.getId());
                                args.putString("noteTitle", item.getTitle());
                                args.putString("noteText", item.getText());
                                fragment.setArguments(args);

                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            }
                        });
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