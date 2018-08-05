package ru.androidschool.h_h.eightapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<Note> listOfNotes;

    public class NoteHolder extends RecyclerView.ViewHolder {

        View noteView;
        TextView titleView;
        TextView textView;
        int backgroundColor;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteView = itemView;
            titleView = itemView.findViewById(R.id.noteTitle);
            textView = itemView.findViewById(R.id.noteText);
        }

        public void bind(Note note) {
            titleView.setText(note.title);
            textView.setText(note.text);
            if (note.backgroundColor != 0xFFFFFF) {
                noteView.setBackgroundColor(note.backgroundColor);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
