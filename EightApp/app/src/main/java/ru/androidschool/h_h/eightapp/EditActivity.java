package ru.androidschool.h_h.eightapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class EditActivity extends AppCompatActivity {
    public static final String INTENT_TAG = "result note";
    public static final int REQUEST_CODE = 1;
    Note newNote;
    Toolbar toolbar;
    TextInputEditText editTitle;
    TextInputEditText editText;

    private void setToolbar(){
        toolbar = findViewById(R.id.editToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(listener->{
            String title = editTitle.getText().toString();
            String text = editText.getText().toString();
            if ((title+text).equals("")){
                setResult(AppCompatActivity.RESULT_CANCELED);
            } else {
                newNote.setTitle(title);
                newNote.setText(text);
                Intent result = new Intent();
                result.putExtra(INTENT_TAG, newNote);
                setResult(AppCompatActivity.RESULT_OK, result);
            }
            finish();
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
        setToolbar();
    }
}
