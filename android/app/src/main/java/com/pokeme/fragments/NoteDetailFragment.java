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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pokeme.R;
import com.pokeme.service.NetworkManager;
import com.pokeme.service.NoteService;
import com.pokeme.service.VolleyCallback;
import com.pokeme.store.Session;

import org.json.JSONException;
import org.json.JSONObject;


public class NoteDetailFragment extends Fragment implements View.OnClickListener {
    NetworkManager queue = NetworkManager.getInstance(getActivity());
    String token = Session.getInstance(getActivity()).getToken();
    String noteTitle;
    Integer noteId;
    String noteText;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteId = getArguments().getInt("noteId");
        noteTitle = getArguments().getString("noteTitle");
        noteText = getArguments().getString("noteText");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView title = (TextView)view.findViewById(R.id.txtDetaiNoteTitle);
        TextView text = (TextView)view.findViewById(R.id.txtDetailNoteText);

        title.setText(noteTitle);
        text.setText(noteText);

        Button button = (Button) view.findViewById(R.id.btnDetailDeleteNote);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.note_delete_title);
        dialog
                .setMessage(R.string.delete_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            JsonObjectRequest request = NoteService.deleteNote(token, noteId, new VolleyCallback() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

}
