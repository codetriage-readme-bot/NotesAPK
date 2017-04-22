package com.gmail.lusersks.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.data.NotesData;
import com.gmail.lusersks.notes.R;

public class FormActivity extends AppCompatActivity {

    private EditText etNoteTitle;
    private EditText etNoteContent;
    private Intent intent;
    private String oldTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etNoteTitle = (EditText) findViewById(R.id.et_note_title);
        etNoteContent = (EditText) findViewById(R.id.et_note_content);

        intent = this.getIntent();
        setTitle(intent.getStringExtra(MainActivity.EXTRA_FORM_TITLE));
        oldTitle = intent.getStringExtra(MainActivity.EXTRA_NOTE);
        etNoteTitle.setText(oldTitle);
        etNoteContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_form_save) {
            String title = etNoteTitle.getText().toString();
            String content = etNoteContent.getText().toString();

            if (getTitle().equals("New note")) {
                NotesData.addItem(this, title, content);
            } else {
                NotesData.editItem(this, oldTitle, title, content);
                intent.putExtra(MainActivity.EXTRA_NOTE, title);
                intent.putExtra(MainActivity.EXTRA_CONTENT, content);
            }

            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        if (id == R.id.menu_settings) {
            Intent intent = new Intent();
            intent.setClass(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
