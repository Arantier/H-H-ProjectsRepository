package ru.android_school.h_h.eightapp.note_list;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_create.EditActivity;
import ru.android_school.h_h.eightapp.note_trio.Note;
import ru.android_school.h_h.eightapp.note_trio.NoteDatabase;

public class ListActivity extends AppCompatActivity {

    //================
    //ТЕХНИЧЕСКИЕ ПОЛЯ
    //================
    NoteDatabase db;
    Flowable<List<Note>> flowableDatabase;
    ArrayList<Note> exposedNotes;
    ArrayList<Note> receivedNotes;
    NoteListAdapter listAdapter;

    //================
    //ВИЗУАЛЬНЫЕ ПОЛЯ
    //================
    MenuItem searchMenuItem;

    public void replaceList(List<Note> replacerList, boolean showArchivedNotes) {
        exposedNotes.clear();
        if (!showArchivedNotes) {
            for (Note n : replacerList) {
                if (!n.isArchived){
                    exposedNotes.add(n);
                }
            }
        } else {
            exposedNotes.addAll(replacerList);
        }
        listAdapter.notifyDataSetChanged();
        if (exposedNotes.size() != 0) {
            findViewById(R.id.emptyListReplacer).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.emptyListReplacer).setVisibility(View.VISIBLE);
        }
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_search);
        searchMenuItem = toolbar.getMenu().findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                replaceList(receivedNotes,false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<Note> requestedList = new ArrayList<>();
                for (Note n : receivedNotes){
                    if (n.title.contains(s) || n.text.contains(s)){
                        requestedList.add(n);
                    }
                }
                replaceList(requestedList,true);
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
        receivedNotes = new ArrayList<>();
        exposedNotes = new ArrayList<>();
        listAdapter = new NoteListAdapter(exposedNotes);
        noteListView.setAdapter(listAdapter);
        noteListView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        db = Room.databaseBuilder(this, NoteDatabase.class, "NoteDatabase")
                .build();
        db.noteDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        progressDialog.dismiss();
                        searchMenuItem.collapseActionView();
                        receivedNotes = (ArrayList) notes;
                        replaceList(receivedNotes,false);
                    }
                });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Action action;
        switch (item.getItemId()) {
            case (R.id.note_delete):
                action = new Action() {
                    @Override
                    public void run() throws Exception {
                        db.noteDao()
                                .delete(listAdapter.selectedNote);
                    }
                };
                break;
            case (R.id.note_archive):
                listAdapter.selectedNote.isArchived = !listAdapter.selectedNote.isArchived;
                action = new Action() {
                    @Override
                    public void run() throws Exception {
                        db.noteDao()
                                .update(listAdapter.selectedNote);
                    }
                };
                break;
            default:
                action = new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("onContextItemSelected", "Тело метода run в onContextItemReceived пусто. Что-то не так с условием?");
                    }
                };
                break;
        }
        Completable.fromAction(action).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (EditActivity.REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    final Note newNote = data.getParcelableExtra(EditActivity.NEW_NOTE);
                    Completable.fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            db.noteDao()
                                    .insert(newNote);
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
                break;
        }
    }
}
