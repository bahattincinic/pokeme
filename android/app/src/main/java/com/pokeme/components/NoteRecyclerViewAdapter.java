package com.pokeme.components;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pokeme.R;
import com.pokeme.models.Note;


public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.CustomViewHolder> {
    private Note[] noteItemList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public NoteRecyclerViewAdapter(Context context, Note[] noteItemList) {
        this.noteItemList = noteItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_card_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Note note = noteItemList[i];

        customViewHolder.cardTitle.setText(Html.fromHtml(note.getTitle()));
        customViewHolder.cardText.setText(Html.fromHtml(note.getText()));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(note);
            }
        };
        customViewHolder.cardTitle.setOnClickListener(listener);
        customViewHolder.cardText.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != noteItemList ? noteItemList.length : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView cardTitle;
        protected TextView cardText;

        public CustomViewHolder(View view) {
            super(view);
            this.cardTitle = (TextView) view.findViewById(R.id.cardTitle);
            this.cardText = (TextView) view.findViewById(R.id.cardText);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}