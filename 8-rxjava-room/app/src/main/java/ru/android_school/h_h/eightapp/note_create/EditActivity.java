package ru.android_school.h_h.eightapp.note_create;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_trio.Note;

public class EditActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String NEW_NOTE = "new_note";
    Note newNote;
    Toolbar toolbar;
    TextInputEditText editTitle;
    EditText editText;

    private void setToolbar(){
        toolbar = findViewById(R.id.editToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote.title = editTitle.getText().toString();
                newNote.text = editText.getText().toString();
                if ((newNote.title+newNote.text).equals("")){
                    setResult(AppCompatActivity.RESULT_CANCELED);
                } else {
                    Intent result = new Intent();
                    result.putExtra(NEW_NOTE, newNote);
                    setResult(AppCompatActivity.RESULT_OK, result);
                }
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_edit_toolbar);
        toolbar.getMenu().findItem(R.id.pickColor).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                PickColorDialog.newInstance(newNote)
                        .show(getSupportFragmentManager(),PickColorDialog.TAG);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTitle = findViewById(R.id.editNoteTitle);
        editText = findViewById(R.id.editNoteText);
        newNote = new Note("Note unready","If you see this text, then something went wrong");
        setToolbar();
    }
}
