package ru.android_school.h_h.eightapp.note_list;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_trio.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    public static final int NOTE_COMPLETE = 1;
    public static final int NOTE_TITLE = 2;
    public static final int NOTE_TEXT = 3;

    ArrayList<Note> listOfNotes;

    Note selectedNote;

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener {

        Note note;
        View noteView;
        TextView titleView;
        TextView textView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteView = itemView;
            titleView = itemView.findViewById(R.id.noteTitle);
            textView = itemView.findViewById(R.id.noteText);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Note note) {
            this.note = note;
            if (!note.title.isEmpty()) {
                titleView.setText(note.title);
            }
            if (!note.text.isEmpty()) {
                textView.setText(note.text);
            }
            if (note.backgroundColorResource != R.color.white) {
                noteView.setBackgroundResource(note.backgroundColorResource);
                titleView.setTextColor(Color.WHITE);
                textView.setTextColor(Color.WHITE);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = new MenuInflater(view.getContext());
            inflater.inflate(R.menu.context_note, contextMenu);
        }

        @Override
        public boolean onLongClick(View view) {
            selectedNote = note;
            return false;
        }
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case NOTE_COMPLETE:
                return new NoteViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.note_layout_complete, viewGroup, false));
            case NOTE_TITLE:
                return new NoteViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.note_layout_title, viewGroup, false));
            case NOTE_TEXT:
                return new NoteViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.note_layout_text, viewGroup, false));
            default:
                View emptyView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.note_layout_complete, viewGroup, false);
                emptyView.setVisibility(View.GONE);
                return new NoteViewHolder(emptyView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder viewHolder, int i) {
        viewHolder.bind(listOfNotes.get(i));
        Log.i("onBindViewHolder", "Забайнден элемент:" + listOfNotes.get(i));
    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }

    public NoteListAdapter(ArrayList<Note> listOfNotes) {
        this.listOfNotes = listOfNotes;
    }

    @Override
    public int getItemViewType(int position) {
        if (!listOfNotes.get(position).title.isEmpty()) {
            if (!listOfNotes.get(position).text.isEmpty()) {
                return NOTE_COMPLETE;
            } else {
                return NOTE_TITLE;
            }
        } else {
            return NOTE_TEXT;
        }
    }
}
