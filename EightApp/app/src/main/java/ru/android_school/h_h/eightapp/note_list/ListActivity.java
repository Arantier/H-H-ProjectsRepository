package ru.android_school.h_h.eightapp.note_list;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_create.EditActivity;
import ru.android_school.h_h.eightapp.note_trio.Note;
import ru.android_school.h_h.eightapp.note_trio.NoteDatabase;

public class ListActivity extends AppCompatActivity {

    NoteDatabase db;
    ArrayList<Note> listOfNotes;
    NoteListAdapter listAdapter;

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_search);
        MenuItem searchMenuItem = toolbar.getMenu().findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Toast.makeText(ListActivity.this, "Поиск развёрнут", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Toast.makeText(ListActivity.this, "Поиск свёрнут", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(ListActivity.this, "Ищется строка: " + s, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setToolbar();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListActivity.this, EditActivity.class), EditActivity.REQUEST_CODE);
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        RecyclerView noteListView = findViewById(R.id.recyclerView);
        listOfNotes = new ArrayList<>();
        listAdapter = new NoteListAdapter(listOfNotes);
        noteListView.setAdapter(listAdapter);
        noteListView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        db = Room.databaseBuilder(this, NoteDatabase.class, "NoteDatabase")
                .allowMainThreadQueries()
                .build();
        db.noteDao()
                .getAllLive()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        progressDialog.dismiss();
                        Toast.makeText(ListActivity.this, "List updated", Toast.LENGTH_SHORT)
                                .show();
                        /*String notesString = "";
                        for(Note n : notes){
                            notesString+=n.toString()+"\n=====\n";
                        }
                        Log.i("CurrentListState","Array of notes:"+notesString);*/
                        listOfNotes.clear();
                        listOfNotes.addAll(notes);
                        listAdapter.notifyDataSetChanged();
                        if (listOfNotes.size() != 0) {
                            findViewById(R.id.emptyListReplacer).setVisibility(View.INVISIBLE);
                        } else {
                            findViewById(R.id.emptyListReplacer).setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.note_delete):
                db.noteDao()
                        .delete(listAdapter.selectedNote);
                break;
            case (R.id.note_archive):
                listAdapter.selectedNote.isArchived = true;
                db.noteDao()
                        .update(listAdapter.selectedNote);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (EditActivity.REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    Note newNote = data.getParcelableExtra(EditActivity.NEW_NOTE);
                    db.noteDao()
                            .insert(newNote);
                }
                break;
        }
    }
}
